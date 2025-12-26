package ar.edu.um.programacion.marcos2504.screens.eventos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.edu.um.programacion.marcos2504.api.Api
import ar.edu.um.programacion.marcos2504.models.Evento
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DetalleEventoScreen(private val eventoId: Long) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        var evento by remember { mutableStateOf<Evento?>(null) }
        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf("") }

        LaunchedEffect(eventoId) {
            isLoading = true
            Api.client.getEvento(eventoId).fold(
                onSuccess = {
                    evento = it
                    println("✅ Evento cargado: ${it.titulo}")
                },
                onFailure = {
                    error = it.message ?: "Error al cargar evento"
                    println("❌ Error: ${it.message}")
                }
            )
            isLoading = false
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Evento") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
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
                                        Api.client.getEvento(eventoId).fold(
                                            onSuccess = { evento = it },
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

                evento != null -> {
                    EventoDetalleContent(
                        evento = evento!!,
                        onSeleccionarAsientos = {
                            navigator.push(SeleccionAsientosScreen(evento!!))
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
fun EventoDetalleContent(
    evento: Evento,
    onSeleccionarAsientos: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen del evento
        evento.imagen?.let { imagenUrl ->
            AsyncImage(
                model = imagenUrl,
                contentDescription = "Imagen de ${evento.titulo}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }

        // Información del evento
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = evento.titulo,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "📅 ",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatFecha(evento.fecha),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Dirección
            evento.direccion?.let { direccion ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "📍 ",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = direccion,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Precio
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "💰 ",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$${evento.precioEntrada}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen
            Text(
                text = "Resumen",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = evento.resumen,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción
            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = evento.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Información de asientos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Capacidad del Evento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "🪑 ${evento.filaAsientos} filas x ${evento.columnaAsientos} columnas",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Total: ${evento.filaAsientos * evento.columnaAsientos} asientos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para seleccionar asientos
            Button(
                onClick = onSeleccionarAsientos,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Seleccionar Asientos",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

fun formatFecha(fechaString: String): String {
    return try {
        val instant = Instant.parse(fechaString)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        "${dateTime.dayOfMonth}/${dateTime.monthNumber}/${dateTime.year} ${dateTime.hour}:${dateTime.minute.toString().padStart(2, '0')}"
    } catch (e: Exception) {
        fechaString
    }
}

