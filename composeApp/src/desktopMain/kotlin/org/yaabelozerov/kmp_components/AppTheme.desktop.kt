package org.yaabelozerov.kmp_components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun AppThemeConfiguration(
  darkTheme: Boolean,
  lightColors: ColorScheme,
  darkColors: ColorScheme,
  typography: Typography,
  content: @Composable () -> Unit
) {
  MaterialTheme(colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors, typography = typography, content = content)
}