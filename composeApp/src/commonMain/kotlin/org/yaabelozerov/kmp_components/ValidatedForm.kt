package org.yaabelozerov.kmp_components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import kotlinx.collections.immutable.toImmutableMap

enum class ValidatorKey {
  Login,
  Password,
}

data class Validator<T>(
    val key: ValidatorKey,
    val validator: (T) -> ValidationResult,
)

sealed class ValidationResult {
  data object Valid : ValidationResult()

  data class Invalid(val message: String) : ValidationResult()

  data object Initial : ValidationResult()
}

@Composable
fun ValidatedField(
    value: String,
    enabled: Boolean,
    onValidate: (String) -> ValidationResult,
    onIme: () -> Unit
) {
  var result by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }
  CustomTextField(
      enabled = enabled,
      modifier = Modifier.fillMaxWidth(),
      value = value,
      onValueChange = { result = onValidate(it) },
      errorText =
          when (result) {
            is ValidationResult.Valid -> ""
            is ValidationResult.Invalid -> (result as ValidationResult.Invalid).message
            is ValidationResult.Initial -> ""
          },
      isError = result is ValidationResult.Invalid,
      keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
      keyboardActions = KeyboardActions(onNext = { onIme() }))
}

@Composable
fun ValidatedForm(
    validators: List<Validator<String>>,
    onSubmit: (Map<ValidatorKey, String>) -> Unit,
    isLoading: Boolean
) {
  val focusManager = LocalFocusManager.current
  var state by remember {
    mutableStateOf(
        validators
            .associateWith { Pair<String, ValidationResult>("", ValidationResult.Initial) }
            .toImmutableMap())
  }
  state.entries.forEachIndexed { index, entry ->
    ValidatedField(
        entry.value.first,
        onValidate = {
          val res = entry.key.validator(it)
          state =
              state.plus(entry.key to entry.value.copy(first = it, second = res)).toImmutableMap()
          res
        },
        onIme = {
          if (index != state.size - 1) focusManager.moveFocus(FocusDirection.Down)
          else {
            onSubmit(state.map { it.key.key to it.value.first }.toMap())
            focusManager.clearFocus()
          }
        },
        enabled = !isLoading)
  }
  val enabled = state.all { it.value.second is ValidationResult.Valid }
  if (!isLoading)
      Button(
          onClick = {
            if (enabled) {
              onSubmit(state.map { it.key.key to it.value.first }.toMap())
              focusManager.clearFocus()
            }
          },
          enabled = enabled) {
            Text("Submit")
          }
  else CircularProgressIndicator()
}
