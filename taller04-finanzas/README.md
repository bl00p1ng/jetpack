# Taller 04 — Finanzas Personales (Room + Hilt + Clean Architecture)

App Android en Jetpack Compose que permite registrar, listar y eliminar transacciones de ingresos y gastos, mostrando un dashboard con resumen, gráfica de barras (Vico) y movimientos recientes.

## Cómo abrir el proyecto

1. Abrir Android Studio Ladybug o superior.
2. `File > Open` y seleccionar la carpeta `taller04-finanzas/`.
3. Esperar la sincronización Gradle (descargará Gradle 8.10.2, AGP 8.7.3, KSP, Hilt, Room y Vico).
4. Ejecutar en un emulador con `minSdk 24` o superior.

> No se incluye `gradle-wrapper.jar`. Android Studio lo genera automáticamente al sincronizar, o puedes ejecutar `gradle wrapper` desde una instalación local de Gradle.

## Arquitectura — Clean Architecture

```
presentation  →  domain  ←  data
```

- **domain/**: lógica pura de negocio. Sin dependencias de Android. Contiene `model`, `repository` (interfaz) y `usecase`.
- **data/**: implementación de persistencia con Room (`local`), `mapper` para entidad ↔ modelo y `repository` que implementa la interfaz del dominio.
- **presentation/**: Compose UI por feature (`dashboard`, `agregar`, `lista`), `theme`, `components` reutilizables y `navigation`.
- **di/**: módulos de Hilt — `DatabaseModule` (provee Room) y `RepositoryModule` (`@Binds` de la implementación a la interfaz).

La regla de dependencias se respeta: `presentation` y `data` apuntan a `domain`; `domain` no conoce a nadie.

## Funcionalidad

- **DashboardScreen**: `TarjetaResumen` (ingresos / gastos / balance), `GraficoBarras` (Vico, últimos 6 meses), lista de movimientos recientes y FAB para agregar.
- **AgregarTransaccionScreen**: campos de descripción y monto, `SegmentedButton` para tipo (Ingreso / Gasto), `LazyRow` de `ChipCategoria`. El `AgregarTransaccionUseCase` valida que la descripción no esté vacía y el monto sea mayor que cero.
- **ListaTransaccionesScreen**: lista completa con `SwipeToDismissBox` y diálogo de confirmación antes de eliminar.

## Sistema de Diseño asistido con IA

Entregable de la actividad IA del taller:

- **Paleta** (`theme/Color.kt`): tokens light y dark con colores semánticos `IngresoColor` y `GastoColor`.
- **Tipografía** (`theme/Type.kt`): jerarquía Material 3 (`displaySmall`, `headline*`, `title*`, `body*`, `label*`).
- **Formas** (`theme/Shape.kt`): `medium = 12.dp`, `large = 20.dp`.
- **Componentes reutilizables**:
  - `TarjetaResumen` — card con balance y desglose.
  - `ChipCategoria` — `FilterChip` para selección de categoría.
  - `GraficoBarras` — `CartesianChartHost` de Vico con dos series agrupadas (ingresos / gastos).

## Mapeo de la rúbrica

| Criterio | Implementación |
|----------|----------------|
| Room funcional (CRUD) | `data/local/AppDatabase.kt`, `TransaccionDao.kt`, `TransaccionEntity.kt`, `Converters.kt` |
| Hilt configurado | `TallerApp` (`@HiltAndroidApp`), `MainActivity` (`@AndroidEntryPoint`), `di/DatabaseModule.kt`, `di/RepositoryModule.kt`, ViewModels (`@HiltViewModel`) |
| Clean Architecture | Capas `data` / `domain` / `presentation` con dependencias unidireccionales; `domain` puro Kotlin |
| Gráfica de datos | `presentation/components/GraficoBarras.kt` con Vico 2.0.0-alpha.28 (Compose-native) |
| Sistema de diseño IA | `presentation/theme/*` + tres componentes reutilizables descritos arriba |

## Stack técnico

- Gradle 8.10.2, AGP 8.7.3, Kotlin 2.1.0, KSP 2.1.0-1.0.29
- Compose BOM 2024.12.01, Material 3
- Hilt 2.52, hilt-navigation-compose 1.2.0
- Room 2.6.1 (KSP), Navigation Compose 2.8.5
- Vico `compose` y `compose-m3` 2.0.0-alpha.28
- Lifecycle Runtime Compose 2.8.7, Coroutines 1.9.0
- compileSdk / targetSdk 35, minSdk 24, JDK 17

## Notas sobre Vico (alpha)

Vico 2.0.0-alpha.28 es la API Compose-native moderna (sustituye a MPAndroidChart, basado en Views). Usa `CartesianChartHost`, `rememberCartesianChart`, `rememberColumnCartesianLayer` y `CartesianChartModelProducer.runTransaction { columnSeries { ... } }`. Al ser alpha, paquetes y firmas pueden moverse entre versiones — cualquier upgrade requiere revisar la guía de migración.
