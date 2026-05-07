#!/usr/bin/env bash
# Lanza cualquiera de las apps Android del directorio sin usar Android Studio.
# Uso:
#   ./run-app.sh                        -> menú interactivo (proyecto + AVD)
#   ./run-app.sh <proyecto>             -> compila e instala ese proyecto en el primer AVD
#   ./run-app.sh <proyecto> <avd>       -> usa el AVD especificado
#   ./run-app.sh -l                     -> lista proyectos y AVDs

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
resolve_sdk() {
  for c in "${ANDROID_HOME:-}" "${ANDROID_SDK_ROOT:-}" "$HOME/Library/Android/sdk"; do
    [[ -n "$c" && -x "$c/platform-tools/adb" && -x "$c/emulator/emulator" ]] && { echo "$c"; return; }
  done
}
SDK="$(resolve_sdk)"
if [[ -z "$SDK" ]]; then
  echo "ERROR: no encuentro un Android SDK válido (ANDROID_HOME=${ANDROID_HOME:-unset})" >&2
  echo "Probé: \$ANDROID_HOME, \$ANDROID_SDK_ROOT, ~/Library/Android/sdk" >&2
  exit 1
fi
ADB="$SDK/platform-tools/adb"
EMULATOR="$SDK/emulator/emulator"

# --- Helpers --------------------------------------------------------------

list_projects() {
  for d in "$ROOT_DIR"/*/; do
    [[ -f "$d/settings.gradle.kts" || -f "$d/settings.gradle" ]] || continue
    [[ -d "$d/app" ]] || continue
    basename "$d"
  done
}

list_avds() {
  "$EMULATOR" -list-avds
}

pick_one() {
  # $1 = prompt, resto = opciones
  local prompt="$1"; shift
  local opts=("$@")
  if [[ ${#opts[@]} -eq 0 ]]; then
    echo "ERROR: no hay opciones disponibles para: $prompt" >&2
    exit 1
  fi
  if [[ ${#opts[@]} -eq 1 ]]; then
    echo "${opts[0]}"
    return
  fi
  echo "$prompt" >&2
  local i=1
  for o in "${opts[@]}"; do
    echo "  [$i] $o" >&2
    ((i++))
  done
  local choice
  read -r -p "Selecciona [1-${#opts[@]}]: " choice
  if ! [[ "$choice" =~ ^[0-9]+$ ]] || (( choice < 1 || choice > ${#opts[@]} )); then
    echo "ERROR: selección inválida" >&2
    exit 1
  fi
  echo "${opts[$((choice-1))]}"
}

ensure_gradlew() {
  # Si el proyecto no trae gradlew, reutiliza el de NewsApp.
  local proj="$1"
  if [[ -x "$proj/gradlew" ]]; then return; fi
  local donor="$ROOT_DIR/NewsApp"
  if [[ ! -x "$donor/gradlew" ]]; then
    echo "ERROR: $proj no tiene gradlew y no hay donante en NewsApp/" >&2
    exit 1
  fi
  echo ">> Copiando gradle wrapper desde NewsApp/ a $(basename "$proj")/"
  cp "$donor/gradlew" "$donor/gradlew.bat" "$proj/" 2>/dev/null || true
  mkdir -p "$proj/gradle/wrapper"
  cp "$donor/gradle/wrapper/"* "$proj/gradle/wrapper/" 2>/dev/null || true
  chmod +x "$proj/gradlew"
}

extract_app_id() {
  local proj="$1"
  local gradle_file="$proj/app/build.gradle.kts"
  [[ -f "$gradle_file" ]] || gradle_file="$proj/app/build.gradle"
  grep -E 'applicationId\s*[=]?\s*"' "$gradle_file" | head -1 \
    | sed -E 's/.*"([^"]+)".*/\1/'
}

wait_for_boot() {
  local serial="$1"
  echo ">> Esperando que el emulador termine de bootear..."
  "$ADB" -s "$serial" wait-for-device
  local booted=""
  for _ in $(seq 1 120); do
    booted="$("$ADB" -s "$serial" shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')"
    [[ "$booted" == "1" ]] && break
    sleep 2
  done
  [[ "$booted" == "1" ]] || { echo "ERROR: timeout esperando boot" >&2; exit 1; }
  "$ADB" -s "$serial" shell input keyevent 82 >/dev/null 2>&1 || true
}

start_emulator_if_needed() {
  local avd="$1"
  local running
  running="$("$ADB" devices | awk 'NR>1 && $2=="device" {print $1}' | head -1 || true)"
  if [[ -n "$running" ]]; then
    echo ">> Reutilizando dispositivo en línea: $running"
    echo "$running"
    return
  fi
  echo ">> Lanzando emulador AVD '$avd' en segundo plano..."
  nohup "$EMULATOR" -avd "$avd" >/tmp/emulator-"$avd".log 2>&1 &
  # Espera a que aparezca un serial
  for _ in $(seq 1 60); do
    sleep 2
    running="$("$ADB" devices | awk 'NR>1 && $2=="device" {print $1}' | head -1 || true)"
    [[ -n "$running" ]] && break
  done
  [[ -n "$running" ]] || { echo "ERROR: el emulador no apareció en adb" >&2; exit 1; }
  echo "$running"
}

# --- CLI ------------------------------------------------------------------

if [[ "${1:-}" == "-l" || "${1:-}" == "--list" ]]; then
  echo "Proyectos:"
  list_projects | sed 's/^/  - /'
  echo "AVDs:"
  list_avds | sed 's/^/  - /'
  exit 0
fi

mapfile -t PROJECTS < <(list_projects)
PROJECT="${1:-}"
if [[ -z "$PROJECT" ]]; then
  PROJECT="$(pick_one "¿Qué proyecto querés lanzar?" "${PROJECTS[@]}")"
fi
PROJECT_DIR="$ROOT_DIR/$PROJECT"
if [[ ! -d "$PROJECT_DIR/app" ]]; then
  echo "ERROR: '$PROJECT' no es un proyecto válido" >&2
  exit 1
fi

mapfile -t AVDS < <(list_avds)
AVD="${2:-}"
if [[ -z "$AVD" ]]; then
  AVD="$(pick_one "¿Qué AVD querés usar?" "${AVDS[@]}")"
fi

# --- Ejecución ------------------------------------------------------------

ensure_gradlew "$PROJECT_DIR"
APP_ID="$(extract_app_id "$PROJECT_DIR")"
if [[ -z "$APP_ID" ]]; then
  echo "ERROR: no pude detectar applicationId en $PROJECT/app/build.gradle.kts" >&2
  exit 1
fi
echo ">> Proyecto: $PROJECT  |  applicationId: $APP_ID  |  AVD: $AVD"

SERIAL="$(start_emulator_if_needed "$AVD")"
wait_for_boot "$SERIAL"

echo ">> Compilando e instalando ($PROJECT:app:installDebug)..."
( cd "$PROJECT_DIR" && ANDROID_SERIAL="$SERIAL" ./gradlew :app:installDebug )

echo ">> Lanzando $APP_ID..."
"$ADB" -s "$SERIAL" shell monkey -p "$APP_ID" -c android.intent.category.LAUNCHER 1 >/dev/null

echo ">> Listo. App corriendo en $SERIAL."
