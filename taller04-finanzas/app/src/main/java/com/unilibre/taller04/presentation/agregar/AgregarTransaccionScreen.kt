package com.unilibre.taller04.presentation.agregar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.taller04.domain.model.Categoria
import com.unilibre.taller04.domain.model.TipoTransaccion
import com.unilibre.taller04.presentation.components.ChipCategoria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarTransaccionScreen(
    onBack: () -> Unit,
    viewModel: AgregarTransaccionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.guardado) {
        if (state.guardado) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva transacción") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.descripcion,
                onValueChange = viewModel::onDescripcionChange,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = state.monto,
                onValueChange = viewModel::onMontoChange,
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Text("Tipo", style = MaterialTheme.typography.titleMedium)
            val tipos = TipoTransaccion.entries
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                tipos.forEachIndexed { index, t ->
                    SegmentedButton(
                        selected = state.tipo == t,
                        onClick = { viewModel.onTipoChange(t) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = tipos.size)
                    ) {
                        Text(if (t == TipoTransaccion.INGRESO) "Ingreso" else "Gasto")
                    }
                }
            }

            Text("Categoría", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(Categoria.entries) { cat ->
                    ChipCategoria(
                        categoria = cat,
                        seleccionada = state.categoria == cat,
                        onClick = { viewModel.onCategoriaChange(cat) }
                    )
                }
            }

            state.error?.let { msg ->
                Text(msg, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::guardar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
