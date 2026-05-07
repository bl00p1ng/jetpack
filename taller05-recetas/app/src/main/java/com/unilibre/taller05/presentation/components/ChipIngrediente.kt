package com.unilibre.taller05.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ChipIngrediente(
    nombre: String,
    onRemove: () -> Unit
) {
    InputChip(
        selected = false,
        onClick = onRemove,
        label = { Text(nombre) },
        trailingIcon = {
            Icon(Icons.Default.Close, contentDescription = "Eliminar")
        }
    )
}
