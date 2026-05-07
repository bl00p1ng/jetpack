package com.unilibre.taller05.presentation.recetas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unilibre.taller05.data.ai.ErrorTipo
import com.unilibre.taller05.presentation.components.LoadingDots
import com.unilibre.taller05.presentation.components.RecetaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasIAScreen(
    onBack: () -> Unit,
    onRecetaClick: (String) -> Unit,
    vm: RecetasIAViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recetas IA") },
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
            state.errorTipo?.let { tipo ->
                ErrorCard(
                    tipo = tipo,
                    onReintentar = { vm.iniciarStream() }
                )
            }

            AnimatedVisibility(
                visible = state.streaming && state.recetas.isEmpty() && state.errorTipo == null,
                enter = fadeIn(), exit = fadeOut()
            ) {
                CocinandoCard()
            }

            AnimatedVisibility(
                visible = state.recetas.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.recetas, key = { it.nombre }) { receta ->
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

@Composable
private fun CocinandoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocalDining,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        "Cocinando ideas",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    LoadingDots()
                }
                Text(
                    "La IA esta combinando tus ingredientes para sugerir 3 recetas...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private data class ErrorVisual(
    val icono: ImageVector,
    val titulo: String,
    val mensaje: String,
    val sugerencia: String? = null,
    val mostrarReintentar: Boolean = true
)

private fun ErrorTipo.toVisual(): ErrorVisual = when (this) {
    ErrorTipo.MODELO_SATURADO -> ErrorVisual(
        icono = Icons.Filled.HourglassEmpty,
        titulo = "El modelo esta saturado",
        mensaje = "Gemini tiene mucha demanda en este momento.",
        sugerencia = "Esperate unos segundos y vuelve a intentarlo."
    )
    ErrorTipo.LIMITE_USO -> ErrorVisual(
        icono = Icons.Filled.HourglassEmpty,
        titulo = "Limite de uso alcanzado",
        mensaje = "Se llego al limite de solicitudes para esta API key.",
        sugerencia = "Intenta mas tarde o revisa tu cuota en Google AI Studio."
    )
    ErrorTipo.API_KEY_INVALIDA -> ErrorVisual(
        icono = Icons.Filled.Key,
        titulo = "Clave de API invalida",
        mensaje = "La GEMINI_API_KEY configurada no es valida o no tiene permisos.",
        sugerencia = "Genera una nueva en aistudio.google.com/app/apikey y actualiza local.properties.",
        mostrarReintentar = false
    )
    ErrorTipo.SIN_API_KEY -> ErrorVisual(
        icono = Icons.Filled.Key,
        titulo = "Falta la API key",
        mensaje = "No configuraste GEMINI_API_KEY en local.properties.",
        sugerencia = "Agregala en local.properties y vuelve a compilar la app.",
        mostrarReintentar = false
    )
    ErrorTipo.SIN_INGREDIENTES -> ErrorVisual(
        icono = Icons.Outlined.ErrorOutline,
        titulo = "Sin ingredientes",
        mensaje = "No hay ingredientes para procesar.",
        sugerencia = "Vuelve atras y agrega al menos uno.",
        mostrarReintentar = false
    )
    ErrorTipo.SIN_CONEXION -> ErrorVisual(
        icono = Icons.Filled.WifiOff,
        titulo = "Sin conexion",
        mensaje = "No pudimos contactar al servicio de Gemini.",
        sugerencia = "Verifica tu conexion a internet."
    )
    ErrorTipo.BLOQUEADO_SEGURIDAD -> ErrorVisual(
        icono = Icons.Filled.Shield,
        titulo = "Respuesta bloqueada",
        mensaje = "La IA filtro la respuesta por politicas de seguridad.",
        sugerencia = "Intenta con otros ingredientes."
    )
    ErrorTipo.DESCONOCIDO -> ErrorVisual(
        icono = Icons.Filled.CloudOff,
        titulo = "Algo salio mal",
        mensaje = "No pudimos generar las recetas en este momento.",
        sugerencia = "Vuelve a intentarlo en unos segundos."
    )
}

@Composable
private fun ErrorCard(tipo: ErrorTipo, onReintentar: () -> Unit) {
    val visual = tipo.toVisual()
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = visual.icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = visual.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            Text(
                text = visual.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            visual.sugerencia?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
            if (visual.mostrarReintentar) {
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = onReintentar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text("Reintentar")
                }
            }
        }
    }
}
