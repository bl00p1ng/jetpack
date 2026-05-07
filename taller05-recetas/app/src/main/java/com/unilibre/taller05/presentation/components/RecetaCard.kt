package com.unilibre.taller05.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unilibre.taller05.domain.model.Receta

@Composable
fun RecetaCard(
    receta: Receta,
    onClick: () -> Unit,
    onToggleFavorita: (() -> Unit)? = null,
    visible: Boolean = true,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 }),
        exit = fadeOut()
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            onClick = onClick
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(
                        text = receta.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    if (onToggleFavorita != null) {
                        IconButton(onClick = onToggleFavorita) {
                            Icon(
                                if (receta.esFavorita) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorita"
                            )
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row {
                    AssistChip(
                        onClick = {},
                        label = { Text("${receta.tiempoMinutos} min") },
                        leadingIcon = { Icon(Icons.Default.Schedule, null) },
                        colors = AssistChipDefaults.assistChipColors()
                    )
                    Spacer(Modifier.height(0.dp).padding(start = 8.dp))
                    AssistChip(onClick = {}, label = { Text(receta.dificultad) })
                    Spacer(Modifier.height(0.dp).padding(start = 8.dp))
                    AssistChip(onClick = {}, label = { Text("${receta.calorias} kcal") })
                }
            }
        }
    }
}
