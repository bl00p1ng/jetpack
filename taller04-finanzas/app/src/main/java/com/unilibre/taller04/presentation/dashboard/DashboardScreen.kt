package com.unilibre.taller04.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.taller04.presentation.components.GraficoBarras
import com.unilibre.taller04.presentation.components.TarjetaResumen
import com.unilibre.taller04.presentation.components.TransaccionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAgregar: () -> Unit,
    onVerTodas: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finanzas") },
                actions = {
                    IconButton(onClick = onVerTodas) {
                        Icon(Icons.Default.List, contentDescription = "Ver todas")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    TarjetaResumen(
                        ingresos = state.resumen.totalIngresos,
                        gastos = state.resumen.totalGastos
                    )
                }
                item {
                    GraficoBarras(puntos = state.serieMensual)
                }
                item {
                    Text(
                        "Movimientos recientes",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
                if (state.recientes.isEmpty() && !state.cargando) {
                    item {
                        Text(
                            "No hay transacciones aún. Pulsa + para agregar la primera.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(state.recientes, key = { it.id }) { tx ->
                        TransaccionItem(transaccion = tx)
                    }
                }
            }
        }
    }
}
