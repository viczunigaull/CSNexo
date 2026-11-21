package com.example.csnexo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de la marca usada de forma consistente en las pantallas.
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF98222E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD9),
    onPrimaryContainer = Color(0xFF410008),
    secondary = Color(0xFF775653),
    tertiary = Color(0xFF745B2F)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB3B3),
    onPrimary = Color(0xFF5B0D17),
    primaryContainer = Color(0xFF7A0F1D),
    onPrimaryContainer = Color(0xFFFFDAD9),
    secondary = Color(0xFFE6BDB9),
    tertiary = Color(0xFFE3C28E)
)

@Composable
fun CSNexoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
