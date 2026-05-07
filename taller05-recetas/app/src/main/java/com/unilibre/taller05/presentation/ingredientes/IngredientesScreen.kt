package com.unilibre.taller05.presentation.ingredientes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.taller05.presentation.components.ChipIngrediente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientesScreen(
    onBack: () -> Unit,
    onGenerar: () -> Unit,
    vm: IngredientesViewModel = hiltViewModel()
) {
    val ingredientes by vm.ingredientes.collectAsStateWithLifecycle()
    var nuevo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ingredientes detectados") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                if (ingredientes.isEmpty()) "No se detectaron ingredientes. Agregalos manualmente."
                else "Edita la lista, elimina los que no apliquen y agrega los que falten.",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(ingredientes, key = { it }) { nombre ->
                    ChipIngrediente(nombre = nombre, onRemove = { vm.eliminar(nombre) })
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = nuevo,
                    onValueChange = { nuevo = it },
                    label = { Text("Nuevo ingrediente") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                IconButton(onClick = {
                    vm.agregar(nuevo)
                    nuevo = ""
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }

            Button(
                onClick = onGenerar,
                enabled = ingredientes.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Generar recetas con IA")
            }
        }
    }
}
