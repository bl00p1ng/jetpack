package com.unilibre.taller01.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.unilibre.taller01.R
import com.unilibre.taller01.ui.theme.AppTheme

/**
 * Actividad de extensión del Taller 01: pantalla de presentación personal.
 *
 * Demuestra: Image (painterResource), Column, Row, Icon, Modifier
 * (padding, fillMax, size, clip), Material3 typography y colorScheme.
 */
@Composable
fun PresentacionPersonalScreen(
    nombre: String = "Andrés López",
    descripcion: String = "Estudiante de Ingeniería de Sistemas en la Universidad Libre. " +
        "Apasionado por el desarrollo móvil y las arquitecturas limpias.",
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar_placeholder),
                contentDescription = "Avatar de $nombre",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = nombre,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            FilaIconosSociales()
        }
    }
}

@Composable
private fun FilaIconosSociales(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconoSocial(icon = Icons.Filled.Email, descripcion = "Email")
        IconoSocial(icon = Icons.Filled.Phone, descripcion = "Teléfono")
        IconoSocial(icon = Icons.Filled.Share, descripcion = "Compartir")
    }
}

@Composable
private fun IconoSocial(
    icon: ImageVector,
    descripcion: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.size(56.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = descripcion,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(14.dp),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PresentacionPersonalScreenPreview() {
    AppTheme {
        PresentacionPersonalScreen()
    }
}
