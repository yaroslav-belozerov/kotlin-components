package org.yaabelozerov.kmp_components

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
expect fun AppThemeConfiguration(
  darkTheme: Boolean,
  lightColors: ColorScheme,
  darkColors: ColorScheme,
  content: @Composable () -> Unit
)

