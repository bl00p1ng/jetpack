package com.unilibre.taller03.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.unilibre.taller03.domain.model.WeatherData

@Composable
fun WeatherContent(data: WeatherData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = data.city,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineMedium
        )

        AsyncImage(
            model = "https://openweathermap.org/img/wn/${data.iconCode}@2x.png",
            contentDescription = data.description,
            modifier = Modifier.size(140.dp).padding(top = 8.dp)
        )

        Text(
            text = "${data.tempC.toInt()}°",
            color = Color.White,
            fontSize = 96.sp,
            fontWeight = FontWeight.Light
        )

        Text(
            text = data.description,
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.titleMedium
        )

        Surface(
            color = Color.White.copy(alpha = 0.15f),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text("${data.humidity}%") },
                    leadingIcon = { Icon(Icons.Default.WaterDrop, contentDescription = null) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White.copy(alpha = 0.10f),
                        labelColor = Color.White,
                        leadingIconContentColor = Color.White
                    )
                )
                AssistChip(
                    onClick = {},
                    label = { Text("${data.windKmh.toInt()} km/h") },
                    leadingIcon = { Icon(Icons.Default.Air, contentDescription = null) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color.White.copy(alpha = 0.10f),
                        labelColor = Color.White,
                        leadingIconContentColor = Color.White
                    )
                )
            }
        }
    }
}
