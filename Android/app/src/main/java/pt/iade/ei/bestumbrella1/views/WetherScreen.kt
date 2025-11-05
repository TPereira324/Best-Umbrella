package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class WeatherEntry(
    val day: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    val forecast = listOf(
        WeatherEntry("Hoje", 22.0, "Ensolarado", 45, 12.0),
        WeatherEntry("Sábado", 19.5, "Parcialmente Nublado", 50, 10.5),
        WeatherEntry("Domingo", 17.0, "Chuva Leve", 70, 14.0),
        WeatherEntry("Segunda", 20.5, "Céu Limpo", 40, 9.2)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("map") },
                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                    label = { Text("Mapa", color = Color.Black, fontWeight = FontWeight.Bold) }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("qrscanner") },
                    icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null) },
                    label = { Text("Scanner", color = Color.Black, fontWeight = FontWeight.Bold) }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Cloud, contentDescription = null) },
                    label = { Text("Tempo", color = Color.Black, fontWeight = FontWeight.Bold) }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    label = { Text("Histórico", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
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
                    .padding(16.dp)
            ) {
                Text(
                    "Meteorologia",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(Modifier.height(16.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(60.dp)
                        )
                        Text("Lisboa, Portugal", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text("22°C — Ensolarado", style = MaterialTheme.typography.headlineSmall, color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Humidade: 45% | Vento: 12 km/h", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "Próximos dias",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Black, fontWeight = FontWeight.Bold)
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn {
                    items(forecast) { day ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(day.day, fontWeight = FontWeight.Bold, color = Color.Black)
                                    Text(day.condition, style = MaterialTheme.typography.bodySmall, color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("${day.temperature}°C", color = Color.Black, fontWeight = FontWeight.Bold)
                                    Text("💨 ${day.windSpeed} km/h", style = MaterialTheme.typography.bodySmall, color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWeatherScreen() {
    val navController = rememberNavController()
    WeatherScreen(navController)
}

