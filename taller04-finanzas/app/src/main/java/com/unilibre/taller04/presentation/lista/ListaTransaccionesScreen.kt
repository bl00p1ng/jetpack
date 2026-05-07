package com.unilibre.taller04.presentation.lista

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.taller04.domain.model.Transaccion
import com.unilibre.taller04.presentation.components.TransaccionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTransaccionesScreen(
    onBack: () -> Unit,
    viewModel: ListaTransaccionesViewModel = hiltViewModel()
) {
    val transacciones by viewModel.transacciones.collectAsStateWithLifecycle()
    var pendiente by remember { mutableStateOf<Transaccion?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todas las transacciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (transacciones.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Sin transacciones",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transacciones, key = { it.id }) { tx ->
                    val state = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart || value == SwipeToDismissBoxValue.StartToEnd) {
                                pendiente = tx
                                false
                            } else false
                        }
                    )
                    SwipeToDismissBox(
                        state = state,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    ) {
                        TransaccionItem(transaccion = tx, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }

    pendiente?.let { tx ->
        AlertDialog(
            onDismissRequest = { pendiente = null },
            title = { Text("Eliminar transacción") },
            text = { Text("¿Seguro que deseas eliminar \"${tx.descripcion}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(tx)
                    pendiente = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { pendiente = null }) { Text("Cancelar") }
            }
        )
    }
}
