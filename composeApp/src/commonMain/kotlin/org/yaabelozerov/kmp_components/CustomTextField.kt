package org.yaabelozerov.kmp_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun CustomTextField(
  value: String,
  onValueChange: (String) -> Unit,
  enabled: Boolean = true,
  isError: Boolean = false,
  errorText: String = "",
  leadingIcon: (@Composable () -> Unit)? = null,
  trailingIcon: (@Composable () -> Unit)? = null,
  placeholder: (@Composable () -> Unit)? = null,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  modifier: Modifier = Modifier
) = OutlinedTextField(
  value = value,
  onValueChange = onValueChange,
  enabled = enabled,
  keyboardOptions = keyboardOptions,
  keyboardActions = keyboardActions,
  visualTransformation = visualTransformation,
  modifier = modifier,
  placeholder = placeholder,
  leadingIcon = leadingIcon,
  trailingIcon = trailingIcon,
  supportingText = {
    val density = LocalDensity.current
    AnimatedVisibility(visible = isError,
      enter = slideInVertically { with(density) { -10.dp.roundToPx() } } + fadeIn() + expandVertically(),
      exit = slideOutVertically { with(density) { -10.dp.roundToPx() } } + fadeOut() + shrinkVertically(),
      content = {
        Text(
          modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
          text = errorText,
          fontSize = 16.sp
        )
      })
  },
  textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
  singleLine = true,
  isError = isError,
  shape = MaterialTheme.shapes.medium
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TextLine(
  value: TextFieldValue,
  onValueChange: (TextFieldValue) -> Unit,
  enabled: Boolean = true,
  isError: Boolean = false,
  leadingIcon: (@Composable () -> Unit)? = null,
  placeholderString: String = "",
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  modifier: Modifier = Modifier
) {
  var isFocused by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()
  val coroutineScope = rememberCoroutineScope()
  val surfaceBright = MaterialTheme.colorScheme.surfaceBright
  val primary = MaterialTheme.colorScheme.primary
  val error = MaterialTheme.colorScheme.error
  val animProgress by animateFloatAsState(
    if (isFocused) 1f else 0f, animationSpec = tween(200, easing = EaseOutCubic)
  )
  val animColor = primary.copy(animProgress).compositeOver(surfaceBright)
  var textLength by remember { mutableIntStateOf(0) }
  var animateThis by remember { mutableStateOf(false) }
  BasicTextField(enabled = enabled,
    visualTransformation = visualTransformation,
    keyboardOptions = keyboardOptions,
    keyboardActions = keyboardActions,
    modifier = modifier.onFocusChanged { isFocused = it.isFocused },
    textStyle = MaterialTheme.typography.headlineSmall.copy(color = Color.Transparent),
    value = value,
    onValueChange = { it: TextFieldValue ->
      animateThis = it.selection.start == it.text.length
      onValueChange(it)
      coroutineScope.launch { scrollState.scrollTo(textLength) }
    },
    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
    singleLine = true,
    decorationBox = { innerTextField ->
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.drawWithContent {
        drawRect(
          Brush.linearGradient(
            0f to surfaceBright, 0.85f to surfaceBright, 1f to Color.Transparent
          ), topLeft = Offset(0f, size.height + 4.dp.toPx()), size = Size(size.width, 2.dp.toPx())
        )
        drawRect(
          Brush.linearGradient(
            0f to animColor, animProgress to animColor, 1f to Color.Transparent
          ), topLeft = Offset(0f, size.height + 4.dp.toPx()), size = Size(size.width, 3.dp.toPx())
        )
        if (!enabled) {
          drawRect(
            color = surfaceBright.copy(0.5f),
            topLeft = Offset(0f, size.height + 4.dp.toPx()),
            size = Size(size.width, 3.dp.toPx())
          )
        }
        if (isError) {
          drawRect(
            color = if (enabled) error else error.copy(0.5f),
            topLeft = Offset(0f, size.height + 4.dp.toPx()),
            size = Size(size.width, 3.dp.toPx())
          )
        }
        drawContent()
      }.height(48.dp).padding(top = 8.dp)) {
        CompositionLocalProvider(
          LocalTextStyle provides MaterialTheme.typography.headlineSmall
        ) {
          leadingIcon?.invoke()
          Spacer(Modifier.width(8.dp))
          Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.BottomStart) {
            innerTextField()
            Row(Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
              (visualTransformation.filter(AnnotatedString(value.text)).text).forEach { it ->
                var visible by remember { mutableStateOf(!animateThis) }
                LaunchedEffect(null) { visible = true }
                AnimatedVisibility(
                  visible,
                  enter = fadeIn(
                    animationSpec = tween(
                      100,
                      easing = EaseInOut
                    )
                  ) + slideInVertically(
                    initialOffsetY = { it }, animationSpec = tween(200, easing = EaseOut)
                  ),
                ) {
                  Text(
                    it.toString(), color = (if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onBackground).copy(if (enabled) 1f else 0.5f)
                  )
                }
              }
            }
            if (value.text.isEmpty()) {
              Text(
                placeholderString, color = MaterialTheme.colorScheme.onBackground.copy(0.3f)
              )
            }
          }
        }
        if (isFocused && value.text.isNotEmpty()) {
          IconButton(onClick = { onValueChange(TextFieldValue("")) }) {
            Icon(Icons.Default.Clear, null)
          }
        }
      }
    },
    onTextLayout = { res -> textLength = res.size.width })
}
