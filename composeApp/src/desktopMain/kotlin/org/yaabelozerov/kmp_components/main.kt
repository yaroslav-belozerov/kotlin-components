package org.yaabelozerov.kmp_components

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MultiplatformComponents",
    ) {
        App()
    }
}