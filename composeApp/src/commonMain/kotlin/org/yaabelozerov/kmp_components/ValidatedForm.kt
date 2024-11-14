package org.yaabelozerov.kmp_components

import kotlinx.collections.immutable.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
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
    value: TextFieldValue,
    onValidate: (TextFieldValue) -> ValidationResult
) {
  var result by remember { mutableStateOf<ValidationResult>(ValidationResult.Initial) }
  println("result: $result")
  OutlinedTextField(
      value = value,
      onValueChange = {
        result = onValidate(it)
      },
      supportingText = {
        when (result) {
          is ValidationResult.Valid -> {}
          is ValidationResult.Invalid -> {
            Text(text = (result as ValidationResult.Invalid).message)
          }
          is ValidationResult.Initial -> {}
        }
      },
      singleLine = true,
      isError = result is ValidationResult.Invalid)
}

@Composable
fun ColumnScope.ValidatedForm(
    validators: List<Validator<String>>,
    onSubmit: (Map<ValidatorKey, String>) -> Unit
) {
  var state by remember {
    mutableStateOf(
        validators
            .associateWith {
              Pair<TextFieldValue, ValidationResult>(TextFieldValue(), ValidationResult.Initial)
            }.toImmutableMap())
  }
  state.entries.forEach { entry ->
    ValidatedField(
        entry.value.first
    ) {
      val res = entry.key.validator(it.text)
      state = state.plus(entry.key to entry.value.copy(first = it, second = res)).toImmutableMap()
      res
    }
  }
  Button(onClick = {
    onSubmit(state.map { it.key.key to it.value.first.text }.toMap())
  }, enabled = state.all { it.value.second is ValidationResult.Valid }) {
    Text("Submit")
  }
}
