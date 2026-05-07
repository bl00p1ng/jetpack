# Taller 02 - Estado y Navegacion: App de Lista de Tareas

Aplicacion Android escrita en Jetpack Compose que demuestra los conceptos del segundo taller del curso: estado reactivo, listas dinamicas, navegacion entre pantallas y validacion de formularios.

## Como abrir

1. Abrir Android Studio (Ladybug | 2024.2 o superior).
2. `File -> Open` y seleccionar la carpeta `taller02-tareas`.
3. Esperar a que termine el sync de Gradle (Gradle 8.10.2, AGP 8.7.3, Kotlin 2.1.0, JDK 17).
4. Ejecutar en un emulador o dispositivo con `minSdk 24`.

## Que demuestra

- Estado local con `remember` y `mutableStateOf` (dialogo de nueva tarea).
- `rememberSaveable` para preservar la apertura del dialogo al rotar.
- Lista dinamica con `LazyColumn` y `items(..., key = { it.id })`.
- Navegacion con `NavHost` + `NavController` entre `lista` y `detalle/{id}`.
- Entrada de usuario con `OutlinedTextField` y validacion (`supportingText` en rojo si esta vacio).
- `AlertDialog` para crear nuevas tareas.
- ViewModel (`androidx.lifecycle.ViewModel`) como fuente unica de verdad usando `mutableStateListOf`.
- Animacion de fade (`animateFloatAsState`) cuando una tarea se marca como completada.
- Swipe-to-delete con el componente moderno de Material 3 `SwipeToDismissBox` (reemplazo del deprecado `SwipeToDismiss`).

## Mapeo a la rubrica

| Item de la rubrica | Donde se demuestra |
|---|---|
| Estado reactivo | `TareasViewModel` (`mutableStateListOf`), `NuevaTareaDialog` (`remember` + `mutableStateOf`), `ListaTareasScreen` (`rememberSaveable`) |
| LazyColumn | `ListaTareasScreen` con `items(tareas, key = { it.id })` |
| Navegacion | `AppNavHost` con rutas `lista` y `detalle/{id}` y argumento `NavType.StringType` |
| Validacion | `NuevaTareaDialog` con `isError` y `supportingText` rojo cuando el titulo esta vacio |
| Actividad IA (componente prompteado) | `TareaItem` con `SwipeToDismissBox`, `Checkbox`, `TextDecoration.LineThrough` y `animateFloatAsState` |

## Estructura

```
app/src/main/java/com/unilibre/taller02/
  MainActivity.kt           Aplica tema y monta AppNavHost
  AppNavHost.kt             NavHost con rutas
  model/Tarea.kt            data class
  ui/lista/
    ListaTareasScreen.kt    Pantalla con LazyColumn + FAB + dialog
    TareaItem.kt            Item con swipe, checkbox, fade
    NuevaTareaDialog.kt     AlertDialog con TextField validado
    TareasViewModel.kt      Fuente unica de verdad
  ui/detalle/
    DetalleTareaScreen.kt   Detalle con back y toggle
  ui/theme/                 Color, Type, Theme (Material 3)
```

## Notas tecnicas

- `SwipeToDismissBox` esta marcado como `@OptIn(ExperimentalMaterial3Api::class)`. Es la API estable que reemplaza al deprecado `SwipeToDismiss`. Puede cambiar de firma en futuras versiones de Material 3.
- El estado se conserva mientras el `ViewModel` siga vivo (incluyendo rotacion). Para sobrevivir a la muerte del proceso seria necesario usar `SavedStateHandle` o persistencia local.
