package ar.edu.um.programacion.marcos2504.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import ar.edu.um.programacion.marcos2504.screens.compras.MisComprasScreen
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object ComprasTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.ShoppingBag)
            
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Mis Compras",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        MisComprasScreen()
    }
}