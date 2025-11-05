package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.di.AppModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = AppModule.provideSessionManager(context)
    val coroutineScope = rememberCoroutineScope()
    

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("map") },
                    icon = { Icon(Icons.Default.Map, null) },
                    label = { Text("Mapa", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("qrscanner") },
                    icon = { Icon(Icons.Default.QrCodeScanner, null) },
                    label = { Text("Scanner", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("weather") },
                    icon = { Icon(Icons.Default.Cloud, contentDescription = null) },
                    label = { Text("Tempo", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    icon = { Icon(Icons.Default.History, null) },
                    label = { Text("Histórico", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Person, null) },
                    label = { Text("Perfil", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3).copy(alpha = 0.7f),
                            Color(0xFFE3F2FD)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))

                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    tint = Color.White
                )
                Text(
                    "admin@bestumbrella",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                AssistChip(
                    onClick = {},
                    label = { Text("Eco Warrior", color = Color.Black, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(Modifier.height(24.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("0", fontSize = 22.sp, color = Color(0xFF1565C0))
                            Text("Usos", style = MaterialTheme.typography.bodySmall, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("50", fontSize = 22.sp, color = Color(0xFF1565C0))
                            Text("Pontos", style = MaterialTheme.typography.bodySmall, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("€0.28", fontSize = 22.sp, color = Color(0xFF1565C0))
                            Text("Poupado", style = MaterialTheme.typography.bodySmall, color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Saldo", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("€0.00", color = Color(0xFFD32F2F), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Button(onClick = { navController.navigate("payment") }) {
                                Text("Recarregar")
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Recarregue para começar a usar guarda-chuvas",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Atividade Recente", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Icon(
                            Icons.Default.Umbrella,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = Color(0xFF1565C0)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Nenhuma atividade ainda", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text(
                            "Sua primeira reserva aparecerá aqui",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(5.dp))

                
                Button(
                    onClick = {
                        coroutineScope.launch {
                            sessionManager.clearSession()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    modifier = Modifier
                        .height(50.dp)
                    ,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "logout",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    val navController = rememberNavController()
    ProfileScreen(navController)
}
