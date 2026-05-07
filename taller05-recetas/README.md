# Taller 05 - Asistente de Recetas Inteligente

App Android que combina **CameraX**, **ML Kit Image Labeling** y **Gemini API** para generar recetas a partir de los ingredientes que detecta la camara. Persistencia local con **Room** y UI completa en **Jetpack Compose** con animaciones (typewriter, transiciones, AnimatedVisibility).

## Configuracion

1. Abrir el proyecto en Android Studio (Hedgehog o superior).
2. Copiar `local.properties.example` a `local.properties` y rellenar:
   ```
   sdk.dir=/Users/<usuario>/Library/Android/sdk
   GEMINI_API_KEY=TU_API_KEY
   ```
3. Obtener la API key gratuita en https://aistudio.google.com/app/apikey.
4. Sincronizar Gradle y ejecutar en un dispositivo fisico (la camara no funciona en emuladores sin webcam mapeada).

## Permisos

- `CAMERA` (declarado en `AndroidManifest.xml`). Se solicita en runtime con `accompanist-permissions`.
- `INTERNET` (necesario para Gemini API).

## Modelo Gemini

Se usa **`gemini-2.5-flash`** via SDK `com.google.ai.client.generativeai:generativeai:0.9.0`. Es multimodal, economico y ofrece streaming. La integracion vive en `data/ai/GeminiClient.kt` y emite chunks como `Flow<String>` para alimentar el efecto typewriter en la UI.

## Arquitectura (Clean + MVVM)

```
data/
  ai/         GeminiClient, RecipeJsonParser
  ml/         ImageLabelerHelper (ML Kit)
  local/      Room (Database, Dao, Entity, Converters)
  repository/ RecetasRepositoryImpl
domain/
  model/      Receta, Ingrediente
  repository/ RecetasRepository (puerto)
  usecase/    GenerarRecetas, GuardarFavorita, ObtenerFavoritas, ObtenerReceta
presentation/
  navigation/ AppNavHost (5 rutas)
  theme/      Material3 dinamico
  components/ TypewriterText, LoadingDots, ChipIngrediente, RecetaCard
  inicio/ camara/ ingredientes/ recetas/ detalle/
di/           Hilt modules (Database, Ai, Repository)
```

## 5 Pantallas

1. **Inicio** - grid de favoritas + FAB camara.
2. **Camara** - CameraX + PreviewView + captura.
3. **Ingredientes** - chips editables, agregar/eliminar.
4. **RecetasIA** - streaming de Gemini con typewriter, parseo JSON, lista animada.
5. **DetalleReceta** - pasos, calorias, toggle favorita (Room).

## Mapeo de Rubrica

| Criterio                                | Donde verificarlo                                                                  |
|-----------------------------------------|------------------------------------------------------------------------------------|
| CameraX + ML Kit                        | `presentation/camara/CamaraScreen.kt`, `data/ml/ImageLabelerHelper.kt`             |
| Integracion API IA (streaming)          | `data/ai/GeminiClient.kt`, `RecipeJsonParser.kt`, `recetas/RecetasIAViewModel.kt` |
| Arquitectura limpia                     | Carpetas `domain/`, `data/`, `presentation/` + Hilt en `di/`                       |
| Animaciones Compose                     | `components/TypewriterText.kt`, `LoadingDots.kt`, `AnimatedVisibility` en RecetaCard y RecetasIAScreen |
| Prototipo funcional                     | 5 pantallas en `presentation/`, `AppNavHost`                                       |
| Persistencia offline                    | `data/local/` (Room) + `RecetasRepositoryImpl.toggleFavorita`                      |

## Stack

- Kotlin 2.1.0, AGP 8.7.3, JDK 17, compileSdk 35.
- Compose BOM 2024.12.01, Material3.
- Hilt 2.52, Navigation Compose 2.8.5.
- Room 2.6.1 (KSP).
- CameraX 1.4.1.
- ML Kit `image-labeling:17.0.9`.
- Gemini SDK `generativeai:0.9.0`.
- Moshi 1.15.1 (KSP) para parseo lenient del JSON de Gemini.
- accompanist-permissions 0.36.0.

## Notas tecnicas

- **Conversion ImageProxy -> InputImage**: usamos `InputImage.fromMediaImage(image, rotationDegrees)`. El `ImageProxy` se cierra siempre (incluido en error) para liberar el frame de la camara.
- **Filtrado ML Kit**: `confidence > 0.75f` para reducir ruido.
- **Streaming Gemini**: `generateContentStream(prompt)` retorna `Flow<GenerateContentResponse>`; concatenamos `response.text` y al cerrar parseamos con Moshi (limpiando posibles fences `\`\`\`json`).
- **Typewriter**: `TypewriterText` reanima desde el ultimo prefijo cuando llegan chunks nuevos, asi sentimos que el texto sigue escribiendose en vivo.
- **Sin API key**: si `GEMINI_API_KEY` esta vacia, `AiModule` provee `null` y `RecetasIAScreen` muestra un error claro. La app no crashea.
- **accompanist-permissions en SDK 35**: la libreria `0.36.0` declara compatibilidad oficial con Compose BOM 2024.12. En SDK 35 funciona sin warnings; el unico tradeoff es que `accompanist` ya esta en modo mantenimiento - cuando migremos a target 36 conviene reemplazarlo por la API nativa `rememberLauncherForActivityResult(RequestPermission())`.
