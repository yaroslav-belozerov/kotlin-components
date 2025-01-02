package org.yaabelozerov.kmp_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CustomButton(
  onClick: () -> Unit,
  text: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: ImageVector? = null
) = CustomButtonSkeleton(
  onClick = onClick,
  colors = ButtonDefaults.buttonColors(),
  text = text,
  modifier = modifier,
  enabled = enabled,
  icon = icon
)

@Composable
fun CustomOutlinedButton(
  onClick: () -> Unit,
  text: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: ImageVector? = null
) = CustomButtonSkeleton(
  onClick = onClick,
  colors = ButtonDefaults.outlinedButtonColors(),
  text = text,
  modifier = modifier,
  enabled = enabled,
  icon = icon,
  borderStroke = ButtonDefaults.outlinedButtonBorder(enabled)
)

@Composable
fun CustomTextButton(
  onClick: () -> Unit,
  text: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: ImageVector? = null
) = CustomButtonSkeleton(
  onClick = onClick,
  colors = ButtonDefaults.outlinedButtonColors(),
  text = text,
  modifier = modifier,
  enabled = enabled,
  icon = icon
)

@Composable
private fun CustomButtonSkeleton(
  onClick: () -> Unit,
  colors: ButtonColors,
  text: String,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  icon: ImageVector? = null,
  borderStroke: BorderStroke? = null
) = OutlinedButton(
  onClick = onClick,
  modifier = modifier.bouncyClickable(),
  colors = colors,
  border = borderStroke,
  enabled = enabled,
  content = {
    var lastText by remember { mutableStateOf(text) }
    var lastIcon by remember { mutableStateOf(icon) }
    var isAnimating by remember { mutableStateOf(false) }
    var isStartup by rememberSaveable { mutableStateOf(true) }
    if (isStartup) LaunchedEffect(text, icon) {
      isStartup = true
      isAnimating = true
      delay(100)
      lastText = text
      lastIcon = icon
      isAnimating = false
    }
    AnimatedVisibility(
      !isAnimating,
      enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
      exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        lastIcon?.let {
          Icon(
            imageVector = it, contentDescription = null, modifier = Modifier.padding(end = 4.dp)
          )
        }
        Text(lastText)
      }
    }
  },
  shape = MaterialTheme.shapes.medium
)
