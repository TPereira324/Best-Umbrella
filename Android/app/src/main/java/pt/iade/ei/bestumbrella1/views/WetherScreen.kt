package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.livedata.observeAsState
import pt.iade.ei.bestumbrella1.di.AppModule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
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
        val context = LocalContext.current
        val viewModel = remember { AppModule.provideWeatherViewModel(context) }
        val weatherData by viewModel.weatherData.observeAsState()
        val isLoading by viewModel.isLoading.observeAsState(false)
        val error by viewModel.error.observeAsState()
        val hourly by viewModel.hourly.observeAsState(emptyList())
        val daily by viewModel.daily.observeAsState(emptyList())
        val alerts by viewModel.alerts.observeAsState(emptyList())
        val sunriseSunset by viewModel.sunriseSunset.observeAsState(null)
        val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

        // Lisboa como coordenadas padrão
        LaunchedEffect(Unit) {
            viewModel.getWeatherForecast(latitude = 38.7223, longitude = -9.1393)
        }
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

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                if (error != null) {
                    Text(
                        text = error ?: "Erro",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                }

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
                        val tempText = weatherData?.temperature?.let { String.format("%.1f°C", it) } ?: "--°C"
                        val descText = weatherData?.description ?: ""
                        Text("$tempText — $descText", style = MaterialTheme.typography.headlineSmall, color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        val humidityText = weatherData?.humidity?.let { "$it%" } ?: "--%"
                        val windText = weatherData?.windSpeed?.let { String.format("%.1f km/h", it) } ?: "-- km/h"
                        Text("Humidade: $humidityText | Vento: $windText", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)

                        // Nascer/Pôr do sol (hoje)
                        Spacer(Modifier.height(8.dp))
                        val sunriseText = sunriseSunset?.first?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                        val sunsetText = sunriseSunset?.second?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                        Text("Nascer do sol: $sunriseText | Pôr do sol: $sunsetText", style = MaterialTheme.typography.bodySmall, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(20.dp))
                // Previsão 24 horas
                Text("Previsão (24h)", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        hourly.forEach { h ->
                            val hourText = timeFormatter.format(Date(h.dt * 1000))
                            val tempText = String.format("%.0f°C", h.temp)
                            val desc = h.weather.firstOrNull()?.description ?: ""
                            val pop = h.precipitationProbability?.let { String.format("%.0f%%", it * 100) } ?: "--%"
                            Text("$hourText  •  $tempText  •  $desc  •  Chuva: $pop", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                // Previsão 5 dias
                Text("Previsão (5 dias)", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        val dayFormatter = remember { SimpleDateFormat("EEE, dd/MM", Locale("pt", "PT")) }
                        daily.forEach { d ->
                            val dayText = dayFormatter.format(Date(d.dt * 1000))
                            val minMax = String.format("%.0f° / %.0f°", d.temp.min, d.temp.max)
                            val desc = d.weather.firstOrNull()?.description ?: ""
                            val pop = d.precipitationProbability?.let { String.format("%.0f%%", it * 100) } ?: "--%"
                            Text("$dayText  •  $minMax  •  $desc  •  Chuva: $pop", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                // Alertas meteorológicos
                if (alerts.isNotEmpty()) {
                    Text("Alertas", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2))
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            alerts.forEach { a ->
                                val startText = a.start?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                                val endText = a.end?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                                val title = listOfNotNull(a.event, a.sender_name).joinToString(" — ")
                                Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                                Text("De: $startText  Até: $endText", style = MaterialTheme.typography.bodySmall, color = Color.Black)
                                if (!a.description.isNullOrBlank()) {
                                    Spacer(Modifier.height(4.dp))
                                    Text(a.description!!, style = MaterialTheme.typography.bodySmall, color = Color.Black)
                                }
                                Spacer(Modifier.height(10.dp))
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

