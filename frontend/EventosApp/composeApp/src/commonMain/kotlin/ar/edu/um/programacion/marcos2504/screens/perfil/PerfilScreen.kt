package ar.edu.um.programacion.marcos2504.screens.perfil


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ar.edu.um.programacion.marcos2504.api.Api
import ar.edu.um.programacion.marcos2504.models.User
import ar.edu.um.programacion.marcos2504.screens.auth.LoginScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen() {
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()

    var user by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Cargar datos del usuario
    LaunchedEffect(Unit) {
        isLoading = true
        error = ""
        Api.client.getCurrentUser().fold(
            onSuccess = {
                user = it
                println(" Usuario cargado: ${it.login}")
            },
            onFailure = {
                error = it.message ?: "Error al cargar perfil"
                println(" Error al cargar perfil: ${it.message}")
                it.printStackTrace()
            }
        )
        isLoading = false
    }

    // Dialog de confirmación para cerrar sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión"
                )
            },
            title = {
                Text("Cerrar Sesión")
            },
            text = {
                Text("¿Estás seguro de que deseas cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        Api.client.clearAuthToken()
                        navigator.replaceAll(LoginScreen())
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
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
                        text = "Error al cargar perfil",
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
                                Api.client.getCurrentUser().fold(
                                    onSuccess = { user = it },
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

            user != null -> {
                // Header con foto de perfil y nombre
                ProfileHeader(user!!)

                Spacer(modifier = Modifier.height(24.dp))

                // Sección de Información Personal
                SectionTitle("Información Personal")
                Spacer(modifier = Modifier.height(8.dp))

                InfoCard(
                    icon = Icons.Default.Person,
                    label = "Nombre de usuario",
                    value = user!!.login
                )

                if (!user!!.firstName.isNullOrBlank() || !user!!.lastName.isNullOrBlank()) {
                    InfoCard(
                        icon = Icons.Default.Badge,
                        label = "Nombre completo",
                        value = "${user!!.firstName ?: ""} ${user!!.lastName ?: ""}".trim()
                    )
                }

                if (!user!!.email.isNullOrBlank()) {
                    InfoCard(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = user!!.email!!
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Cuenta
                SectionTitle("Cuenta")
                Spacer(modifier = Modifier.height(8.dp))

                InfoCard(
                    icon = Icons.Default.AccountCircle,
                    label = "ID de usuario",
                    value = "#${user!!.id}"
                )

                InfoCard(
                    icon = if (user!!.activated) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    label = "Estado",
                    value = if (user!!.activated) "Activada" else "Inactiva",
                    valueColor = if (user!!.activated) Color(0xFF4CAF50) else Color(0xFFF44336)
                )

                if (!user!!.langKey.isNullOrBlank()) {
                    InfoCard(
                        icon = Icons.Default.Language,
                        label = "Idioma",
                        value = user!!.langKey!!.uppercase()
                    )
                }

                if (user!!.authorities.isNotEmpty()) {
                    InfoCard(
                        icon = Icons.Default.Security,
                        label = "Roles",
                        value = user!!.authorities.joinToString(", ")
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Cerrar Sesión
                Button(
                    onClick = { showLogoutDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader(user: User) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto de perfil
        if (!user.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = user.imageUrl,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Icono por defecto si no hay imagen
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Sin foto",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre del usuario
        Text(
            text = if (!user.firstName.isNullOrBlank() || !user.lastName.isNullOrBlank()) {
                "${user.firstName ?: ""} ${user.lastName ?: ""}".trim()
            } else {
                user.login
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (!user.firstName.isNullOrBlank() || !user.lastName.isNullOrBlank()) {
            Text(
                text = "@${user.login}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun InfoCard(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = valueColor
                )
            }
        }
    }
}

