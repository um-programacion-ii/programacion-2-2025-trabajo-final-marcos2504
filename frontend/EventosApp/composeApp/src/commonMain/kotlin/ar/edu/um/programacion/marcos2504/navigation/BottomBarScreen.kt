package ar.edu.um.programacion.marcos2504.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator

class BottomBarScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Obtenemos el navigator del padre (LoginScreen)
        val parentNavigator = LocalNavigator.currentOrThrow

        TabNavigator(
            tab = EventosTab,
            tabDisposable = {
                TabDisposable(
                    navigator = it,
                    tabs = listOf(
                        EventosTab,
                        ComprasTab,
                        PerfilTab
                    )
                )
            }
        ) { tabNavigator ->
            // Proveemos el parentNavigator a todos los tabs
            CompositionLocalProvider(LocalNavigator provides parentNavigator) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = tabNavigator.current.options.title
                                )
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar {
                            TabNavigationItem(EventosTab)
                            TabNavigationItem(ComprasTab)
                            TabNavigationItem(PerfilTab)
                        }
                    }
                ) { paddingValues ->
                    CurrentTab()
                }
            }
        }
    }

    @Composable
    private fun androidx.compose.foundation.layout.RowScope.TabNavigationItem(tab: cafe.adriel.voyager.navigator.tab.Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current.key == tab.key,
            label = {
                Text(text = tab.options.title)
            },
            icon = {
                tab.options.icon?.let { icon ->
                    Icon(
                        painter = icon,
                        contentDescription = tab.options.title
                    )
                }
            },
            onClick = {
                tabNavigator.current = tab
            }
        )
    }
}

