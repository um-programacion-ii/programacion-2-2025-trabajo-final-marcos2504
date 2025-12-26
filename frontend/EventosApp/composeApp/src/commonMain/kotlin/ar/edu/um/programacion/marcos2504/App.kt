package ar.edu.um.programacion.marcos2504

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ar.edu.um.programacion.marcos2504.screens.auth.LoginScreen
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(LoginScreen())
    }
}