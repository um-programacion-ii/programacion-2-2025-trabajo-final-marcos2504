package ar.edu.um.programacion.marcos2504.screens.eventos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.edu.um.programacion.marcos2504.api.Api
import ar.edu.um.programacion.marcos2504.models.AsientoRedis
import ar.edu.um.programacion.marcos2504.models.AsientoSeleccionado
import ar.edu.um.programacion.marcos2504.models.Evento
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class SeleccionAsientosScreen(private val evento: Evento) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        var asientosRedis by remember { mutableStateOf<List<AsientoRedis>>(emptyList()) }
        var asientosSeleccionados by remember { mutableStateOf<Set<Pair<Int, Int>>>(emptySet()) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf("") }

        // Cargar estado de asientos desde Redis
        LaunchedEffect(evento.id) {
            println("🔄 [INICIO] LaunchedEffect iniciado para evento ${evento.id}")
            println("   Dimensiones: ${evento.filaAsientos} filas x ${evento.columnaAsientos} columnas")

            val result = Api.client.getAsientosEvento(evento.id)

            result.fold(
                onSuccess = { asientos ->
                    println("✅ [SUCCESS] Asientos cargados desde Redis: ${asientos.size}")
                    asientos.forEach { asiento ->
                        println("   - Fila ${asiento.fila}, Columna ${asiento.columna}: ${asiento.estado}")
                    }
                    if (asientos.isEmpty()) {
                        println("✨ Todos los asientos están LIBRES (no hay datos en Redis)")
                    }
                    asientosRedis = asientos
                },
                onFailure = { e ->
                    println("❌ [FAILURE] Error al cargar asientos: ${e.message}")
                    e.printStackTrace()
                    error = e.message ?: "Error al cargar asientos"
                }
            )

            isLoading = false
            println("🏁 [LOADING=FALSE] LaunchedEffect finalizado")
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(evento.titulo) },
                    navigationIcon = {
                        IconButton(onClick = {
                            // Retroceder estado de sesión antes de salir
                            scope.launch {
                                val result = Api.client.retrocederEstadoSesion()
                                result.fold(
                                    onSuccess = { sesion ->
                                        println("⬅️ Estado retrocedido a: ${sesion.estadoSesion}")
                                        navigator.pop()
                                    },
                                    onFailure = { error ->
                                        // Si falla, navegar igual
                                        println("⚠️ Error al retroceder estado: ${error.message}")
                                        navigator.pop()
                                    }
                                )
                            }
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                )
            }
        ) { paddingValues ->
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                error.isNotEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Error: $error")
                            Button(
                                onClick = {
                                    scope.launch {
                                        isLoading = true
                                        error = ""
                                        Api.client.getAsientosEvento(evento.id).fold(
                                            onSuccess = { asientosRedis = it },
                                            onFailure = { error = it.message ?: "Error" }
                                        )
                                        isLoading = false
                                    }
                                },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(16.dp)
                    ) {
                        // Leyenda
                        item {
                            Text(
                                text = "Selecciona tus asientos",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            LeyendaAsientos()

                            Spacer(modifier = Modifier.height(16.dp))

                        }

                        // Mapa de asientos
                        items(evento.filaAsientos) { fila ->
                            FilaAsientos(
                                fila = fila,
                                columnas = evento.columnaAsientos,
                                asientosRedis = asientosRedis,
                                asientosSeleccionados = asientosSeleccionados,
                                onAsientoClick = { f, c ->
                                    val asiento = Pair(f, c)
                                    val estadoRedis = getEstadoAsiento(f, c, asientosRedis)

                                    println("👆 Click en asiento Fila $f, Columna $c - Estado: $estadoRedis")

                                    // Solo permitir click en asientos libres
                                    if (estadoRedis == EstadoAsiento.LIBRE) {
                                        asientosSeleccionados = if (asiento in asientosSeleccionados) {
                                            // Deseleccionar
                                            println("   ➖ Deseleccionando asiento")
                                            asientosSeleccionados - asiento
                                        } else {
                                            // Seleccionar si no supera el límite
                                            if (asientosSeleccionados.size < 4) {
                                                println("   ➕ Seleccionando asiento (total: ${asientosSeleccionados.size + 1})")
                                                asientosSeleccionados + asiento
                                            } else {
                                                // Ya hay 4 asientos seleccionados
                                                println("   ⚠️ Límite alcanzado (4 asientos)")
                                                asientosSeleccionados
                                            }
                                        }
                                    } else {
                                        println("   Asiento no disponible")
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Botón de continuar
                        if (asientosSeleccionados.isNotEmpty()) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Asientos seleccionados: ${asientosSeleccionados.size}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Total: $${evento.precioEntrada * asientosSeleccionados.size}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(
                                            onClick = {
                                                println("Continuar con ${asientosSeleccionados.size} asientos")

                                                // Convertir asientos seleccionados al formato requerido para la sesión
                                                val asientosParaSesion = asientosSeleccionados.mapIndexed { index, (fila, columna) ->
                                                    AsientoSeleccionado(
                                                        id = index.toLong(),
                                                        fila = (fila + 1).toString(), // Convertir a 1-based
                                                        numero = columna + 1 // Convertir a 1-based
                                                    )
                                                }

                                                scope.launch {
                                                    // Convertir a JSON para el endpoint
                                                    val asientosJson = Json.encodeToString(asientosParaSesion)
                                                    val result = Api.client.bloquearAsientosSesion(
                                                        asientosJson = asientosJson,
                                                        cantidad = asientosSeleccionados.size
                                                    )

                                                    result.fold(
                                                        onSuccess = { sesion ->
                                                            println("✅ Asientos bloqueados. Estado: ${sesion.estadoSesion}")
                                                            navigator.push(
                                                                ConfirmacionVentaScreen(
                                                                    evento = evento,
                                                                    asientosSeleccionados = asientosSeleccionados.toList()
                                                                )
                                                            )
                                                        },
                                                        onFailure = { error ->
                                                            // Si falla, navegar igual
                                                            println("⚠️ Error al actualizar sesión: ${error.message}")
                                                            navigator.push(
                                                                ConfirmacionVentaScreen(
                                                                    evento = evento,
                                                                    asientosSeleccionados = asientosSeleccionados.toList()
                                                                )
                                                            )
                                                        }
                                                    )
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            enabled = asientosSeleccionados.size in 1..4
                                        ) {
                                            Text("Continuar")
                                        }
                                        if (asientosSeleccionados.size > 4) {
                                            Text(
                                                text = "⚠️ Máximo 4 asientos por compra",
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodySmall,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }
                                }
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
    }
}

@Composable
fun LeyendaAsientos() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        LeyendaItem("🟢", "Libre")
        LeyendaItem("🔵", "Seleccionado")
        LeyendaItem("🟡", "Bloqueado")
        LeyendaItem("🔴", "Vendido")
    }
}

@Composable
fun LeyendaItem(emoji: String, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = emoji, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = texto, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun FilaAsientos(
    fila: Int,
    columnas: Int,
    asientosRedis: List<AsientoRedis>,
    asientosSeleccionados: Set<Pair<Int, Int>>,
    onAsientoClick: (Int, Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Número de fila
        Text(
            text = "${fila + 1}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        // Asientos de la fila
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (columna in 0 until columnas) {
                val estado = if (Pair(fila, columna) in asientosSeleccionados) {
                    EstadoAsiento.SELECCIONADO
                } else {
                    getEstadoAsiento(fila, columna, asientosRedis)
                }

                AsientoItem(
                    fila = fila,
                    columna = columna,
                    estado = estado,
                    onClick = { onAsientoClick(fila, columna) }
                )
            }
        }
    }
}

@Composable
fun AsientoItem(
    fila: Int,
    columna: Int,
    estado: EstadoAsiento,
    onClick: () -> Unit
) {
    // Log para depuración de asientos bloqueados/vendidos
    if (estado == EstadoAsiento.BLOQUEADO || estado == EstadoAsiento.VENDIDO) {
        println("🎨 Renderizando asiento ${estado.name}: UI muestra Fila ${fila + 1}, Columna ${columna + 1} (índices internos: fila=$fila, col=$columna)")
    }

    val (backgroundColor, borderColor, clickable) = when (estado) {
        EstadoAsiento.LIBRE -> Triple(
            Color(0xFF4CAF50).copy(alpha = 0.2f),
            Color(0xFF4CAF50),
            true
        )
        EstadoAsiento.SELECCIONADO -> Triple(
            Color(0xFF2196F3),
            Color(0xFF1976D2),
            true
        )
        EstadoAsiento.BLOQUEADO -> Triple(
            Color(0xFFFF9800).copy(alpha = 0.2f),
            Color(0xFFFF9800),
            false
        )
        EstadoAsiento.VENDIDO -> Triple(
            Color(0xFFF44336).copy(alpha = 0.2f),
            Color(0xFFF44336),
            false
        )
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .clickable(enabled = clickable, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${columna + 1}",
            style = MaterialTheme.typography.bodySmall,
            color = if (estado == EstadoAsiento.SELECCIONADO) Color.White else Color.Black
        )
    }
}

enum class EstadoAsiento {
    LIBRE,
    SELECCIONADO,
    BLOQUEADO,
    VENDIDO
}

fun getEstadoAsiento(fila: Int, columna: Int, asientosRedis: List<AsientoRedis>): EstadoAsiento {
    // fila y columna vienen en base-0 desde la UI
    val filaBackend = fila + 1  // Convertir a base-1 para buscar en Redis
    val columnaBackend = columna + 1

    val asiento = asientosRedis.find { it.fila == filaBackend && it.columna == columnaBackend }

    if (asiento != null) {
        println("🔍 MATCH! UI(fila=$fila [0-based], col=$columna [0-based]) -> Backend(fila=${asiento.fila} [1-based], col=${asiento.columna} [1-based]) - Estado: ${asiento.estado}")
    }

    return when (asiento?.estado?.lowercase()) {
        "bloqueado" -> EstadoAsiento.BLOQUEADO
        "vendido", "ocupado" -> EstadoAsiento.VENDIDO
        "libre" -> EstadoAsiento.LIBRE
        null -> EstadoAsiento.LIBRE // Si no está en Redis, está libre
        else -> EstadoAsiento.LIBRE
    }
}

