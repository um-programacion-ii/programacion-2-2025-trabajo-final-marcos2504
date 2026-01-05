package ar.edu.um.programacion.marcos2504.screens.compras

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.edu.um.programacion.marcos2504.api.Api
import ar.edu.um.programacion.marcos2504.models.Venta
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun MisComprasScreen() {
    val scope = rememberCoroutineScope()
    var ventas by remember { mutableStateOf<List<Venta>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Cargar compras al iniciar
    LaunchedEffect(Unit) {
        isLoading = true
        error = ""
        Api.client.getMisCompras().fold(
            onSuccess = {
                ventas = it
                println("✅ Compras cargadas: ${it.size}")
            },
            onFailure = {
                error = it.message ?: "Error al cargar compras"
                println("❌ Error al cargar compras: ${it.message}")
                it.printStackTrace()
            }
        )
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            error.isNotEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Error al cargar compras",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                error = ""
                                Api.client.getMisCompras().fold(
                                    onSuccess = { ventas = it },
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

            ventas.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Sin compras",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No tienes compras registradas",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ventas) { venta ->
                        VentaCard(venta)
                    }
                }
            }
        }
    }
}

@Composable
fun VentaCard(venta: Venta) {
    val estadoColor = when (venta.estadoVenta) {
        "CONFIRMADA" -> Color(0xFF4CAF50)
        "RECHAZADA" -> Color(0xFFF44336)
        "ERROR_SINCRONIZACION" -> Color(0xFFFF9800)
        else -> MaterialTheme.colorScheme.secondary
    }

    val estadoIcon = when (venta.estadoVenta) {
        "CONFIRMADA" -> Icons.Default.CheckCircle
        "RECHAZADA" -> Icons.Default.Error
        "ERROR_SINCRONIZACION" -> Icons.Default.Warning
        else -> Icons.Default.Warning
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(2.dp, estadoColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título del evento
            Text(
                text = venta.evento?.titulo ?: "Venta #${venta.id ?: 0}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Estado de la venta
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = estadoIcon,
                    contentDescription = venta.estadoVenta,
                    tint = estadoColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = venta.estadoVenta ?: "DESCONOCIDO",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = estadoColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Información del evento
            if (venta.evento?.fecha != null) {
                InfoRow(
                    label = "Fecha del evento:",
                    value = formatearFecha(venta.evento.fecha)
                )
            }

            if (venta.evento?.descripcion != null) {
                InfoRow(
                    label = "Descripción:",
                    value = venta.evento.descripcion
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Detalles de la compra
            InfoRow(
                label = "Cantidad de asientos:",
                value = "${venta.cantidadAsientos ?: 0}"
            )

            InfoRow(
                label = "Precio total:",
                value = "$${String.format("%.2f", venta.precioVenta ?: 0.0)}"
            )

            if (venta.fechaVenta != null) {
                InfoRow(
                    label = "Fecha de compra:",
                    value = formatearFecha(venta.fechaVenta)
                )
            }

            if (venta.descripcion != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = venta.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun formatearFecha(fechaISO: String): String {
    return try {
        val instant = Instant.parse(fechaISO)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        "${dateTime.dayOfMonth}/${dateTime.monthNumber}/${dateTime.year} ${dateTime.hour}:${
            dateTime.minute.toString().padStart(2, '0')
        }"
    } catch (e: Exception) {
        fechaISO
    }
}

