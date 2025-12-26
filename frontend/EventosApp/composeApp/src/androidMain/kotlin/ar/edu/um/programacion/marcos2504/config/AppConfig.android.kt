package ar.edu.um.programacion.marcos2504.config

/**
 * Configuración para Android
 *
 * Para Android Emulator: usa 10.0.2.2 para acceder a localhost del host
 * Para dispositivo físico: cambia a la IP de tu computadora en la red local
 * Ejemplo: "http://192.168.100.12:8080"
 */
actual object AppConfig {
    // Para emulador Android usa 10.0.2.2
    // Para dispositivo físico usa la IP de tu computadora
    actual val BASE_URL: String = "http://192.168.100.12:8080"
}

