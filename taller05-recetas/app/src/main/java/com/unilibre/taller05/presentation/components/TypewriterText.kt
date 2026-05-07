package com.unilibre.taller05.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun TypewriterText(
    fullText: String,
    modifier: Modifier = Modifier,
    style: TextStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
    charDelayMs: Long = 18L
) {
    var displayed by remember(fullText) { mutableStateOf("") }
    LaunchedEffect(fullText) {
        if (fullText.isEmpty()) {
            displayed = ""
            return@LaunchedEffect
        }
        // If the new full text is a prefix-extension, animate from the current length.
        val start = if (fullText.startsWith(displayed)) displayed.length else 0
        if (start == 0) displayed = ""
        for (i in start..fullText.length) {
            displayed = fullText.substring(0, i)
            delay(charDelayMs)
        }
    }
    Box(modifier = modifier) {
        Text(text = displayed, style = style)
    }
}
