package ar.edu.um.programacion.marcos2504.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Evento resumido (para lista)
 */
@Serializable
data class EventoResumido(
    val id: Long? = null,
    val titulo: String? = null,
    val resumen: String? = null,
    val descripcion: String? = null,
    val fecha: String? = null,
    val precioEntrada: Double? = null,
    @SerialName("tipo")
    val eventoTipo: EventoTipo? = null
)

/**
 * Evento completo (con todos los detalles)
 */
@Serializable
data class Evento(
    val id: Long,
    val titulo: String,
    val resumen: String,
    val descripcion: String,
    val fecha: String,
    val direccion: String? = null,
    val imagen: String? = null,
    val filaAsientos: Int,
    val columnaAsientos: Int,
    val precioEntrada: Double,
    @SerialName("tipo")
    val eventoTipo: EventoTipo? = null,
    val integrantes: List<Integrante> = emptyList()
)

/**
 * Tipo de evento
 */
@Serializable
data class EventoTipo(
    val id: Long? = null,
    val nombre: String? = null,
    val descripcion: String? = null
)

/**
 * Integrante/presentador del evento
 */
@Serializable
data class Integrante(
    val nombre: String,
    val apellido: String,
    val identificacion: String? = null
)

/**
 * Estado de un asiento desde Redis
 */
@Serializable
data class AsientoRedis(
    val fila: Int,
    val columna: Int,
    val estado: String, // "Libre", "Bloqueado", "Vendido"
    val persona: String? = null
)

/**
 * Request para bloquear asientos
 */
@Serializable
data class BloquearAsientosRequest(
    val eventoId: Long,
    val asientos: List<AsientoCoord>
)

/**
 * Coordenadas de un asiento
 */
@Serializable
data class AsientoCoord(
    val fila: Int,
    val columna: Int
)

/**
 * Respuesta del endpoint de bloqueo de asientos
 */
@Serializable
data class BloqueoResponse(
    val mensaje: String? = null,
    val bloqueados: Int? = null,
    val exito: Boolean? = null
)

/**
 * Request para realizar venta
 */
@Serializable
data class RealizarVentaRequest(
    val eventoId: Long,
    val asientos: List<AsientoVenta>
)

/**
 * Asiento con datos de comprador
 */
@Serializable
data class AsientoVenta(
    val fila: Int,
    val columna: Int,
    val persona: String
)

/**
 * Venta registrada
 */
@Serializable
data class Venta(
    val id: Long? = null,
    val fechaVenta: String? = null,
    val precioVenta: Double? = null,
    val cantidadAsientos: Int? = null,
    val resultado: Boolean? = null,
    val estadoVenta: String? = null,
    val descripcion: String? = null,
    val evento: EventoResumido? = null
)
