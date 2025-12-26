package ar.edu.um.programacion.marcos2504.api

import ar.edu.um.programacion.marcos2504.config.AppConfig
import ar.edu.um.programacion.marcos2504.models.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Cliente HTTP para comunicarse con el backend EventosApp
 */
class ApiClient(
    private val baseUrl: String = AppConfig.BASE_URL
) {
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true
                encodeDefaults = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }
    
    // Token JWT almacenado después del login
    private var authToken: String? = null
    
    /**
     * Guarda el token de autenticación
     */
    fun setAuthToken(token: String) {
        authToken = token
    }
    
    /**
     * Limpia el token (logout)
     */
    fun clearAuthToken() {
        authToken = null
    }
    
    /**
     * Login de usuario
     */
    suspend fun login(request: LoginRequest): Result<AuthResponse> {
        return try {
            val response: AuthResponse = client.post("$baseUrl/api/authenticate") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
            
            // Guardar token automáticamente
            setAuthToken(response.id_token)
            
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Registro de nuevo usuario
     */
    suspend fun register(request: RegisterRequest): Result<Unit> {
        return try {
            client.post("$baseUrl/api/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtener información del usuario actual
     */
    suspend fun getCurrentUser(): Result<User> {
        return try {
            val response: User = client.get("$baseUrl/api/account") {
                bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtener lista de eventos
     */
    suspend fun getEventos(): Result<List<Evento>> {
        return try {
            val response: List<Evento> = client.get("$baseUrl/api/eventos") {
                bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtener detalle de un evento
     */
    suspend fun getEvento(id: Long): Result<Evento> {
        return try {
            val response: Evento = client.get("$baseUrl/api/eventos/$id") {
                bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtener asientos de un evento desde Redis
     */
    suspend fun getAsientosEvento(eventoId: Long): Result<List<AsientoRedis>> {
        return try {
            println("🌐 [API] GET $baseUrl/api/asientos/evento/$eventoId")
            val response: List<AsientoRedis> = client.get("$baseUrl/api/asientos/evento/$eventoId") {
                bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
            }.body()
            println("📦 [API] Response: ${response.size} asientos")
            Result.success(response)
        } catch (e: Exception) {
            println("💥 [API] Error: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Bloquear asientos
     */
    suspend fun bloquearAsientos(request: BloquearAsientosRequest): Result<BloqueoResponse> {
        return try {
            println("🔒 [API] POST $baseUrl/api/compras/bloquear")
            println("   Request: $request")
            val response: BloqueoResponse = client.post("$baseUrl/api/compras/bloquear") {
                contentType(ContentType.Application.Json)
                bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
                setBody(request)
            }.body()
            println("✅ [API] Bloqueo response: $response")
            Result.success(response)
        } catch (e: Exception) {
            println("❌ [API] Error al bloquear: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Realizar venta
     */
    suspend fun realizarVenta(request: RealizarVentaRequest): Result<Venta> {
        return try {
            println("💰 [API] POST $baseUrl/api/compras/realizar")
            println("   Request: $request")
            val response: Venta = client.post("$baseUrl/api/compras/realizar") {
                contentType(ContentType.Application.Json)
                bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
                setBody(request)
            }.body()
            println("✅ [API] Venta response: $response")
            Result.success(response)
        } catch (e: Exception) {
            println("❌ [API] Error al realizar venta: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    /**
     * Obtener mis compras
     */
    suspend fun getMisCompras(): Result<List<Venta>> {
        return try {
            val response: List<Venta> = client.get("$baseUrl/api/ventas") {
                bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
            }.body()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

/**
 * Instancia global del API client
 */
object Api {
    val client = ApiClient()
}
