package org.yaabelozerov.kmp_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
) =
    OutlinedTextField(
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
                enter = slideInVertically {
                    with(density) { -10.dp.roundToPx() }
                } + fadeIn() + expandVertically(),
                exit = slideOutVertically { with(density) { -10.dp.roundToPx() } } + fadeOut() + shrinkVertically(), content = {
                    Text(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp), text = errorText, fontSize = 16.sp)
                })
        },
        textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
        singleLine = true,
        isError = isError,
        shape = MaterialTheme.shapes.medium)

@Composable
fun TextLine(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
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
) {
    var isFocused by remember { mutableStateOf(false) }
    val surfaceBright = MaterialTheme.colorScheme.surfaceBright
    val primary = MaterialTheme.colorScheme.primary
    val animProgress by animateFloatAsState(if (isFocused) 1f else 0f, animationSpec = tween(200, easing = EaseOutCubic))
    val animColor = primary.copy(animProgress).compositeOver(surfaceBright)
    BasicTextField(
        modifier = modifier.padding(vertical = 12.dp).height(48.dp).onFocusChanged {
            isFocused = it.isFocused
        },
        textStyle = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onBackground.copy(0f)),
        value = value,
        onValueChange = { it: TextFieldValue -> onValueChange(it) },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.drawWithContent {
                drawRect(Brush.linearGradient(0f to surfaceBright, 0.85f to surfaceBright, 1f to Color.Transparent), topLeft = Offset(0f, size.height - 1.dp.toPx()), size = Size(size.width, 2.dp.toPx()))
                drawRect(Brush.linearGradient(0f to animColor, animProgress to animColor, 1f to Color.Transparent), topLeft = Offset(0f, size.height - 1.dp.toPx()), size = Size(size.width, 3.dp.toPx()))
                drawContent()
            }) {
                innerTextField()
                Row(Modifier.fillMaxSize()) {
                    value.text.forEach {
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(null) {
                            visible = true
                        }
                        AnimatedVisibility(visible, enter = slideInVertically(animationSpec = tween(50, easing = EaseInOut)) { -it } + fadeIn(animationSpec = tween(100, 25, EaseInOut)) + expandVertically(expandFrom = Alignment.CenterVertically)) {
                            Text(it.toString(), style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                }
                if (value.text.isEmpty()) {
                    Text("Placeholder", style = MaterialTheme.typography.headlineSmall.copy(MaterialTheme.colorScheme.onBackground.copy(0.3f)))
                }
            }
        }
    )
}