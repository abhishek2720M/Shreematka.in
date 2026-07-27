package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ShreeMatkaColorScheme = darkColorScheme(
  primary = GoldPrimary,
  onPrimary = Color.Black,
  primaryContainer = DarkSurfaceVariant,
  onPrimaryContainer = GoldLight,
  secondary = GoldLight,
  onSecondary = Color.Black,
  background = DarkBackground,
  onBackground = TextPrimary,
  surface = DarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = DarkSurfaceVariant,
  onSurfaceVariant = TextSecondary,
  outline = DarkCardBorder,
  error = AccentRed,
  onError = Color.White
)

@Composable
fun ShreeMatkaTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = ShreeMatkaColorScheme,
    typography = Typography,
    content = content
  )
}

