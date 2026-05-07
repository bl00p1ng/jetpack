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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unilibre.taller04.domain.model.TipoTransaccion
import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.presentation.theme.GastoColor
import com.unilibre.taller04.presentation.theme.IngresoColor
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransaccionItem(
    transaccion: Transaccion,
    modifier: Modifier = Modifier
) {
    val nf = remember { NumberFormat.getCurrencyInstance(Locale("es", "CO")) }
    val df = remember { SimpleDateFormat("dd MMM yyyy", Locale("es", "CO")) }
    val color = if (transaccion.tipo == TipoTransaccion.INGRESO) IngresoColor else GastoColor
    val signo = if (transaccion.tipo == TipoTransaccion.INGRESO) "+" else "-"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(end = 12.dp)) {
                Text(transaccion.descripcion, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${transaccion.categoria.display} · ${df.format(Date(transaccion.fecha))}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "$signo${nf.format(transaccion.monto)}",
                style = MaterialTheme.typography.titleMedium,
                color = color
            )
        }
    }
}
