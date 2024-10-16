package com.yaabelozerov.components

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaabelozerov.components.ui.theme.ComponentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ComponentsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Phone Number",
                            fontSize = MaterialTheme.typography.displaySmall.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                        PhoneField(prefix = "+7",
                            mask = "000 000 00-00",
                            maskNumber = '0',
                            onContinue = {
                                Log.i("phone", it)
                                true  // Phone validation
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun PhoneField(
    prefix: String = "+7",
    mask: String = "000 000 00 00",
    maskNumber: Char = '0',
    onContinue: (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var phone by rememberSaveable { mutableStateOf("") }
    var hasError by rememberSaveable { mutableStateOf(false) }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    OutlinedTextField(value = phone,
        onValueChange = { it ->
            hasError = false
            phone = it.take(mask.count { it == maskNumber })
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            hasError = if (mask.filter { it.isDigit() }.length == phone.length) {
                focusManager.clearFocus()
                !onContinue(prefix + phone)
            } else {
                true
            }
        }),
        visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        modifier = modifier
            .fillMaxWidth()
            .onKeyEvent { it: KeyEvent ->
                if (it.key == Key.Paste) {
                    phone =
                        formatClipboardNumber(clipboardManager.getText()).also { Log.i("str", it) }
                    return@onKeyEvent true
                }
                return@onKeyEvent false
            }
            .focusRequester(focusRequester),
        trailingIcon = {
            if (phone.isNotEmpty()) {
                IconButton(modifier = Modifier.padding(8.dp, 0.dp), onClick = {
                    phone = ""
                    focusRequester.requestFocus()
                }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        leadingIcon = {
            Text(
                text = prefix,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp, 0.dp, 4.dp, 0.dp)
            )
        },
        placeholder = {
            Text(
                text = mask,
                style = LocalTextStyle.current.copy(fontSize = 24.sp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        },
        textStyle = LocalTextStyle.current.copy(fontSize = 24.sp),
        singleLine = true,
        isError = hasError,
        supportingText = {
            val density = LocalDensity.current
            AnimatedVisibility(visible = hasError,
                enter = slideInVertically {
                    with(density) { -10.dp.roundToPx() }
                } + fadeIn(),
                exit = slideOutVertically { with(density) { -10.dp.roundToPx() } } + fadeOut()) {
                Text(text = "Invalid phone number", fontSize = 16.sp)
            }
        })
}

class PhoneVisualTransformation(val mask: String, val maskNumber: Char) : VisualTransformation {

    private val maxLength = mask.count { it == maskNumber }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskNumber) {
                    val nextDigitIndex = mask.indexOf(maskNumber, maskIndex)
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(mask, maskNumber))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PhoneVisualTransformation) return false
        if (mask != other.mask) return false
        if (maskNumber != other.maskNumber) return false
        return true
    }

    override fun hashCode(): Int {
        return mask.hashCode()
    }
}

private class PhoneOffsetMapper(val mask: String, val numberChar: Char) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        var noneDigitCount = 0
        var i = 0
        while (i < offset + noneDigitCount) {
            if (mask[i++] != numberChar) noneDigitCount++
        }
        return offset + noneDigitCount
    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}

fun formatClipboardNumber(annotatedString: AnnotatedString?): String {
    if (annotatedString == null) return ""

    val s = annotatedString.text
    if (s.length > 10) {
        s.replaceFirst("8", "")
        s.replaceFirst("+7", "")
    }
    return s.filter { it.isDigit() }
}