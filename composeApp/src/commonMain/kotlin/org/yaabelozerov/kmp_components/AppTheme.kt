package org.yaabelozerov.kmp_components

import androidx.compose.runtime.Composable

@Composable
expect fun AppTheme(
  darkTheme: Boolean,
  content: @Composable () -> Unit
)

