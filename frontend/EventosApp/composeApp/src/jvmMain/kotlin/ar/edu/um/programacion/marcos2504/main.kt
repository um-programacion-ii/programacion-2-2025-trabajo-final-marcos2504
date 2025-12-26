package ar.edu.um.programacion.marcos2504

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "EventosApp",
    ) {
        App()
    }
}