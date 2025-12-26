package ar.edu.um.programacion.marcos2504.models

import kotlinx.serialization.Serializable

/**
 * Request para login de usuario
 */
@Serializable
data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
)

/**
 * Request para registro de nuevo usuario
 */
@Serializable
data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)

/**
 * Response de autenticación (JWT token)
 */
@Serializable
data class AuthResponse(
    val id_token: String
)

/**
 * Usuario autenticado
 */
@Serializable
data class User(
    val id: Long,
    val login: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val imageUrl: String? = null,
    val activated: Boolean = false,
    val langKey: String? = null,
    val authorities: List<String> = emptyList()
)
