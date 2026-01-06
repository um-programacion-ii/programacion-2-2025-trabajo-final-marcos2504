package ar.edu.um.programacion.marcos2504.models

import kotlinx.serialization.Serializable

/**
 * DTO de sesión del usuario
 * Representa el estado del flujo de compra
 */
@Serializable
data class SesionDTO(
    val id: Long? = null,
    val tokenJWT: String = "",
    val fechaInicio: String? = null,
    val activa: Boolean = true,
    val ultimoAcceso: String? = null,
    val eventoSeleccionado: Long? = null,
    val estadoSesion: EstadoSesion? = null,
    val asientosSeleccionados: String? = null,  // JSON string
    val cantidadAsientos: Int? = null
)

/**
 * Estados del flujo de compra
 */
@Serializable
enum class EstadoSesion {
    LISTA_EVENTOS,           // Usuario viendo eventos (inicio)
    EVENTO_SELECCIONADO,     // Seleccionó un evento
    ASIENTOS_BLOQUEADOS,     // Bloqueó asientos
    DATOS_COMPLETOS,         // Completó datos de asistentes
    VENTA_CONFIRMADA,        // Venta procesada
    EXPIRADA                 // Sesión expirada
}

/**
 * Asiento seleccionado para la sesión
 */
@Serializable
data class AsientoSeleccionado(
    val id: Long,
    val fila: String,
    val numero: Int
)