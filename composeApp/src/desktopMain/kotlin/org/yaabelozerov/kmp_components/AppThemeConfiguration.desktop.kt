package org.yaabelozerov.kmp_components

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun AppThemeConfiguration(
  darkTheme: Boolean,
  lightColors: ColorScheme,
  darkColors: ColorScheme,
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = if (darkTheme) darkColors else lightColors,
    typography = makeTypography(),
    content = content
  )
}