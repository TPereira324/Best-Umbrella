package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.iade.ei.bestumbrella1.di.AppModule
import pt.iade.ei.bestumbrella1.models.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersAdminScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val sessionManager: SessionManager = AppModule.provideSessionManager(context)
    val usersViewModel = AppModule.provideUsersViewModel(context)

    LaunchedEffect(Unit) {
        usersViewModel.fetchUsers()
    }

    val users by usersViewModel.users.collectAsState()
    val isLoading by usersViewModel.isLoading.collectAsState()
    val error by usersViewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3).copy(alpha = 0.7f), Color(0xFFE3F2FD))
                )
            )
    ) {
        TopAppBar(
            title = { Text("Utilizadores", color = Color.Black) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                }
            }
        )

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("A carregar...", color = Color.Black)
            }
        } else if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Erro: ${error}", color = MaterialTheme.colorScheme.error)
            }
        } else {
            // Ordenar para mostrar o admin primeiro
            val sortedUsers = users.sortedByDescending { it.email.equals("admin@bestumbrella", ignoreCase = true) }
            LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                items(sortedUsers) { user ->
                    val isAdminUser = user.email.equals("admin@bestumbrella", ignoreCase = true)
                    val displayName = if (user.name.isNotBlank()) user.name else user.email
                    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(displayName, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (isAdminUser) "Principal" else "Teste",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isAdminUser) Color(0xFF2E7D32) else Color(0xFF616161)
                            )
                        }
                        Text(user.email, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}