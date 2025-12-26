package ar.edu.um.programacion.marcos2504.screens.eventos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.edu.um.programacion.marcos2504.api.Api
import ar.edu.um.programacion.marcos2504.models.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class AsientoSeleccionado(
    val fila: Int,
    val columna: Int
)

class ConfirmacionVentaScreen(
    private val evento: Evento,
    private val asientosSeleccionados: List<Pair<Int, Int>> // (fila, columna) en índices base-0
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        // Estado para los nombres de cada asiento
        var nombresAsientos by remember {
            mutableStateOf(
                asientosSeleccionados.associateWith { "" }.toMutableMap()
            )
        }

        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }
        var success by remember { mutableStateOf(false) }

        val precioTotal = evento.precioEntrada * asientosSeleccionados.size
        val todosLosNombresCompletos = nombresAsientos.values.all { it.isNotBlank() }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Confirmar Compra") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título del evento
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = evento.titulo,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = evento.fecha,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Sección de asientos
                item {
                    Text(
                        text = "Datos de los asientos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Lista de asientos con inputs
                items(asientosSeleccionados.toList()) { asiento ->
                    AsientoConNombre(
                        fila = asiento.first,
                        columna = asiento.second,
                        nombre = nombresAsientos[asiento] ?: "",
                        onNombreChange = { nuevoNombre ->
                            nombresAsientos = nombresAsientos.toMutableMap().apply {
                                this[asiento] = nuevoNombre
                            }
                        }
                    )
                }

                // Resumen de precio
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Cantidad de asientos:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${asientosSeleccionados.size}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Precio por entrada:",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "$${evento.precioEntrada}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "TOTAL:",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "$${precioTotal}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Mensaje de error
                if (error != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = "❌ Error: $error",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                // Mensaje de éxito
                if (success) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "✅ ¡Compra realizada con éxito!",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        // Volver a la lista de eventos
                                        navigator.popUntilRoot()
                                    }
                                ) {
                                    Text("Volver a eventos")
                                }
                            }
                        }
                    }
                }

                // Botón de confirmación
                item {
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                error = null

                                println("🔄 Iniciando proceso de compra...")
                                println("   Evento: ${evento.titulo} (ID: ${evento.id})")
                                println("   Asientos: ${asientosSeleccionados.size}")

                                // Paso 1: Bloquear asientos
                                val asientosCoord = asientosSeleccionados.map { (fila, columna) ->
                                    AsientoCoord(
                                        fila = fila + 1, // Convertir a base-1
                                        columna = columna + 1
                                    )
                                }

                                val bloquearRequest = BloquearAsientosRequest(
                                    eventoId = evento.id,
                                    asientos = asientosCoord
                                )

                                println("📝 Request bloquear: $bloquearRequest")

                                Api.client.bloquearAsientos(bloquearRequest).fold(
                                    onSuccess = { respuestaBloqueo ->
                                        println("✅ Asientos bloqueados correctamente: $respuestaBloqueo")

                                        // Paso 2: Realizar venta
                                        val asientosVenta = asientosSeleccionados.map { (fila, columna) ->
                                            AsientoVenta(
                                                fila = fila + 1, // Convertir a base-1
                                                columna = columna + 1,
                                                persona = nombresAsientos[Pair(fila, columna)] ?: ""
                                            )
                                        }

                                        val ventaRequest = RealizarVentaRequest(
                                            eventoId = evento.id,
                                            asientos = asientosVenta
                                        )

                                        println("📝 Request venta: $ventaRequest")

                                        scope.launch {
                                            Api.client.realizarVenta(ventaRequest).fold(
                                                onSuccess = { venta ->
                                                    println("✅ Venta realizada exitosamente!")
                                                    println("   ID: ${venta.id}")
                                                    println("   Estado: ${venta.estadoVenta}")
                                                    println("   Descripción: ${venta.descripcion}")
                                                    println("   Precio total: ${venta.precioVenta}")
                                                    isLoading = false
                                                    success = true
                                                },
                                                onFailure = { e ->
                                                    println("❌ Error al realizar venta: ${e.message}")
                                                    e.printStackTrace()
                                                    isLoading = false
                                                    error = e.message ?: "Error al realizar la venta"
                                                }
                                            )
                                        }
                                    },
                                    onFailure = { e ->
                                        println("❌ Error al bloquear asientos: ${e.message}")
                                        e.printStackTrace()
                                        isLoading = false
                                        error = e.message ?: "Error al bloquear asientos"
                                    }
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isLoading && todosLosNombresCompletos && !success,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Procesando...")
                        } else {
                            Text(
                                text = "Confirmar Compra",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    if (!todosLosNombresCompletos && !success) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚠️ Completa todos los nombres para continuar",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                // Espacio al final
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun AsientoConNombre(
    fila: Int,
    columna: Int,
    nombre: String,
    onNombreChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎫 Asiento: Fila ${fila + 1}, Columna ${columna + 1}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre y Apellido") },
                placeholder = { Text("Ej: Juan Pérez") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

