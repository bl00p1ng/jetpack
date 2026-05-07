package com.unilibre.taller02.ui.lista

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction

@Composable
fun NuevaTareaDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var tocado by remember { mutableStateOf(false) }
    val esError = tocado && texto.trim().isEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva tarea") },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = {
                    texto = it
                    tocado = true
                },
                label = { Text("Titulo") },
                singleLine = true,
                isError = esError,
                supportingText = {
                    if (esError) {
                        Text(
                            text = "El titulo no puede estar vacio",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    tocado = true
                    if (texto.trim().isNotEmpty()) {
                        onConfirm(texto.trim())
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
