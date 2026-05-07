package com.unilibre.taller04.presentation.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.unilibre.taller04.domain.model.Categoria

@Composable
fun ChipCategoria(
    categoria: Categoria,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        modifier = modifier,
        selected = seleccionada,
        onClick = onClick,
        label = { Text(categoria.display) },
        colors = FilterChipDefaults.filterChipColors()
    )
}
