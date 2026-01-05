# 📱 Documentación del Sistema de Ventas de Eventos - EventosApp

## 📋 Tabla de Contenidos
1. [Arquitectura General](#arquitectura-general)
2. [Componentes del Frontend](#componentes-del-frontend)
3. [Modelos de Datos](#modelos-de-datos)
4. [Flujo de Ventas](#flujo-de-ventas)
5. [Pantallas Implementadas](#pantallas-implementadas)
6. [API Client](#api-client)
7. [Configuración](#configuración)

---

## 🏗️ Arquitectura General

El sistema está implementado usando **Kotlin Multiplatform** con **Jetpack Compose** para la UI.

### Stack Tecnológico
- **Framework**: Kotlin Multiplatform + Compose Multiplatform
- **Navegación**: Voyager (Screen + Tab Navigation)
- **HTTP Client**: Ktor Client
- **Serialización**: kotlinx.serialization
- **Imágenes**: Coil3
- **Backend**: Spring Boot + Redis

### Estructura del Proyecto
```
composeApp/src/
├── commonMain/kotlin/ar/edu/um/programacion/marcos2504/
│   ├── api/
│   │   └── ApiClient.kt           # Cliente HTTP para comunicación con backend
│   ├── config/
│   │   └── AppConfig.kt           # Configuración (expect/actual)
│   ├── models/
│   │   ├── AuthModels.kt          # Modelos de autenticación
│   │   └── EventoModels.kt        # Modelos de eventos, asientos y ventas
│   ├── navigation/
│   │   ├── BottomBarScreen.kt     # Barra de navegación inferior
│   │   ├── EventosTab.kt          # Tab de eventos
│   │   ├── ComprasTab.kt          # Tab de compras
│   │   └── PerfilTab.kt           # Tab de perfil
│   └── screens/
│       ├── auth/
│       │   ├── LoginScreen.kt     # Pantalla de login
│       │   └── RegisterScreen.kt  # Pantalla de registro
│       ├── eventos/
│       │   ├── DetalleEventoScreen.kt        # Detalle del evento
│       │   ├── SeleccionAsientosScreen.kt    # Selección de asientos
│       │   └── ConfirmacionVentaScreen.kt    # Confirmación y pago
│       └── compras/
│           └── MisComprasScreen.kt           # Historial de compras
└── androidMain/kotlin/ar/edu/um/programacion/marcos2504/
    └── config/
        └── AppConfig.android.kt   # Configuración específica de Android
```

---

## 🧩 Componentes del Frontend

### 1. **ApiClient.kt** - Cliente HTTP
**Ubicación**: `api/ApiClient.kt`

**Propósito**: Centraliza todas las llamadas HTTP al backend.

**Características**:
- Configuración de Ktor Client con JSON serialization
- Manejo de tokens JWT
- Logging de requests/responses
- Manejo de errores con `Result<T>`

**Métodos principales**:
```kotlin
// Autenticación
suspend fun login(request: LoginRequest): Result<AuthResponse>
suspend fun register(request: RegisterRequest): Result<Unit>
suspend fun getCurrentUser(): Result<User>

// Eventos
suspend fun getEventos(): Result<List<Evento>>
suspend fun getEvento(id: Long): Result<Evento>
suspend fun getAsientosEvento(eventoId: Long): Result<List<AsientoRedis>>

// Ventas
suspend fun bloquearAsientos(request: BloquearAsientosRequest): Result<BloqueoResponse>
suspend fun realizarVenta(request: RealizarVentaRequest): Result<Venta>
suspend fun getMisCompras(): Result<List<Venta>>
```

**Uso del Token**:
```kotlin
// El token se guarda automáticamente después del login
setAuthToken(token: String)

// Se incluye automáticamente en todas las peticiones autenticadas
bearerAuth(authToken ?: throw IllegalStateException("No auth token"))
```

### 2. **AppConfig** - Configuración de URLs
**Ubicación**: `config/AppConfig.kt` (expect) y `androidMain/config/AppConfig.android.kt` (actual)

**Configuración Android**:
```kotlin
actual object AppConfig {
    actual val BASE_URL: String = "http://10.0.2.2:8080"
    // Para emulador: 10.0.2.2
    // Para dispositivo físico: usar IP de tu PC (ej: 192.168.100.12:8080)
}
```

### 3. **Navegación con Voyager**

#### BottomBarScreen
Pantalla principal con navegación por tabs.

**Tabs disponibles**:
- **EventosTab**: Lista de eventos disponibles
- **ComprasTab**: Historial de compras del usuario
- **PerfilTab**: Perfil y configuración del usuario

#### Navegación entre pantallas
```kotlin
// Desde lista de eventos → detalle
navigator.push(DetalleEventoScreen(eventoId))

// Desde detalle → selección de asientos
navigator.push(SeleccionAsientosScreen(eventoId))

// Desde selección → confirmación de venta
navigator.push(ConfirmacionVentaScreen(eventoId, asientosSeleccionados))

// Volver atrás
navigator.pop()
```

---

## 📊 Modelos de Datos

### AuthModels.kt

#### LoginRequest
```kotlin
data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
)
```

#### RegisterRequest
```kotlin
data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val email: String
)
```

#### AuthResponse
```kotlin
data class AuthResponse(
    val id_token: String  // Token JWT
)
```

#### User
```kotlin
data class User(
    val id: Long,
    val login: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val imageUrl: String?,
    val activated: Boolean,
    val langKey: String?,
    val authorities: List<String>
)
```

### EventoModels.kt

#### Evento
```kotlin
data class Evento(
    val id: Long,
    val titulo: String,
    val resumen: String,
    val descripcion: String,
    val fecha: String,               // ISO 8601
    val direccion: String?,
    val imagen: String?,
    val filaAsientos: Int,           // Dimensiones de la matriz
    val columnaAsientos: Int,
    val precioEntrada: Double,
    val eventoTipo: EventoTipo?,
    val integrantes: List<Integrante>
)
```

#### AsientoRedis
```kotlin
data class AsientoRedis(
    val fila: Int,                   // 1-based
    val columna: Int,                // 1-based
    val estado: String,              // "Libre", "Bloqueado", "Vendido"
    val persona: String?             // Nombre del comprador si está vendido
)
```

#### Venta
```kotlin
data class Venta(
    val id: Long?,
    val fechaVenta: String?,         // ISO 8601
    val precioVenta: Double?,
    val cantidadAsientos: Int?,
    val resultado: Boolean?,
    val estadoVenta: String?,        // "CONFIRMADA", "RECHAZADA", "ERROR_SINCRONIZACION"
    val descripcion: String?,
    val evento: EventoResumido?
)
```

#### Requests de Venta

**BloquearAsientosRequest**:
```kotlin
data class BloquearAsientosRequest(
    val eventoId: Long,
    val asientos: List<AsientoCoord>  // fila, columna
)
```

**RealizarVentaRequest**:
```kotlin
data class RealizarVentaRequest(
    val eventoId: Long,
    val asientos: List<AsientoVenta>  // fila, columna, persona
)
```

---

## 🔄 Flujo de Ventas

### 1️⃣ Lista de Eventos (EventosTab)

**Funcionalidad**:
- Muestra todos los eventos disponibles
- Carga desde: `GET /api/eventos`
- Al hacer clic → navega a DetalleEventoScreen

**Estados**:
- Loading: Muestra CircularProgressIndicator
- Error: Muestra mensaje + botón "Reintentar"
- Vacío: Muestra "No hay eventos disponibles"
- Éxito: Lista con Cards de eventos

### 2️⃣ Detalle del Evento (DetalleEventoScreen)

**Funcionalidad**:
- Muestra información completa del evento
- Carga desde: `GET /api/eventos/{id}`
- Botón "Ver Asientos" → navega a SeleccionAsientosScreen

**Información mostrada**:
- Imagen del evento
- Título y descripción
- Fecha y ubicación
- Precio de entrada
- Integrantes/presentadores
- Dimensiones de la sala (filas × columnas)

### 3️⃣ Selección de Asientos (SeleccionAsientosScreen)

**Funcionalidad**:
- Muestra matriz interactiva de asientos
- Carga evento: `GET /api/eventos/{id}`
- Carga estados: `GET /api/asientos/evento/{id}`
- Permite seleccionar hasta 4 asientos libres
- Botón "Continuar" → navega a ConfirmacionVentaScreen

**Lógica de Matriz de Asientos**:

```kotlin
fun crearMatrizAsientos(
    filas: Int, 
    columnas: Int, 
    asientosRedis: List<AsientoRedis>
): List<List<AsientoUI>>
```

**Proceso**:
1. Crear matriz vacía de `filas × columnas`
2. Por defecto todos los asientos → `LIBRE`
3. Para cada AsientoRedis en la lista:
   - Ubicar en matriz usando `fila-1` y `columna-1` (convertir de 1-based a 0-based)
   - Actualizar estado según Redis:
     - `"Bloqueado"` → `BLOQUEADO`
     - `"Vendido"` o `"Ocupado"` → `VENDIDO`
     - `"Libre"` → `LIBRE` (expiró el bloqueo)

**Estados de Asientos**:
```kotlin
enum class EstadoAsiento {
    LIBRE,      // Verde, clickeable
    BLOQUEADO,  // Amarillo, no clickeable
    VENDIDO     // Rojo, no clickeable
}
```

**UI de Asientos**:
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(columnas),
    // ...
) {
    items(asientosAplanados) { asiento ->
        AsientoBox(
            asiento = asiento,
            isSelected = asiento in asientosSeleccionados,
            onClick = { /* seleccionar/deseleccionar */ }
        )
    }
}
```

**Colores**:
- 🟢 Verde (`Color(0xFF4CAF50)`): LIBRE
- 🟡 Amarillo (`Color(0xFFFFEB3B)`): BLOQUEADO
- 🔴 Rojo (`Color(0xFFF44336)`): VENDIDO
- 🔵 Azul (`Color(0xFF2196F3)`): SELECCIONADO

**Validaciones**:
- Máximo 4 asientos seleccionados
- Solo se pueden seleccionar asientos LIBRES
- Botón "Continuar" habilitado solo si hay selección

### 4️⃣ Confirmación de Venta (ConfirmacionVentaScreen)

**Funcionalidad**:
- Muestra resumen de la compra
- Inputs para nombre/apellido de cada asiento
- Realiza proceso de bloqueo + venta
- Muestra resultado

**Proceso de Compra**:

```kotlin
// 1. BLOQUEAR ASIENTOS
val bloqueoRequest = BloquearAsientosRequest(
    eventoId = eventoId,
    asientos = asientosSeleccionados.map { 
        AsientoCoord(it.fila, it.columna) 
    }
)

Api.client.bloquearAsientos(bloqueoRequest).fold(
    onSuccess = { /* continuar con venta */ },
    onFailure = { /* mostrar error */ }
)

// 2. REALIZAR VENTA (solo si bloqueo fue exitoso)
val ventaRequest = RealizarVentaRequest(
    eventoId = eventoId,
    asientos = asientosConNombres.map { (asiento, nombre) ->
        AsientoVenta(
            fila = asiento.fila,
            columna = asiento.columna,
            persona = nombre
        )
    }
)

Api.client.realizarVenta(ventaRequest).fold(
    onSuccess = { 
        // Mostrar mensaje de éxito
        // Navegar de vuelta a eventos
    },
    onFailure = { /* mostrar error */ }
)
```

**Endpoints Backend**:

**Bloquear Asientos**:
```http
POST http://10.0.2.2:8080/api/compras/bloquear
Authorization: Bearer {token}
Content-Type: application/json

{
  "eventoId": 1,
  "asientos": [
    {"fila": 1, "columna": 3},
    {"fila": 1, "columna": 4}
  ]
}
```

**Realizar Venta**:
```http
POST http://10.0.2.2:8080/api/compras/realizar
Authorization: Bearer {token}
Content-Type: application/json

{
  "eventoId": 1,
  "asientos": [
    {"fila": 1, "columna": 3, "persona": "Juan Perez"},
    {"fila": 1, "columna": 4, "persona": "Maria Lopez"}
  ]
}
```

**Validaciones**:
- Todos los campos de nombres deben estar completos
- Se verifica que los asientos estén disponibles
- Se maneja timeout de bloqueo (Redis TTL)

**Estados**:
- `Loading`: Durante bloqueo/venta
- `Success`: Venta confirmada
- `Error`: Asientos no disponibles, error de red, timeout, etc.

### 5️⃣ Mis Compras (ComprasTab)

**Funcionalidad**:
- Muestra historial de compras del usuario
- Carga desde: `GET /api/ventas` (o `/api/ventas/mis-compras`)
- Muestra estado de cada venta

**Estados de Venta**:
- ✅ **CONFIRMADA**: Venta exitosa (verde)
- ❌ **RECHAZADA**: Venta rechazada (rojo)
- ⚠️ **ERROR_SINCRONIZACION**: Error en sincronización con Redis (naranja)

**Información mostrada por venta**:
- Título del evento (o "Venta #ID" si no tiene evento asociado)
- Estado de la venta (con icono y color)
- Fecha del evento
- Descripción del evento
- Cantidad de asientos comprados
- Precio total pagado
- Fecha de compra
- Descripción adicional (si existe)

**Componentes UI**:

```kotlin
@Composable
fun MisComprasScreen() {
    // Carga lista de ventas
    // Estados: Loading, Error, Vacío, Lista
}

@Composable
fun VentaCard(venta: Venta) {
    // Card con información de la venta
    // Color del borde según estado
    // Iconos según estado
}

@Composable
fun InfoRow(label: String, value: String) {
    // Fila con label a la izquierda, value a la derecha
}

fun formatearFecha(fechaISO: String): String {
    // Convierte ISO 8601 a formato legible
    // Ejemplo: "26/12/2025 14:30"
}
```

### Componentes de Perfil

**ProfileHeader**
```kotlin
@Composable
fun ProfileHeader(user: User)
```
- Avatar circular (imagen o icono default)
- Nombre completo en negrita (headlineMedium)
- Username con @ en gris (si tiene nombre completo)

**SectionTitle**
```kotlin
@Composable
fun SectionTitle(title: String)
```
- Título de sección en color primario
- Typography: titleLarge, Bold

**InfoCard**
```kotlin
@Composable
fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
)
```
- Card con icono + label + valor
- Icono coloreado con color primario
- Label pequeño en gris
- Valor destacado en negrita
- ValueColor opcional para casos especiales (ej: estado activo/inactivo)

---

## 📱 Pantallas Implementadas

### Pantallas de Autenticación

#### LoginScreen
- Inputs: username, password
- Checkbox: "Recordarme"
- Botón: "Iniciar Sesión"
- Link: "¿No tienes cuenta? Regístrate"
- Al login exitoso → guarda token y navega a BottomBarScreen

#### RegisterScreen
- Inputs: username, email, firstName, lastName, password
- Botón: "Registrarse"
- Link: "¿Ya tienes cuenta? Inicia sesión"
- Al registro exitoso → navega a LoginScreen

### Pantallas de Eventos

#### EventosTab (Lista)
- Lista scrolleable de eventos
- Card por evento con:
  - Imagen
  - Título
  - Fecha formateada
  - Precio
  - Resumen/descripción
- Click → DetalleEventoScreen

#### DetalleEventoScreen
- Imagen destacada
- Información completa
- Lista de integrantes
- Botón "Ver Asientos Disponibles"

#### SeleccionAsientosScreen
- Título del evento
- Matriz interactiva de asientos
- Leyenda de colores
- Contador de seleccionados (X/4)
- Precio total calculado
- Botón "Continuar" (fixed bottom)

#### ConfirmacionVentaScreen
- Información del evento
- Lista de asientos seleccionados
- Input de nombre por cada asiento
- Precio total
- Botón "Confirmar Compra"
- Dialog de loading durante proceso
- Mensajes de éxito/error

### Pantallas de Compras

#### ComprasTab (MisComprasScreen)
- Lista de compras realizadas
- Cards con información de cada venta
- Estados visuales (colores e iconos)
- Pull-to-refresh implícito (LaunchedEffect)
- Estados: Loading, Error, Vacío, Lista
- Título dinámico: muestra título del evento o "Venta #ID" si no tiene evento
- **Importante**: El contenido respeta el padding de las barras de navegación (top y bottom)

### Pantallas de Perfil

#### PerfilTab (PerfilScreen)
- Muestra información completa del usuario autenticado
- Carga desde: `GET /api/account`
- Estados: Loading, Error, Datos del usuario
- **Componentes**:
  - **ProfileHeader**: Avatar (imagen o icono default), nombre completo y username
  - **Sección Información Personal**:
    - Nombre de usuario
    - Nombre completo (si existe)
    - Email (si existe)
  - **Sección Cuenta**:
    - ID de usuario
    - Estado de activación (con color verde/rojo)
    - Idioma (si existe)
    - Roles/autoridades (si existen)
  - **Botón Cerrar Sesión**: 
    - Color rojo (MaterialTheme.colorScheme.error)
    - Muestra dialog de confirmación
    - Al confirmar: limpia token y redirige a LoginScreen

**Características**:
- Diseño limpio con Cards para cada dato
- Iconos descriptivos para cada campo
- Foto de perfil circular (AsyncImage con Coil)
- Scroll vertical para contenido largo
- Dialog de confirmación antes de cerrar sesión
- Manejo de datos opcionales (firstName, lastName, email, etc.)

---

## 🔧 Configuración

### URLs del Backend

**Emulador Android**:
```kotlin
actual val BASE_URL: String = "http://10.0.2.2:8080"
```

**Dispositivo Físico**:
```kotlin
actual val BASE_URL: String = "http://192.168.100.12:8080"
```
⚠️ Cambiar `192.168.100.12` por la IP de tu computadora en la red local.

### Endpoints Disponibles

#### Autenticación
- `POST /api/authenticate` - Login
- `POST /api/register` - Registro
- `GET /api/account` - Info usuario actual (perfil)

#### Eventos
- `GET /api/eventos` - Lista de eventos
- `GET /api/eventos/{id}` - Detalle de evento
- `GET /api/asientos/evento/{id}` - Estados de asientos (Redis)

#### Ventas
- `POST /api/compras/bloquear` - Bloquear asientos (60s TTL en Redis)
- `POST /api/compras/realizar` - Confirmar venta
- `GET /api/ventas` - Historial de compras del usuario

### Dependencias Principales

```kotlin
// build.gradle.kts (composeApp)
dependencies {
    // Compose Multiplatform
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    
    // Voyager (navegación)
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:1.0.0")
    
    // Ktor (HTTP client)
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")
    
    // Serialización
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Coil (imágenes)
    implementation("io.coil-kt.coil3:coil-compose:3.0.0")
    implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.0")
    
    // DateTime
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
}
```

---

## 🎨 Componentes UI Reutilizables

### AsientoBox
Representa un asiento individual en la matriz.

**Props**:
- `asiento: AsientoUI` - Datos del asiento
- `isSelected: Boolean` - Si está seleccionado
- `onClick: () -> Unit` - Callback al hacer clic

**Comportamiento**:
- Clickeable solo si estado = LIBRE
- Cambia color según estado + selección
- Muestra fila/columna

### VentaCard
Card para mostrar información de una venta.

**Props**:
- `venta: Venta` - Datos de la venta

**Características**:
- Borde coloreado según estado
- Icono según estado
- Información organizada con InfoRow
- Divider para separar secciones

### InfoRow
Fila con label y valor alineados.

**Props**:
- `label: String` - Etiqueta (izquierda)
- `value: String` - Valor (derecha)

---

## 🐛 Debugging

### Logs en consola
El código incluye prints estratégicos:

```kotlin
println("✅ Evento cargado: ${it.titulo}")
println("❌ Error: ${it.message}")
println("🌐 [API] GET $baseUrl/api/eventos")
println("📦 [API] Response: ${response.size} eventos")
println("🔒 [API] POST $baseUrl/api/compras/bloquear")
println("💰 [API] POST $baseUrl/api/compras/realizar")
```

### Verificar token
```kotlin
// En ApiClient
println("Token: ${authToken?.take(20)}...")
```

### Verificar matriz de asientos
```kotlin
// En SeleccionAsientosScreen
matriz.forEachIndexed { fila, columnas ->
    columnas.forEach { asiento ->
        println("[$fila,$columna] = ${asiento.estado}")
    }
}
```

---

## ✅ Testing

### Probar endpoints con curl

```bash
# Login
TOKEN=$(curl -s -X POST http://10.0.2.2:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}' | jq -r '.id_token')

# Eventos
curl -s -X GET http://10.0.2.2:8080/api/eventos \
  -H "Authorization: Bearer $TOKEN" | jq .

# Asientos
curl -s -X GET http://10.0.2.2:8080/api/asientos/evento/1 \
  -H "Authorization: Bearer $TOKEN" | jq .

# Bloquear
curl -s -X POST http://10.0.2.2:8080/api/compras/bloquear \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"eventoId":1,"asientos":[{"fila":1,"columna":3}]}' | jq .

# Venta
curl -s -X POST http://10.0.2.2:8080/api/compras/realizar \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"eventoId":1,"asientos":[{"fila":1,"columna":3,"persona":"Juan Perez"}]}' | jq .

# Mis compras
curl -s -X GET http://10.0.2.2:8080/api/ventas \
  -H "Authorization: Bearer $TOKEN" | jq .
```

---

## 📝 Notas Importantes

### Conversión de índices (1-based ↔ 0-based)

**Backend/Redis**: Los asientos se indexan desde 1 (1-based)
- Fila 1, Columna 1 es el primer asiento

**Kotlin/UI**: Las listas se indexan desde 0 (0-based)
- `matriz[0][0]` es el primer asiento

**Conversión al crear matriz**:
```kotlin
matriz[asiento.fila - 1][asiento.columna - 1] = asientoUI
```

**Conversión al enviar al backend**:
```kotlin
// Los valores ya vienen en 1-based desde AsientoUI
AsientoCoord(fila = asiento.fila, columna = asiento.columna)
```

### TTL de Bloqueos
- Los asientos bloqueados expiran en **60 segundos**
- Redis maneja automáticamente la liberación
- Al recargar, los bloqueados expirados aparecen como LIBRE

### Manejo de Errores
- Siempre usar `Result<T>` en ApiClient
- Capturar excepciones en llamadas API
- Mostrar mensajes user-friendly
- Proveer botón "Reintentar" en caso de error

### Estados de Carga
- Mostrar `CircularProgressIndicator` durante operaciones async
- Deshabilitar botones durante loading
- Usar `LaunchedEffect` para cargas iniciales
- Usar `rememberCoroutineScope()` para acciones de usuario

---

## 🚀 Próximos Pasos Sugeridos

1. **Implementar refresh manual** en listas (SwipeRefresh)
2. **Agregar filtros** en lista de eventos (por fecha, tipo, precio)
3. **Búsqueda** de eventos
4. **Notificaciones** cuando expire un bloqueo
5. **QR Code** para las entradas compradas
6. **Compartir** eventos
7. **Favoritos** (guardar eventos para ver después)
8. **Modo oscuro** (Dark theme)
9. **Caché local** de eventos (offline-first)
10. **Animaciones** en transiciones de pantallas

---

## 📞 Soporte

Para problemas o dudas:
- Revisar logs en Logcat (Android Studio)
- Verificar conectividad de red
- Asegurar que el backend esté corriendo
- Verificar configuración de URL en `AppConfig.android.kt`

---

**Última actualización**: Enero 2026
**Versión de la documentación**: 1.0

