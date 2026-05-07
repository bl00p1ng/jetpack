package com.unilibre.taller02.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unilibre.taller02.ui.lista.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTareaScreen(
    tareaId: String,
    viewModel: TareasViewModel,
    onBack: () -> Unit
) {
    val tarea = viewModel.obtenerPorId(tareaId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (tarea == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Tarea no encontrada")
                Button(onClick = onBack) { Text("Volver") }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Estado: " + if (tarea.completada) "Completada" else "Pendiente",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = {
                    viewModel.alternarCompletada(tarea.id)
                    onBack()
                }) {
                    Text(
                        text = if (tarea.completada)
                            "Marcar como pendiente y volver"
                        else
                            "Marcar como completada y volver"
                    )
                }
            }
        }
    }
}
