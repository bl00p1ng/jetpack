# Taller 03 - Clima (ViewModel + StateFlow + API REST)

App de clima en Jetpack Compose que consume OpenWeatherMap, expone estados con
`StateFlow` y guarda historial de busquedas en Room.

## Stack

- Kotlin 2.1.0 / AGP 8.7.3 / Gradle 8.10.2 / JDK 17
- Jetpack Compose (BOM 2024.12.01) + Material3
- Hilt 2.52 (DI)
- Retrofit 2.11.0 + Moshi 1.15.1 + OkHttp logging
- Room 2.6.1 (historial)
- Coil 2.7.0 (icono del clima)

## Configuracion

1. Crea una cuenta gratis en https://openweathermap.org/api y genera una API key.
2. Copia `local.properties.example` a `local.properties` y rellena:

   ```properties
   OPENWEATHER_API_KEY=tu_api_key_aqui
   ```

3. Abre el proyecto en Android Studio (Ladybug+) y deja sincronizar Gradle.
4. Ejecuta en un dispositivo/emulador con minSdk 24+.

> Si la key esta vacia la app compila pero mostrara un error claro en pantalla.

## Arquitectura (MVVM)

```
ui (Compose) -> ViewModel (StateFlow) -> Repository -> Retrofit (remote) + Room (local)
```

- `WeatherUiState` (sealed): `Idle`, `Loading`, `Success(WeatherData)`, `Error(msg)`.
- `WeatherViewModel` expone `uiState: StateFlow<WeatherUiState>` y `history: StateFlow<List<String>>`.
- `WeatherRepository` consulta la API y persiste cada busqueda exitosa.
- `ConditionGradient` calcula el degradado animado con `animateColorAsState`.

## Mapeo a la rubrica

| Criterio                                | Donde se cumple                                              |
|-----------------------------------------|--------------------------------------------------------------|
| MVVM con ViewModel + StateFlow          | `WeatherViewModel.kt`, `WeatherScreen.kt`                    |
| Consumo de API REST con Retrofit        | `data/remote/WeatherApi.kt`, `di/NetworkModule.kt`           |
| Estados UI (Loading/Success/Error)      | `WeatherUiState.kt`, `WeatherScreen.kt`                      |
| Diseno con IA (gradiente animado)       | `ConditionGradient.kt` (`animateColorAsState`)               |
| Extension Room (historial 5 chips)      | `data/local/*`, `HistoryRow.kt`                              |
| Hilt DI                                 | `di/*`, `TallerApp.kt`, `MainActivity.kt`                    |

## Gotchas

- KSP debe coincidir con la version de Kotlin (`2.1.0-1.0.29`).
- Hilt requiere `@HiltAndroidApp` en `TallerApp` y `android:name=".TallerApp"` en `AndroidManifest.xml`.
- Recuerda habilitar `buildFeatures.buildConfig = true` para usar `BuildConfig.OPENWEATHER_API_KEY`.
- Usa `collectAsStateWithLifecycle()` (no `collectAsState()`).
