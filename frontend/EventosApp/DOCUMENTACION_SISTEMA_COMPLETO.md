# 📚 Documentación Completa - Sistema de Venta de Entradas EventosApp

## 📋 Tabla de Contenidos
1. [Arquitectura General](#arquitectura-general)
2. [Componentes de Compose Utilizados](#componentes-de-compose-utilizados)
3. [Flujo de Compra de Entradas](#flujo-de-compra-de-entradas)
4. [Sistema de Asientos](#sistema-de-asientos)
5. [Lógica de Estados de Asientos](#lógica-de-estados-de-asientos)
6. [API y Backend](#api-y-backend)
7. [Modelos de Datos](#modelos-de-datos)
8. [Pantallas de la Aplicación](#pantallas-de-la-aplicación)
9. [Debugging y Troubleshooting](#debugging-y-troubleshooting)

---

## 🏗️ Arquitectura General

### Stack Tecnológico

**Frontend (Kotlin Multiplatform)**
- Compose Multiplatform para UI
- Ktor Client para HTTP
- Voyager para navegación
- Kotlinx Serialization para JSON

**Backend (JHipster)**
- Spring Boot
- Redis para estado de asientos
- PostgreSQL para persistencia
- JWT para autenticación

### Estructura del Proyecto

```
EventosApp/
├── composeApp/          # Aplicación móvil (KMP)
│   └── src/
│       └── commonMain/
│           ├── api/                # Cliente HTTP
│           ├── models/             # DTOs/Modelos
│           ├── screens/            # Pantallas UI
│           │   └── eventos/
│           │       ├── DetalleEventoScreen.kt
│           │       ├── SeleccionAsientosScreen.kt
│           │       └── ConfirmacionVentaScreen.kt
│           └── navigation/         # Tabs y navegación
└── server/              # Backend JHipster
    └── src/main/java/
        └── ar/edu/um/programacion/
            ├── domain/             # Entidades JPA
            ├── repository/         # Repos JPA y Redis
            ├── service/            # Lógica de negocio
            └── web/rest/           # Controladores REST
```

---

## 🎨 Componentes de Compose Utilizados

Esta sección explica todos los componentes de Jetpack Compose y Compose Multiplatform que se utilizan en la aplicación.

### 📐 Layout Components (Componentes de Diseño)

#### 1. **Column**
```kotlin
Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    // Elementos se apilan verticalmente
}
```

**Uso:** Organiza elementos hijos en una columna vertical (uno debajo del otro).

**Propiedades clave:**
- `verticalArrangement`: Cómo distribuir el espacio vertical (SpaceBetween, SpaceAround, Center, etc.)
- `horizontalAlignment`: Alineación horizontal de los hijos (Start, Center, End)

**Dónde se usa:**
- Pantalla de detalle del evento (imagen, título, descripción, botón)
- Formularios de entrada de datos
- Tarjetas de eventos

---

#### 2. **Row**
```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Text("Precio:")
    Text("$5000")
}
```

**Uso:** Organiza elementos hijos en una fila horizontal (uno al lado del otro).

**Propiedades clave:**
- `horizontalArrangement`: Distribución horizontal (SpaceBetween, SpaceEvenly, etc.)
- `verticalAlignment`: Alineación vertical (Top, CenterVertically, Bottom)

**Dónde se usa:**
- Mostrar precio + fecha en una línea
- Leyenda de colores de asientos
- Headers con iconos y texto

---

#### 3. **Box**
```kotlin
Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    CircularProgressIndicator()
}
```

**Uso:** Contenedor que apila elementos uno encima de otro. Útil para centrar contenido o superponer elementos.

**Propiedades clave:**
- `contentAlignment`: Dónde posicionar los hijos (Center, TopStart, BottomEnd, etc.)

**Dónde se usa:**
- Centrar el CircularProgressIndicator durante la carga
- Centrar mensajes de error
- Asientos individuales con número centrado

---

#### 4. **LazyColumn**
```kotlin
LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    items(eventos) { evento ->
        EventoCard(evento, onClick = { ... })
    }
    
    item {
        Button(onClick = { ... }) {
            Text("Continuar")
        }
    }
}
```

**Uso:** Lista vertical que solo renderiza elementos visibles (lazy loading/virtualización). Equivalente a RecyclerView en Android Views.

**Métodos:**
- `items(lista) { item -> }`: Renderiza elementos de una lista
- `item { }`: Renderiza un elemento único

**Dónde se usa:**
- Lista de eventos en EventosTab
- Matriz de asientos en SeleccionAsientosScreen
- Formulario de confirmación con múltiples asientos

---

#### 5. **Spacer**
```kotlin
Spacer(modifier = Modifier.height(16.dp))
Spacer(modifier = Modifier.width(8.dp))
```

**Uso:** Agrega espacio vacío entre elementos.

**Dónde se usa:**
- Separar secciones de contenido
- Margen entre texto e imagen
- Espacio entre elementos de un Row o Column

---

### 🎯 Material 3 Components

#### 6. **Scaffold**
```kotlin
@OptIn(ExperimentalMaterial3Api::class)
Scaffold(
    topBar = {
        TopAppBar(
            title = { Text("Eventos") },
            navigationIcon = { ... }
        )
    },
    bottomBar = { ... }
) { paddingValues ->
    // Contenido principal
    Column(modifier = Modifier.padding(paddingValues)) {
        // ...
    }
}
```

**Uso:** Estructura básica de Material Design con TopAppBar, BottomBar, FAB, etc.

**Propiedades:**
- `topBar`: Barra superior (TopAppBar)
- `bottomBar`: Barra inferior (NavigationBar)
- `floatingActionButton`: Botón de acción flotante
- `paddingValues`: Padding automático para evitar que el contenido quede detrás de las barras

**Dónde se usa:**
- Todas las pantallas principales
- Proporciona estructura consistente

---

#### 7. **TopAppBar**
```kotlin
TopAppBar(
    title = { Text("Detalle del Evento") },
    navigationIcon = {
        IconButton(onClick = { navigator.pop() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
        }
    },
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
)
```

**Uso:** Barra de navegación superior con título y acciones.

**Componentes:**
- `title`: Título de la pantalla
- `navigationIcon`: Icono de navegación (generalmente flecha de volver)
- `actions`: Acciones adicionales (menú, búsqueda, etc.)

**Dónde se usa:**
- Todas las pantallas para navegación y contexto

---

#### 8. **Card**
```kotlin
Card(
    modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Contenido de la tarjeta")
    }
}
```

**Uso:** Contenedor con elevación (sombra) que agrupa contenido relacionado.

**Propiedades:**
- `elevation`: Profundidad de la sombra
- `colors`: Colores del fondo
- `shape`: Forma de las esquinas

**Dónde se usa:**
- Tarjetas de eventos en la lista
- Resumen de compra
- Información destacada (precio total, asientos seleccionados)

---

#### 9. **Button**
```kotlin
Button(
    onClick = { /* acción */ },
    enabled = todosLosNombresCompletos,
    modifier = Modifier.fillMaxWidth(),
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
    )
) {
    Text("Confirmar Compra")
}
```

**Uso:** Botón de acción principal (Material Design).

**Propiedades:**
- `onClick`: Acción al hacer click
- `enabled`: Si está habilitado o deshabilitado
- `colors`: Personalización de colores

**Variantes:**
- `OutlinedButton`: Botón con borde
- `TextButton`: Botón sin fondo
- `FilledTonalButton`: Botón con color tonal

**Dónde se usa:**
- "Seleccionar Asientos"
- "Continuar con X asientos"
- "Confirmar Compra"
- "Reintentar" en errores

---

#### 10. **Text**
```kotlin
Text(
    text = "Título del Evento",
    style = MaterialTheme.typography.headlineMedium,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.primary,
    maxLines = 2,
    overflow = TextOverflow.Ellipsis
)
```

**Uso:** Mostrar texto con diferentes estilos.

**Propiedades:**
- `style`: Estilo tipográfico (headlineLarge, bodyMedium, labelSmall, etc.)
- `fontWeight`: Peso de la fuente (Bold, Normal, Light)
- `color`: Color del texto
- `maxLines`: Máximo de líneas
- `overflow`: Qué hacer cuando el texto es muy largo (Ellipsis = "...")

**Estilos comunes:**
- `headlineLarge/Medium/Small`: Títulos principales
- `titleLarge/Medium/Small`: Subtítulos
- `bodyLarge/Medium/Small`: Texto de cuerpo
- `labelLarge/Medium/Small`: Etiquetas pequeñas

**Dónde se usa:**
- Títulos de eventos
- Descripciones
- Precios
- Etiquetas de formularios

---

#### 11. **TextField / OutlinedTextField**
```kotlin
OutlinedTextField(
    value = nombre,
    onValueChange = { nombre = it },
    label = { Text("Nombre y Apellido") },
    placeholder = { Text("Juan Perez") },
    modifier = Modifier.fillMaxWidth(),
    singleLine = true,
    isError = nombre.isBlank(),
    supportingText = {
        if (nombre.isBlank()) {
            Text("Campo requerido")
        }
    }
)
```

**Uso:** Campo de entrada de texto.

**Propiedades:**
- `value`: Valor actual del campo
- `onValueChange`: Callback cuando cambia el texto
- `label`: Etiqueta flotante
- `placeholder`: Texto de ejemplo
- `isError`: Si mostrar estado de error
- `supportingText`: Texto de ayuda o error debajo

**Variantes:**
- `TextField`: Relleno (filled)
- `OutlinedTextField`: Con borde (outlined)

**Dónde se usa:**
- Input de nombre/apellido de compradores
- Formularios de login/registro

---

#### 12. **Icon**
```kotlin
Icon(
    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
    contentDescription = "Volver",
    tint = MaterialTheme.colorScheme.onSurface
)
```

**Uso:** Mostrar iconos de Material Icons.

**Propiedades:**
- `imageVector`: Icono a mostrar
- `contentDescription`: Descripción para accesibilidad
- `tint`: Color del icono

**Iconos comunes:**
- `Icons.AutoMirrored.Filled.ArrowBack`: Flecha volver
- `Icons.Filled.Info`: Información
- `Icons.Filled.Check`: Check/confirmación
- `Icons.Filled.Error`: Error

**Dónde se usa:**
- NavigationIcon en TopAppBar
- Iconos decorativos en tarjetas
- Estados visuales (✅ ❌)

---

#### 13. **IconButton**
```kotlin
IconButton(
    onClick = { navigator.pop() }
) {
    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
}
```

**Uso:** Botón circular para iconos (generalmente en TopAppBar).

**Dónde se usa:**
- Botón de volver en TopAppBar
- Acciones de menú

---

#### 14. **CircularProgressIndicator**
```kotlin
CircularProgressIndicator(
    modifier = Modifier.size(48.dp),
    color = MaterialTheme.colorScheme.primary
)
```

**Uso:** Indicador de carga circular (spinner).

**Variantes:**
- `CircularProgressIndicator()`: Indeterminado (gira infinitamente)
- `CircularProgressIndicator(progress = 0.5f)`: Determinado (muestra progreso)
- `LinearProgressIndicator()`: Barra horizontal

**Dónde se usa:**
- Mientras se cargan eventos
- Mientras se cargan asientos
- Durante el proceso de compra

---

#### 15. **HorizontalDivider**
```kotlin
HorizontalDivider(
    modifier = Modifier.padding(vertical = 8.dp),
    thickness = 1.dp,
    color = MaterialTheme.colorScheme.outlineVariant
)
```

**Uso:** Línea horizontal separadora.

**Propiedades:**
- `thickness`: Grosor de la línea
- `color`: Color de la línea

**Dónde se usa:**
- Separar secciones de contenido
- Antes de totales en resumen de compra

---

### 🖼️ Componentes Especiales

#### 16. **AsyncImage** (Coil)
```kotlin
AsyncImage(
    model = "https://ejemplo.com/imagen.jpg",
    contentDescription = "Imagen del evento",
    modifier = Modifier
        .fillMaxWidth()
        .height(250.dp),
    contentScale = ContentScale.Crop,
    placeholder = painterResource(Res.drawable.placeholder),
    error = painterResource(Res.drawable.error)
)
```

**Uso:** Cargar imágenes desde URL de forma asíncrona.

**Propiedades:**
- `model`: URL de la imagen
- `contentDescription`: Descripción para accesibilidad
- `contentScale`: Cómo escalar la imagen (Crop, Fit, FillBounds, etc.)
- `placeholder`: Imagen mientras carga
- `error`: Imagen si falla la carga

**Dónde se usa:**
- Imágenes de eventos
- Avatares
- Cualquier imagen de internet

---

### 🎭 State Management (Manejo de Estado)

#### 17. **remember / mutableStateOf**
```kotlin
var nombre by remember { mutableStateOf("") }
var isLoading by remember { mutableStateOf(false) }
var eventos by remember { mutableStateOf<List<Evento>>(emptyList()) }
```

**Uso:** Crear estado que sobrevive a recomposiciones.

**Explicación:**
- `remember`: Guarda el valor entre recomposiciones
- `mutableStateOf`: Crea un estado observable
- `by`: Delegado que permite usar `nombre` directamente en vez de `nombre.value`

**Cuándo usar:**
- Variables que afectan la UI
- Estado local de una pantalla
- Selecciones del usuario

---

#### 18. **rememberCoroutineScope**
```kotlin
val scope = rememberCoroutineScope()

Button(
    onClick = {
        scope.launch {
            // Operación suspendida (API call)
            Api.client.getEventos()
        }
    }
) {
    Text("Cargar")
}
```

**Uso:** Obtener un CoroutineScope vinculado al ciclo de vida del Composable.

**Explicación:**
- Permite lanzar coroutines desde callbacks (onClick, etc.)
- Se cancela automáticamente cuando el Composable sale de la pantalla

**Dónde se usa:**
- Llamadas a API desde botones
- Operaciones asíncronas en respuesta a eventos del usuario

---

#### 19. **LaunchedEffect**
```kotlin
LaunchedEffect(eventoId) {
    // Se ejecuta cuando cambia eventoId o al entrar a la pantalla
    isLoading = true
    Api.client.getEvento(eventoId).fold(
        onSuccess = { evento = it },
        onFailure = { error = it.message }
    )
    isLoading = false
}
```

**Uso:** Ejecutar código suspendido cuando cambia una clave o al montar el Composable.

**Propiedades:**
- `key`: Si cambia, se cancela la coroutine anterior y se lanza una nueva
- Si `key = Unit`, solo se ejecuta al montar

**Dónde se usa:**
- Cargar datos al entrar a una pantalla
- Actualizar datos cuando cambia un parámetro
- Efectos secundarios controlados

---

#### 20. **rememberScrollState / verticalScroll**
```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
) {
    // Contenido scrolleable
}
```

**Uso:** Hacer scrolleable un Column o Row.

**Explicación:**
- `rememberScrollState()`: Crea un estado de scroll que se mantiene entre recomposiciones
- `verticalScroll()`: Modifica el Column para que sea scrolleable verticalmente
- `horizontalScroll()`: Lo mismo pero horizontalmente

**Dónde se usa:**
- Pantalla de detalle del evento (mucho contenido)
- Formularios largos

---

### 🎨 Modifiers (Modificadores)

Los modifiers son la forma de personalizar componentes en Compose. Se encadenan con `.`

#### Modifiers Comunes:

**Tamaño:**
```kotlin
Modifier
    .size(48.dp)                    // Ancho y alto fijos
    .width(200.dp)                  // Solo ancho
    .height(100.dp)                 // Solo alto
    .fillMaxWidth()                 // 100% del ancho disponible
    .fillMaxHeight()                // 100% del alto disponible
    .fillMaxSize()                  // 100% de ambos
```

**Espaciado:**
```kotlin
Modifier
    .padding(16.dp)                 // Padding en todos lados
    .padding(horizontal = 16.dp)    // Solo izq/der
    .padding(vertical = 8.dp)       // Solo arriba/abajo
    .padding(start = 16.dp, end = 8.dp)
```

**Forma y Bordes:**
```kotlin
Modifier
    .background(Color.Red)
    .background(Color.Red, RoundedCornerShape(8.dp))
    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
    .clip(CircleShape)              // Recortar en círculo
```

**Interacción:**
```kotlin
Modifier
    .clickable { /* acción */ }     // Hacer clickeable
    .clickable(enabled = false) { } // Deshabilitar clicks
```

**Alineación:**
```kotlin
Modifier
    .align(Alignment.Center)        // Dentro de Box o Column/Row
```

---

### 🎬 Compose Navigation (Voyager)

#### 21. **Screen**
```kotlin
class DetalleEventoScreen(private val eventoId: Long) : Screen {
    @Composable
    override fun Content() {
        // UI de la pantalla
    }
}
```

**Uso:** Define una pantalla navegable.

**Características:**
- Puede recibir parámetros en el constructor
- Implementa `Content()` con la UI

---

#### 22. **LocalNavigator**
```kotlin
val navigator = LocalNavigator.currentOrThrow

Button(onClick = { 
    navigator.push(SeleccionAsientosScreen(evento))
}) {
    Text("Continuar")
}

IconButton(onClick = { navigator.pop() }) {
    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
}
```

**Uso:** Navegar entre pantallas.

**Métodos:**
- `push(screen)`: Ir a una nueva pantalla
- `pop()`: Volver a la pantalla anterior
- `replace(screen)`: Reemplazar pantalla actual
- `popUntilRoot()`: Volver al inicio

---

### 📊 Tabla Resumen de Componentes

| Componente | Categoría | Uso Principal | Ejemplo en la App |
|------------|-----------|---------------|-------------------|
| Column | Layout | Apilar verticalmente | Formularios, pantallas |
| Row | Layout | Apilar horizontalmente | Precio + fecha, leyenda |
| Box | Layout | Superponer/centrar | Loading, asientos |
| LazyColumn | Layout | Listas scrolleables | Eventos, asientos |
| Spacer | Layout | Espaciado | Entre secciones |
| Scaffold | Estructura | Pantalla base | Todas las pantallas |
| TopAppBar | Navegación | Barra superior | Título + volver |
| Card | Contenedor | Agrupar contenido | Tarjeta de evento |
| Button | Interacción | Acciones principales | Confirmar, continuar |
| Text | Contenido | Mostrar texto | Títulos, descripciones |
| OutlinedTextField | Input | Entrada de datos | Nombres de compradores |
| Icon | Visual | Iconos | Volver, info |
| CircularProgressIndicator | Feedback | Carga | Cargando eventos |
| AsyncImage | Media | Imágenes de red | Imágenes de eventos |
| LaunchedEffect | Estado | Efectos al montar | Cargar datos API |
| rememberCoroutineScope | Estado | Coroutines en UI | Click handlers |

---

### 🎨 Material Theme

#### MaterialTheme.colorScheme

```kotlin
// Colores dinámicos del tema
MaterialTheme.colorScheme.primary          // Color primario
MaterialTheme.colorScheme.secondary        // Color secundario
MaterialTheme.colorScheme.surface          // Fondo de superficies
MaterialTheme.colorScheme.background       // Fondo general
MaterialTheme.colorScheme.error            // Color de error
MaterialTheme.colorScheme.onPrimary        // Texto sobre primary
MaterialTheme.colorScheme.outline          // Bordes
```

**Uso:** Mantener consistencia de colores en toda la app según el tema (claro/oscuro).

#### MaterialTheme.typography

```kotlin
MaterialTheme.typography.headlineLarge     // Títulos grandes
MaterialTheme.typography.headlineMedium    // Títulos medianos
MaterialTheme.typography.titleMedium       // Subtítulos
MaterialTheme.typography.bodyLarge         // Texto normal grande
MaterialTheme.typography.bodyMedium        // Texto normal
MaterialTheme.typography.labelSmall        // Etiquetas pequeñas
```

**Uso:** Mantener consistencia tipográfica.

---

### 🔧 Ejemplos Completos de Uso

#### Ejemplo 1: Tarjeta de Evento Clickeable

```kotlin
@Composable
fun EventoCard(evento: Evento, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Imagen
            AsyncImage(
                model = evento.imagen,
                contentDescription = "Imagen de ${evento.titulo}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Título
            Text(
                text = evento.titulo,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Resumen
            Text(
                text = evento.resumen,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Precio y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "💰 $${evento.precioEntrada}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatFecha(evento.fecha),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
```

**Componentes usados:** Card, Column, AsyncImage, Spacer, Text, Row

---

#### Ejemplo 2: Pantalla con Loading y Error

```kotlin
@Composable
fun EventosScreen() {
    val scope = rememberCoroutineScope()
    var eventos by remember { mutableStateOf<List<Evento>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }
    
    // Cargar al entrar
    LaunchedEffect(Unit) {
        Api.client.getEventos().fold(
            onSuccess = { eventos = it },
            onFailure = { error = it.message ?: "Error" }
        )
        isLoading = false
    }
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Eventos") })
        }
    ) { paddingValues ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            error.isNotEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Error: $error")
                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    error = ""
                                    Api.client.getEventos().fold(
                                        onSuccess = { eventos = it },
                                        onFailure = { error = it.message ?: "Error" }
                                    )
                                    isLoading = false
                                }
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(eventos) { evento ->
                        EventoCard(evento, onClick = { /* navegar */ })
                    }
                }
            }
        }
    }
}
```

**Componentes usados:** Scaffold, TopAppBar, Box, Column, LazyColumn, CircularProgressIndicator, Button, Text

**Conceptos:** remember, mutableStateOf, LaunchedEffect, rememberCoroutineScope, when expression

---

#### Ejemplo 3: Formulario con Validación

```kotlin
@Composable
fun FormularioComprador() {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val esValido = nombre.isNotBlank() && email.contains("@")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = nombre.isBlank(),
            supportingText = {
                if (nombre.isBlank()) {
                    Text("Campo requerido")
                }
            }
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = !email.contains("@") && email.isNotEmpty(),
            supportingText = {
                if (!email.contains("@") && email.isNotEmpty()) {
                    Text("Email inválido")
                }
            }
        )
        
        Button(
            onClick = { /* enviar */ },
            enabled = esValido,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar")
        }
    }
}
```

**Componentes usados:** Column, OutlinedTextField, Button, Text

**Conceptos:** Validación en tiempo real, estado derivado, enabled condicional

---

## 📚 Recursos Adicionales

### Documentación Oficial

- **Compose Multiplatform:** https://www.jetbrains.com/lp/compose-multiplatform/
- **Material 3 Components:** https://m3.material.io/components
- **Compose API:** https://developer.android.com/jetpack/compose

### Librerías Utilizadas

- **Ktor Client:** HTTP client multiplataforma
- **Voyager:** Navegación para Compose Multiplatform
- **Coil:** Carga de imágenes (AsyncImage)
- **Kotlinx Serialization:** Serialización JSON

---



### Visión General del Proceso

```
┌─────────────┐      ┌──────────────┐      ┌─────────────────┐      ┌──────────────┐
│   Lista de  │      │   Detalle    │      │   Selección     │      │ Confirmación │
│   Eventos   │ ───> │   Evento     │ ───> │   Asientos      │ ───> │   y Pago     │
└─────────────┘      └──────────────┘      └─────────────────┘      └──────────────┘
       │                     │                       │                       │
       │                     │                       │                       │
       ▼                     ▼                       ▼                       ▼
  GET /eventos        GET /eventos/{id}    GET /asientos/evento/{id}   POST /compras/bloquear
                                                                        POST /compras/realizar
```

### Paso a Paso Detallado

#### **1. Lista de Eventos** (`EventosTab.kt`)

**Endpoint:** `GET /api/eventos`

```kotlin
Api.client.getEventos()
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "titulo": "Concierto de Rock",
    "resumen": "Una noche inolvidable",
    "descripcion": "...",
    "fecha": "2024-12-31T20:00:00Z",
    "precioEntrada": 5000.0,
    "filaAsientos": 10,
    "columnaAsientos": 20,
    "imagen": "https://..."
  }
]
```

**UI:** Muestra tarjetas con imagen, título, resumen, fecha y precio.

---

#### **2. Detalle del Evento** (`DetalleEventoScreen.kt`)

Cuando el usuario clickea un evento:

**Endpoint:** `GET /api/eventos/{id}`

```kotlin
Api.client.getEvento(eventoId)
```

**Muestra:**
- Imagen del evento
- Título y fecha
- Dirección (si existe)
- Precio de entrada
- Resumen y descripción
- Información de capacidad (filas × columnas)
- Botón "Seleccionar Asientos"

---

#### **3. Selección de Asientos** (`SeleccionAsientosScreen.kt`)

**¡ESTA ES LA PARTE MÁS IMPORTANTE!** 🎯

##### 3.1. Carga del Estado de Asientos

**Endpoint:** `GET /api/asientos/evento/{eventoId}`

```kotlin
LaunchedEffect(evento.id) {
    Api.client.getAsientosEvento(evento.id).fold(
        onSuccess = { asientos ->
            asientosRedis = asientos
        },
        onFailure = { error = it.message }
    )
    isLoading = false
}
```

**Respuesta del Backend (desde Redis):**
```json
[
  {
    "fila": 1,
    "columna": 3,
    "estado": "Bloqueado",
    "persona": null
  },
  {
    "fila": 2,
    "columna": 5,
    "estado": "Vendido",
    "persona": "Juan Perez"
  }
]
```

**⚠️ IMPORTANTE:** Redis solo contiene asientos que NO están libres. Si un asiento no aparece en la lista, significa que está **LIBRE**.

##### 3.2. Mapeo de Coordenadas (¡CRÍTICO! 🔴)

**Problema:** La UI usa índices **base-0** (0, 1, 2...) pero el backend usa **base-1** (1, 2, 3...)

**Solución:**

```kotlin
fun getEstadoAsiento(fila: Int, columna: Int, asientosRedis: List<AsientoRedis>): EstadoAsiento {
    // fila y columna vienen en base-0 desde la UI
    val filaBackend = fila + 1  // Convertir a base-1
    val columnaBackend = columna + 1
    
    val asiento = asientosRedis.find { 
        it.fila == filaBackend && it.columna == columnaBackend 
    }
    
    return when (asiento?.estado?.lowercase()) {
        "bloqueado" -> EstadoAsiento.BLOQUEADO
        "vendido", "ocupado" -> EstadoAsiento.VENDIDO
        "libre" -> EstadoAsiento.LIBRE
        null -> EstadoAsiento.LIBRE // No está en Redis = LIBRE
        else -> EstadoAsiento.LIBRE
    }
}
```

**Ejemplo de Mapeo:**

| UI (base-0) | Backend (base-1) | Redis | Estado UI |
|-------------|------------------|-------|-----------|
| fila=0, col=2 | fila=1, col=3 | {"fila":1,"columna":3,"estado":"Bloqueado"} | 🟡 BLOQUEADO |
| fila=1, col=4 | fila=2, col=5 | {"fila":2,"columna":5,"estado":"Vendido"} | 🔴 VENDIDO |
| fila=3, col=1 | fila=4, col=2 | (no existe en Redis) | 🟢 LIBRE |

##### 3.3. Renderizado de la Matriz de Asientos

```kotlin
// Para cada fila del evento
items(evento.filaAsientos) { fila ->
    FilaAsientos(
        fila = fila,  // base-0
        columnas = evento.columnaAsientos,
        asientosRedis = asientosRedis,
        asientosSeleccionados = asientosSeleccionados,
        onAsientoClick = { f, c ->
            val estadoRedis = getEstadoAsiento(f, c, asientosRedis)
            
            // Solo permitir click en LIBRES
            if (estadoRedis == EstadoAsiento.LIBRE) {
                if (asiento in asientosSeleccionados) {
                    // Deseleccionar
                    asientosSeleccionados -= asiento
                } else if (asientosSeleccionados.size < 4) {
                    // Seleccionar (máximo 4)
                    asientosSeleccionados += asiento
                }
            }
        }
    )
}
```

**Colores de Asientos:**
- 🟢 **Verde** = LIBRE (clickeable)
- 🔵 **Azul** = SELECCIONADO por el usuario (clickeable para deseleccionar)
- 🟡 **Amarillo** = BLOQUEADO por otro usuario (no clickeable, expira en 5 min)
- 🔴 **Rojo** = VENDIDO (no clickeable, permanente)

##### 3.4. Validaciones de Selección

```kotlin
// Límite de 4 asientos por compra
if (asientosSeleccionados.size < 4) {
    asientosSeleccionados += asiento
} else {
    println("⚠️ Límite alcanzado (4 asientos)")
}

// Solo mostrar botón "Continuar" si hay asientos seleccionados
if (asientosSeleccionados.isNotEmpty()) {
    Button(
        onClick = {
            navigator.push(
                ConfirmacionVentaScreen(evento, asientosSeleccionados.toList())
            )
        }
    ) {
        Text("Continuar con ${asientosSeleccionados.size} asientos")
    }
}
```

---

#### **4. Confirmación de Venta** (`ConfirmacionVentaScreen.kt`)

##### 4.1. Entrada de Datos del Comprador

```kotlin
// Para cada asiento seleccionado, pedir nombre/apellido
items(asientosSeleccionados.toList()) { asiento ->
    AsientoConNombre(
        fila = asiento.first,
        columna = asiento.second,
        nombre = nombresAsientos[asiento] ?: "",
        onNombreChange = { nuevoNombre ->
            nombresAsientos[asiento] = nuevoNombre
        }
    )
}

// Validar que todos tengan nombre
val todosLosNombresCompletos = nombresAsientos.values.all { it.isNotBlank() }
```

##### 4.2. Proceso de Compra (2 pasos)

**🔒 PASO 1: Bloquear Asientos**

**Endpoint:** `POST /api/compras/bloquear`

**Request Body:**
```json
{
  "eventoId": 1,
  "asientos": [
    {"fila": 1, "columna": 3},
    {"fila": 1, "columna": 4}
  ]
}
```

**Código:**
```kotlin
// Convertir de base-0 a base-1
val asientosCoord = asientosSeleccionados.map { (fila, columna) ->
    AsientoCoord(
        fila = fila + 1,      // ← Conversión base-0 a base-1
        columna = columna + 1
    )
}

val bloquearRequest = BloquearAsientosRequest(
    eventoId = evento.id,
    asientos = asientosCoord
)

Api.client.bloquearAsientos(bloquearRequest).fold(
    onSuccess = { respuesta ->
        // ✅ Continuar con paso 2
    },
    onFailure = { error ->
        // ❌ Mostrar error (asiento ya tomado)
    }
)
```

**Response:**
```json
{
  "mensaje": "Asientos bloqueados correctamente",
  "bloqueados": 2,
  "exito": true
}
```

**¿Qué hace el backend?**
1. Verifica que los asientos estén libres en Redis
2. Marca los asientos como "Bloqueado" en Redis
3. Establece un TTL de 5 minutos (expiración automática)
4. Devuelve confirmación

**⏱️ Importante:** El bloqueo expira en 5 minutos. Si no completas la compra en ese tiempo, los asientos vuelven a estar LIBRES automáticamente.

---

**💰 PASO 2: Realizar Venta**

**Endpoint:** `POST /api/compras/realizar`

**Request Body:**
```json
{
  "eventoId": 1,
  "asientos": [
    {
      "fila": 1,
      "columna": 3,
      "persona": "Juan Perez"
    },
    {
      "fila": 1,
      "columna": 4,
      "persona": "Maria Lopez"
    }
  ]
}
```

**Código:**
```kotlin
val asientosVenta = asientosSeleccionados.map { (fila, columna) ->
    AsientoVenta(
        fila = fila + 1,      // ← Conversión base-0 a base-1
        columna = columna + 1,
        persona = nombresAsientos[Pair(fila, columna)] ?: ""
    )
}

val ventaRequest = RealizarVentaRequest(
    eventoId = evento.id,
    asientos = asientosVenta
)

Api.client.realizarVenta(ventaRequest).fold(
    onSuccess = { venta ->
        println("✅ Compra exitosa! ID: ${venta.id}")
        success = true
    },
    onFailure = { error ->
        println("❌ Error: ${error.message}")
        this.error = error.message
    }
)
```

**Response:**
```json
{
  "id": 42,
  "fechaVenta": "2024-12-26T07:04:20.123Z",
  "precioVenta": 10000.0,
  "cantidadAsientos": 2,
  "resultado": true,
  "estadoVenta": "CONFIRMADA",
  "descripcion": "Venta confirmada para 2 asientos",
  "evento": {
    "id": 1,
    "titulo": null,
    "resumen": null,
    "descripcion": null,
    "fecha": null,
    "precioEntrada": null,
    "eventoTipo": null
  }
}
```

**⚠️ NOTA:** El objeto `evento` dentro de la respuesta puede tener campos `null`. Por eso tuvimos que hacer los campos de `EventoResumido` opcionales.

**¿Qué hace el backend?**
1. Verifica que los asientos estén bloqueados (y no vendidos)
2. Crea entidad `Venta` en PostgreSQL
3. Crea entidades `AsientoVenta` en PostgreSQL
4. Cambia el estado en Redis de "Bloqueado" → "Vendido"
5. Elimina el TTL (los asientos quedan vendidos permanentemente)
6. Devuelve el objeto `Venta` con toda la información

---

## 🗺️ Sistema de Asientos

### Estructura de Datos

```kotlin
enum class EstadoAsiento {
    LIBRE,        // Verde - Disponible para comprar
    SELECCIONADO, // Azul - Seleccionado por el usuario actual (solo UI)
    BLOQUEADO,    // Amarillo - Bloqueado por otro usuario (5 min)
    VENDIDO       // Rojo - Vendido (permanente)
}

data class AsientoRedis(
    val fila: Int,           // base-1 (desde backend)
    val columna: Int,        // base-1 (desde backend)
    val estado: String,      // "Libre" | "Bloqueado" | "Vendido"
    val persona: String?     // Nombre del comprador (si está vendido)
)
```

### Coordenadas: Base-0 vs Base-1

**🎯 REGLA DE ORO:**
- **UI (Kotlin):** Trabaja en base-0 (los arrays empiezan en 0)
- **Backend (Redis/DB):** Trabaja en base-1 (las filas/columnas empiezan en 1)
- **Conversión:** Siempre antes de enviar al backend o al recibir

**Tabla de Conversión:**

| Contexto | Fila | Columna | Cuándo |
|----------|------|---------|--------|
| UI interna | 0-based | 0-based | Loops, arrays, Set<Pair<Int, Int>> |
| Display al usuario | 1-based | 1-based | Text("Fila ${fila + 1}") |
| Backend (Redis) | 1-based | 1-based | AsientoRedis.fila, AsientoRedis.columna |
| Backend (Request) | 1-based | 1-based | AsientoCoord, AsientoVenta |

**Ejemplo Práctico:**

```kotlin
// UI: Usuario ve "Fila 1, Columna 3"
// Internamente se guarda como Pair(0, 2)
val asientoUI = Pair(0, 2)

// Al enviar al backend:
val asientoBackend = AsientoCoord(
    fila = asientoUI.first + 1,    // 0 + 1 = 1
    columna = asientoUI.second + 1 // 2 + 1 = 3
)
// Backend recibe: {"fila": 1, "columna": 3} ✅

// Al recibir de Redis:
val asientoRedis = AsientoRedis(fila = 1, columna = 3, estado = "Bloqueado")

// Para buscar en la UI:
val filaUI = asientoRedis.fila - 1      // 1 - 1 = 0
val columnaUI = asientoRedis.columna - 1 // 3 - 1 = 2
// UI busca Pair(0, 2) ✅
```

---

## 🔌 API y Backend

### Endpoints Utilizados

#### Autenticación

```http
POST /api/authenticate
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}

Response:
{
  "id_token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

```http
POST /api/register
Content-Type: application/json

{
  "login": "newuser",
  "password": "password123",
  "langKey": "es"
}

Response: 201 Created
```

#### Eventos

```http
GET /api/eventos
Authorization: Bearer {token}

Response:
[
  {
    "id": 1,
    "titulo": "Evento 1",
    "resumen": "...",
    ...
  }
]
```

```http
GET /api/eventos/{id}
Authorization: Bearer {token}

Response:
{
  "id": 1,
  "titulo": "Evento 1",
  "filaAsientos": 10,
  "columnaAsientos": 20,
  ...
}
```

#### Asientos

```http
GET /api/asientos/evento/{eventoId}
Authorization: Bearer {token}

Response:
[
  {
    "fila": 1,
    "columna": 3,
    "estado": "Bloqueado",
    "persona": null
  },
  {
    "fila": 2,
    "columna": 5,
    "estado": "Vendido",
    "persona": "Juan Perez"
  }
]
```

**⚠️ IMPORTANTE:** Si la lista está vacía `[]`, significa que TODOS los asientos están libres.

#### Compras

```http
POST /api/compras/bloquear
Authorization: Bearer {token}
Content-Type: application/json

{
  "eventoId": 1,
  "asientos": [
    {"fila": 1, "columna": 3},
    {"fila": 1, "columna": 4}
  ]
}

Response:
{
  "mensaje": "Asientos bloqueados correctamente",
  "bloqueados": 2,
  "exito": true
}
```

```http
POST /api/compras/realizar
Authorization: Bearer {token}
Content-Type: application/json

{
  "eventoId": 1,
  "asientos": [
    {
      "fila": 1,
      "columna": 3,
      "persona": "Juan Perez"
    },
    {
      "fila": 1,
      "columna": 4,
      "persona": "Maria Lopez"
    }
  ]
}

Response:
{
  "id": 42,
  "fechaVenta": "2024-12-26T07:04:20.123Z",
  "precioVenta": 10000.0,
  "cantidadAsientos": 2,
  "resultado": true,
  "estadoVenta": "CONFIRMADA",
  "descripcion": "Venta confirmada para 2 asientos",
  "evento": {...}
}
```

### Cliente HTTP (ApiClient.kt)

```kotlin
class ApiClient(private val baseUrl: String = "http://192.168.100.12:8080") {
    
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                coerceInputValues = true  // ← Importante para nulls
                encodeDefaults = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY  // ← Ver todo el tráfico HTTP
        }
    }
    
    private var authToken: String? = null
    
    suspend fun login(request: LoginRequest): Result<AuthResponse>
    suspend fun register(request: RegisterRequest): Result<Unit>
    suspend fun getEventos(): Result<List<Evento>>
    suspend fun getEvento(id: Long): Result<Evento>
    suspend fun getAsientosEvento(eventoId: Long): Result<List<AsientoRedis>>
    suspend fun bloquearAsientos(request: BloquearAsientosRequest): Result<BloqueoResponse>
    suspend fun realizarVenta(request: RealizarVentaRequest): Result<Venta>
}
```

**Uso:**
```kotlin
// Login
Api.client.login(LoginRequest("user", "pass")).fold(
    onSuccess = { response ->
        // Token guardado automáticamente
    },
    onFailure = { error ->
        // Manejar error
    }
)

// Cualquier request posterior usa el token automáticamente
Api.client.getEventos().fold(...)
```

---

## 📦 Modelos de Datos

### Evento

```kotlin
@Serializable
data class Evento(
    val id: Long,
    val titulo: String,
    val resumen: String,
    val descripcion: String,
    val fecha: String,
    val direccion: String? = null,
    val imagen: String? = null,
    val filaAsientos: Int,           // Número de filas
    val columnaAsientos: Int,        // Asientos por fila
    val precioEntrada: Double,
    @SerialName("tipo")
    val eventoTipo: EventoTipo? = null,
    val integrantes: List<Integrante> = emptyList()
)
```

### EventoResumido (para listas)

```kotlin
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
```

**⚠️ Todos los campos son opcionales** porque el backend a veces devuelve objetos con `null` (especialmente dentro de `Venta.evento`).

### Asientos

```kotlin
@Serializable
data class AsientoRedis(
    val fila: Int,           // base-1
    val columna: Int,        // base-1
    val estado: String,      // "Libre" | "Bloqueado" | "Vendido"
    val persona: String? = null
)

@Serializable
data class AsientoCoord(
    val fila: Int,           // base-1
    val columna: Int         // base-1
)

@Serializable
data class AsientoVenta(
    val fila: Int,           // base-1
    val columna: Int,        // base-1
    val persona: String
)
```

### Requests/Responses

```kotlin
@Serializable
data class BloquearAsientosRequest(
    val eventoId: Long,
    val asientos: List<AsientoCoord>
)

@Serializable
data class BloqueoResponse(
    val mensaje: String? = null,
    val bloqueados: Int? = null,
    val exito: Boolean? = null
)

@Serializable
data class RealizarVentaRequest(
    val eventoId: Long,
    val asientos: List<AsientoVenta>
)

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
```

---

## 🖥️ Pantallas de la Aplicación

### 1. EventosTab (Lista)

**Responsabilidad:**
- Mostrar todos los eventos disponibles
- Navegación al detalle

**Estados:**
- Loading (CircularProgressIndicator)
- Error (con botón reintentar)
- Empty (sin eventos)
- Success (lista de tarjetas)

**Componentes:**
```kotlin
@Composable
fun EventoCard(evento: Evento, onClick: () -> Unit) {
    Card {
        AsyncImage(url = evento.imagen)
        Text(evento.titulo)
        Text(evento.resumen)
        Text(evento.fecha)
        Text("$${evento.precioEntrada}")
    }
}
```

### 2. DetalleEventoScreen

**Responsabilidad:**
- Mostrar información completa del evento
- Botón para ir a selección de asientos

**Datos mostrados:**
- Imagen
- Título y fecha
- Dirección
- Precio
- Resumen y descripción
- Capacidad (filas × columnas)

### 3. SeleccionAsientosScreen

**Responsabilidad:**
- Cargar estado de asientos desde Redis
- Mostrar matriz visual de asientos
- Permitir selección (máx 4)
- Navegación a confirmación

**Algoritmo:**

```kotlin
// 1. Cargar asientos desde Redis
LaunchedEffect {
    asientosRedis = Api.client.getAsientosEvento(eventoId)
}

// 2. Renderizar matriz
for (fila in 0 until evento.filaAsientos) {
    for (columna in 0 until evento.columnaAsientos) {
        val estado = getEstadoAsiento(fila, columna, asientosRedis)
        AsientoItem(fila, columna, estado)
    }
}

// 3. Manejar clicks
onAsientoClick = { fila, columna ->
    if (estado == LIBRE) {
        if (asiento in seleccionados) {
            seleccionados -= asiento  // Deseleccionar
        } else if (seleccionados.size < 4) {
            seleccionados += asiento  // Seleccionar
        }
    }
}
```

### 4. ConfirmacionVentaScreen

**Responsabilidad:**
- Recolectar nombres de compradores
- Mostrar resumen de compra
- Ejecutar proceso de compra (bloquear + vender)
- Mostrar resultado

**Proceso:**

```kotlin
onClick = {
    // 1. Bloquear
    bloquearAsientos(asientos)
        .onSuccess {
            // 2. Vender
            realizarVenta(asientos + nombres)
                .onSuccess {
                    success = true
                    // Mostrar mensaje de éxito
                }
                .onFailure {
                    error = "Error al realizar venta"
                }
        }
        .onFailure {
            error = "Asientos no disponibles"
        }
}
```

---

## 🐛 Debugging y Troubleshooting

### Logs en la Aplicación

Todos los requests tienen logging extensivo:

```kotlin
println("🌐 [API] GET $baseUrl/api/asientos/evento/$eventoId")
println("📦 [API] Response: ${response.size} asientos")
println("🔒 [API] POST $baseUrl/api/compras/bloquear")
println("✅ [API] Bloqueo response: $response")
println("❌ [API] Error: ${e.message}")
```

**Emojis de logging:**
- 🌐 = Request HTTP
- 📦 = Response exitosa
- ✅ = Operación exitosa
- ❌ = Error
- 🔒 = Bloqueo
- 💰 = Venta
- 🔄 = Carga/recarga
- 👆 = Interacción de usuario
- 🎨 = Renderizado UI
- 🔍 = Búsqueda/match

### Verificar en Logcat (Android Studio)

```bash
# Ver logs de API
adb logcat | grep "\[API\]"

# Ver logs de asientos
adb logcat | grep "asiento"

# Ver logs de compra
adb logcat | grep "compra\|venta"
```

### Testing Manual con cURL

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://192.168.100.12:8080/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user"}' \
  | jq -r '.id_token')

# 2. Ver eventos
curl -s http://192.168.100.12:8080/api/eventos \
  -H "Authorization: Bearer $TOKEN" | jq .

# 3. Ver asientos del evento 1
curl -s http://192.168.100.12:8080/api/asientos/evento/1 \
  -H "Authorization: Bearer $TOKEN" | jq .

# 4. Bloquear asiento
curl -s -X POST http://192.168.100.12:8080/api/compras/bloquear \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"eventoId":1,"asientos":[{"fila":1,"columna":3}]}' | jq .

# 5. Realizar venta
curl -s -X POST http://192.168.100.12:8080/api/compras/realizar \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"eventoId":1,"asientos":[{"fila":1,"columna":3,"persona":"Juan Perez"}]}' | jq .
```

### Errores Comunes y Soluciones

#### 1. "Field 'columnaAsientos' is required"

**Problema:** Backend envía `columnaAsientos` pero modelo esperaba `columnAsientos`.

**Solución:**
```kotlin
// ❌ INCORRECTO
val columnAsientos: Int

// ✅ CORRECTO
val columnaAsientos: Int
```

#### 2. "Unexpected JSON token... Unexpected 'null'"

**Problema:** Backend devuelve campos `null` pero el modelo no los permite.

**Solución:**
```kotlin
// ❌ INCORRECTO
data class EventoResumido(
    val titulo: String,
    val resumen: String
)

// ✅ CORRECTO
data class EventoResumido(
    val titulo: String? = null,
    val resumen: String? = null
)
```

#### 3. "Request method 'POST' is not supported"

**Problema:** URL incorrecta del endpoint.

**Solución:**
```kotlin
// ❌ INCORRECTO
client.post("$baseUrl/api/ventas/bloquear")

// ✅ CORRECTO
client.post("$baseUrl/api/compras/bloquear")
```

#### 4. Asientos desfasados (muestra fila/columna incorrecta)

**Problema:** No se está convirtiendo entre base-0 y base-1.

**Solución:**
```kotlin
// Al enviar al backend
AsientoCoord(
    fila = filaUI + 1,      // ← +1
    columna = columnaUI + 1
)

// Al recibir de Redis
val filaUI = asientoRedis.fila - 1      // ← -1
val columnaUI = asientoRedis.columna - 1
```

#### 5. "Asiento no disponible" al intentar comprar un asiento libre

**Problema:** Otro usuario lo bloqueó justo antes, o el estado de Redis no se actualizó.

**Solución:**
- Implementar polling o WebSockets para actualizar en tiempo real
- Por ahora: pedir al usuario que recargue la selección de asientos

#### 6. Bloqueo expiró antes de completar compra

**Problema:** Usuario tardó más de 5 minutos.

**Solución:**
- Mostrar timer en la UI
- Permitir re-bloquear antes de vender
- Backend rechazará la venta si el bloqueo expiró

---

## 📊 Diagrama de Estados de Asiento

```
                   ┌─────────┐
                   │  LIBRE  │ (no está en Redis)
                   └────┬────┘
                        │
                        │ Usuario hace click
                        ▼
                 ┌──────────────┐
                 │ SELECCIONADO │ (solo UI, no en backend)
                 └──────┬───────┘
                        │
                        │ Click en "Continuar"
                        │ POST /bloquear
                        ▼
                  ┌────────────┐
                  │ BLOQUEADO  │ (Redis, TTL 5 min)
                  └─────┬──────┘
                        │
                ┌───────┴───────┐
                │               │
                │ POST /realizar  │ TTL expira
                │               │
                ▼               ▼
         ┌──────────┐      ┌─────────┐
         │ VENDIDO  │      │  LIBRE  │
         └──────────┘      └─────────┘
         (permanente)      (disponible otra vez)
```

---

## 🚀 Compilar e Instalar

### Debug Build

```bash
cd /home/marcos/Escritorio/trabajo/EventosApp

# Compilar
./gradlew :composeApp:assembleDebug

# Instalar en dispositivo/emulador
./gradlew :composeApp:installDebug

# O manualmente
adb install composeApp/build/outputs/apk/debug/composeApp-debug.apk
```

### Release Build

```bash
# Generar APK firmado
./gradlew :composeApp:assembleRelease

# Ubicación del APK
# composeApp/build/outputs/apk/release/composeApp-release.apk
```

---

## 🎯 Resumen Ejecutivo

### Flujo Completo de Compra

1. **Usuario ve lista de eventos** → GET `/api/eventos`
2. **Usuario clickea evento** → GET `/api/eventos/{id}`
3. **Usuario clickea "Seleccionar Asientos"**
4. **Sistema carga estado de Redis** → GET `/api/asientos/evento/{id}`
5. **Sistema renderiza matriz** con colores según estado
6. **Usuario selecciona hasta 4 asientos** (base-0 en UI)
7. **Usuario clickea "Continuar"**
8. **Usuario ingresa nombres** para cada asiento
9. **Sistema bloquea asientos** → POST `/api/compras/bloquear` (base-1)
10. **Si bloqueo OK, sistema realiza venta** → POST `/api/compras/realizar` (base-1)
11. **Sistema muestra confirmación** con ID de venta
12. **Usuario vuelve a la lista de eventos**

### Conceptos Clave

- **Base-0 vs Base-1:** UI trabaja en base-0, backend en base-1. Convertir siempre.
- **Redis = Estado en tiempo real:** Solo contiene asientos NO libres.
- **Bloqueo temporal:** 5 minutos, después vuelve a LIBRE automáticamente.
- **Venta = Permanente:** Los asientos vendidos nunca vuelven a estar libres.
- **Máximo 4 asientos:** Por transacción (validación en UI).
- **Proceso de 2 pasos:** Primero bloquear, luego vender (transacción segura).

### Archivos Más Importantes

1. `ApiClient.kt` - Cliente HTTP con todos los endpoints
2. `EventoModels.kt` - Modelos de datos (Evento, Asiento, Venta, etc.)
3. `SeleccionAsientosScreen.kt` - Lógica de matriz y selección
4. `ConfirmacionVentaScreen.kt` - Proceso de compra (bloquear + vender)
5. `DetalleEventoScreen.kt` - Detalle del evento
6. `EventosTab.kt` - Lista de eventos

---

## 📞 Contacto y Soporte

Para dudas o problemas:

1. **Revisar logs** en Logcat con los filtros mencionados
2. **Verificar backend** con cURL (ver sección Testing Manual)
3. **Revisar estado de Redis** en el backend
4. **Comprobar sincronización** entre UI (base-0) y backend (base-1)

---

**Última actualización:** 26 de diciembre de 2024

**Versión del sistema:** 1.0.0

**Autor:** Marcos - EventosApp

---


