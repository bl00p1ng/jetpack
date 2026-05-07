package com.unilibre.taller04.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.unilibre.taller04.presentation.theme.GastoColor
import com.unilibre.taller04.presentation.theme.IngresoColor
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TarjetaResumen(
    ingresos: Double,
    gastos: Double,
    modifier: Modifier = Modifier
) {
    val balance = ingresos - gastos
    val nf = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Balance actual", style = MaterialTheme.typography.titleMedium)
            Text(
                nf.format(balance),
                style = MaterialTheme.typography.displaySmall,
                color = if (balance >= 0) IngresoColor else GastoColor
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Bloque("Ingresos", nf.format(ingresos), IngresoColor)
                Bloque("Gastos", nf.format(gastos), GastoColor)
            }
        }
    }
}

@Composable
private fun Bloque(titulo: String, valor: String, color: Color) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(titulo, style = MaterialTheme.typography.labelLarge)
        Text(valor, style = MaterialTheme.typography.titleMedium, color = color)
    }
}
