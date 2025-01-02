package org.yaabelozerov.kmp_components

import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import multiplatformcomponents.composeapp.generated.resources.Res
import multiplatformcomponents.composeapp.generated.resources.Roboto_Black
import multiplatformcomponents.composeapp.generated.resources.Roboto_Bold
import multiplatformcomponents.composeapp.generated.resources.Roboto_Light
import multiplatformcomponents.composeapp.generated.resources.Roboto_Medium
import multiplatformcomponents.composeapp.generated.resources.Roboto_Regular
import multiplatformcomponents.composeapp.generated.resources.Roboto_Thin
import multiplatformcomponents.composeapp.generated.resources.dela_gothiic_regular
import org.jetbrains.compose.resources.Font


@Composable
fun makeTypography(): Typography {
  val baseline = Typography()
  val bodyFontFamily = FontFamily(
    Font(Res.font.Roboto_Thin, weight = FontWeight.Thin, style = FontStyle.Normal),
    Font(Res.font.Roboto_Light, weight = FontWeight.Light, style = FontStyle.Normal),
    Font(Res.font.Roboto_Regular, weight = FontWeight.Normal, style = FontStyle.Normal),
    Font(Res.font.Roboto_Medium, weight = FontWeight.Medium, style = FontStyle.Normal),
    Font(Res.font.Roboto_Bold, weight = FontWeight.Bold, style = FontStyle.Normal),
    Font(Res.font.Roboto_Black, weight = FontWeight.Black, style = FontStyle.Normal),
  )
  val displayFontFamily = FontFamily(
    Font(Res.font.dela_gothiic_regular, weight = FontWeight.Normal, style = FontStyle.Normal)
  )

  return Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
  )
}

val primaryLight = Color(0xFF515B92)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFDEE0FF)
val onPrimaryContainerLight = Color(0xFF0A154B)
val secondaryLight = Color(0xFF5B5D72)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFDFE1F9)
val onSecondaryContainerLight = Color(0xFF171A2C)
val tertiaryLight = Color(0xFF76546D)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFFD7F2)
val onTertiaryContainerLight = Color(0xFF2D1228)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFFBF8FF)
val onBackgroundLight = Color(0xFF1B1B21)
val surfaceLight = Color(0xFFFBF8FF)
val onSurfaceLight = Color(0xFF1B1B21)
val surfaceVariantLight = Color(0xFFE3E1EC)
val onSurfaceVariantLight = Color(0xFF46464F)
val outlineLight = Color(0xFF767680)
val outlineVariantLight = Color(0xFFC6C5D0)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF303036)
val inverseOnSurfaceLight = Color(0xFFF2F0F7)
val inversePrimaryLight = Color(0xFFBAC3FF)
val surfaceDimLight = Color(0xFFDBD9E0)
val surfaceBrightLight = Color(0xFFFBF8FF)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF5F2FA)
val surfaceContainerLight = Color(0xFFEFEDF4)
val surfaceContainerHighLight = Color(0xFFE9E7EF)
val surfaceContainerHighestLight = Color(0xFFE3E1E9)

val primaryDark = Color(0xFFBAC3FF)
val onPrimaryDark = Color(0xFF222C61)
val primaryContainerDark = Color(0xFF394379)
val onPrimaryContainerDark = Color(0xFFDEE0FF)
val secondaryDark = Color(0xFFC3C5DD)
val onSecondaryDark = Color(0xFF2C2F42)
val secondaryContainerDark = Color(0xFF434659)
val onSecondaryContainerDark = Color(0xFFDFE1F9)
val tertiaryDark = Color(0xFFE5BAD8)
val onTertiaryDark = Color(0xFF44263E)
val tertiaryContainerDark = Color(0xFF5D3C55)
val onTertiaryContainerDark = Color(0xFFFFD7F2)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF121318)
val onBackgroundDark = Color(0xFFE3E1E9)
val surfaceDark = Color(0xFF121318)
val onSurfaceDark = Color(0xFFE3E1E9)
val surfaceVariantDark = Color(0xFF46464F)
val onSurfaceVariantDark = Color(0xFFC6C5D0)
val outlineDark = Color(0xFF90909A)
val outlineVariantDark = Color(0xFF46464F)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE3E1E9)
val inverseOnSurfaceDark = Color(0xFF303036)
val inversePrimaryDark = Color(0xFF515B92)
val surfaceDimDark = Color(0xFF121318)
val surfaceBrightDark = Color(0xFF39393F)
val surfaceContainerLowestDark = Color(0xFF0D0E13)
val surfaceContainerLowDark = Color(0xFF1B1B21)
val surfaceContainerDark = Color(0xFF1F1F25)
val surfaceContainerHighDark = Color(0xFF29292F)
val surfaceContainerHighestDark = Color(0xFF34343A)

