package com.unilibre.taller04.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.Shape
import com.unilibre.taller04.domain.model.PuntoMensual
import com.unilibre.taller04.presentation.theme.GastoColor
import com.unilibre.taller04.presentation.theme.IngresoColor

@Composable
fun GraficoBarras(
    puntos: List<PuntoMensual>,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(puntos) {
        if (puntos.isNotEmpty()) {
            modelProducer.runTransaction {
                columnSeries {
                    series(puntos.map { it.ingresos })
                    series(puntos.map { it.gastos })
                }
            }
        }
    }

    val ingresoColumn = rememberLineComponent(
        color = IngresoColor,
        thickness = 12.dp,
        shape = Shape.rounded(4)
    )
    val gastoColumn = rememberLineComponent(
        color = GastoColor,
        thickness = 12.dp,
        shape = Shape.rounded(4)
    )

    val labels = puntos.map { it.etiqueta }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = "Ingresos vs Gastos (últimos 6 meses)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
        CartesianChartHost(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(12.dp),
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                        ingresoColumn, gastoColumn
                    ),
                    mergeMode = { ColumnCartesianLayer.MergeMode.Grouped() }
                ),
                startAxis = rememberStartAxis(
                    label = rememberAxisLabelComponent()
                ),
                bottomAxis = rememberBottomAxis(
                    label = rememberAxisLabelComponent(),
                    valueFormatter = { x, _, _ ->
                        labels.getOrNull(x.toInt()) ?: ""
                    }
                )
            ),
            modelProducer = modelProducer,
            scrollState = rememberVicoScrollState(scrollEnabled = false)
        )
    }
}
