package ar.edu.um.programacion.marcos2504

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform