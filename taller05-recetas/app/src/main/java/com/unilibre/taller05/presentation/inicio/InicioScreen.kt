package com.unilibre.taller05.presentation.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import com.unilibre.taller05.presentation.components.RecetaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    onCamera: () -> Unit,
    onRecetaClick: (String) -> Unit,
    vm: InicioViewModel = hiltViewModel()
) {
    val favoritas by vm.favoritas.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Recetas IA") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onCamera,
                icon = { Icon(Icons.Default.PhotoCamera, null) },
                text = { Text("Escanear") }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            if (favoritas.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Aun no tienes recetas favoritas.", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Pulsa Escanear para detectar ingredientes con la camara y generar recetas con IA.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoritas, key = { it.id }) { receta ->
                        RecetaCard(
                            receta = receta,
                            onClick = { onRecetaClick(receta.nombre) }
                        )
                    }
                }
            }
        }
    }
}
