package org.yaabelozerov.kmp_components

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun AppThemeConfiguration(
  darkTheme: Boolean,
  lightColors: ColorScheme,
  darkColors: ColorScheme,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> darkColors
    else -> lightColors
  }

  MaterialTheme(
    colorScheme = colorScheme, typography = makeTypography(), content = content
  )
}