val appLightScheme = lightColorScheme(
  primary = primaryLight,
  onPrimary = onPrimaryLight,
  primaryContainer = primaryContainerLight,
  onPrimaryContainer = onPrimaryContainerLight,
  secondary = secondaryLight,
  onSecondary = onSecondaryLight,
  secondaryContainer = secondaryContainerLight,
  onSecondaryContainer = onSecondaryContainerLight,
  tertiary = tertiaryLight,
  onTertiary = onTertiaryLight,
  tertiaryContainer = tertiaryContainerLight,
  onTertiaryContainer = onTertiaryContainerLight,
  error = errorLight,
  onError = onErrorLight,
  errorContainer = errorContainerLight,
  onErrorContainer = onErrorContainerLight,
  background = backgroundLight,
  onBackground = onBackgroundLight,
  surface = surfaceLight,
  onSurface = onSurfaceLight,
  surfaceVariant = surfaceVariantLight,
  onSurfaceVariant = onSurfaceVariantLight,
  outline = outlineLight,
  outlineVariant = outlineVariantLight,
  scrim = scrimLight,
  inverseSurface = inverseSurfaceLight,
  inverseOnSurface = inverseOnSurfaceLight,
  inversePrimary = inversePrimaryLight,
  surfaceDim = surfaceDimLight,
  surfaceBright = surfaceBrightLight,
  surfaceContainerLowest = surfaceContainerLowestLight,
  surfaceContainerLow = surfaceContainerLowLight,
  surfaceContainer = surfaceContainerLight,
  surfaceContainerHigh = surfaceContainerHighLight,
  surfaceContainerHighest = surfaceContainerHighestLight,
)

val appDarkScheme = darkColorScheme(
  primary = primaryDark,
  onPrimary = onPrimaryDark,
  primaryContainer = primaryContainerDark,
  onPrimaryContainer = onPrimaryContainerDark,
  secondary = secondaryDark,
  onSecondary = onSecondaryDark,
  secondaryContainer = secondaryContainerDark,
  onSecondaryContainer = onSecondaryContainerDark,
  tertiary = tertiaryDark,
  onTertiary = onTertiaryDark,
  tertiaryContainer = tertiaryContainerDark,
  onTertiaryContainer = onTertiaryContainerDark,
  error = errorDark,
  onError = onErrorDark,
  errorContainer = errorContainerDark,
  onErrorContainer = onErrorContainerDark,
  background = backgroundDark,
  onBackground = onBackgroundDark,
  surface = surfaceDark,
  onSurface = onSurfaceDark,
  surfaceVariant = surfaceVariantDark,
  onSurfaceVariant = onSurfaceVariantDark,
  outline = outlineDark,
  outlineVariant = outlineVariantDark,
  scrim = scrimDark,
  inverseSurface = inverseSurfaceDark,
  inverseOnSurface = inverseOnSurfaceDark,
  inversePrimary = inversePrimaryDark,
  surfaceDim = surfaceDimDark,
  surfaceBright = surfaceBrightDark,
  surfaceContainerLowest = surfaceContainerLowestDark,
  surfaceContainerLow = surfaceContainerLowDark,
  surfaceContainer = surfaceContainerDark,
  surfaceContainerHigh = surfaceContainerHighDark,
  surfaceContainerHighest = surfaceContainerHighestDark,
)