package com.unilibre.taller05.presentation.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecetaScreen(
    nombre: String,
    onBack: () -> Unit,
    vm: DetalleRecetaViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                },
                actions = {
                    IconButton(onClick = { vm.toggleFavorita() }) {
                        Icon(
                            if (state.receta?.esFavorita == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorita"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when {
                state.cargando -> CircularProgressIndicator()
                state.receta == null -> Text("Receta no encontrada: $nombre")
                else -> {
                    val r = state.receta!!
                    Text(r.nombre, style = MaterialTheme.typography.headlineSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AssistChip(onClick = {}, label = { Text("${r.tiempoMinutos} min") })
                        AssistChip(onClick = {}, label = { Text(r.dificultad) })
                        AssistChip(onClick = {}, label = { Text("${r.calorias} kcal") })
                    }
                    if (r.ingredientesBase.isNotEmpty()) {
                        Card { Column(Modifier.padding(12.dp)) {
                            Text("Ingredientes", style = MaterialTheme.typography.titleMedium)
                            r.ingredientesBase.forEach { Text("- $it") }
                        } }
                    }
                    Card { Column(Modifier.padding(12.dp)) {
                        Text("Pasos", style = MaterialTheme.typography.titleMedium)
                        r.pasos.forEachIndexed { idx, paso ->
                            Row(verticalAlignment = Alignment.Top) {
                                Text("${idx + 1}. ", style = MaterialTheme.typography.bodyMedium)
                                Text(paso, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    } }
                }
            }
        }
    }
}
