package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.livedata.observeAsState
import pt.iade.ei.bestumbrella1.di.AppModule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: pt.iade.ei.bestumbrella1.viewmodels.WeatherViewModel = remember { AppModule.provideWeatherViewModel(context) }
    val weatherData by viewModel.weatherData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val hourly by viewModel.hourly.observeAsState(emptyList())
    val daily by viewModel.daily.observeAsState(emptyList())
    val sunriseSunset by viewModel.sunriseSunset.observeAsState(null)
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        viewModel.getWeatherForecast(latitude = 38.7223, longitude = -9.1393)
    }
    Scaffold(
        bottomBar = { AppBottomNavigationBar(navController) }
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
                    .verticalScroll(rememberScrollState())
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

                error?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ícone dinâmico conforme condição atual
                        val descLower = (weatherData?.description ?: "").lowercase(Locale.getDefault())
                        val currentPop = hourly.firstOrNull()?.precipitationProbability ?: 0.0
                        val isRaining = currentPop >= 0.3 ||
                                descLower.contains("chuva") ||
                                descLower.contains("aguace") ||
                                descLower.contains("chuvisco") ||
                                descLower.contains("tempest") ||
                                descLower.contains("trovo")
                        val isCloudy = !isRaining && (
                                descLower.contains("nublado") ||
                                descLower.contains("nuvens") ||
                                descLower.contains("encoberto") ||
                                descLower.contains("nuvens dispersas") ||
                                descLower.contains("poucas nuvens")
                            )
                        WeatherAnimatedIcon(isRaining = isRaining, isCloudy = isCloudy)
                        Text("Lisboa, Portugal", style = MaterialTheme.typography.titleLarge, color = Color.Black, fontWeight = FontWeight.Bold)
                        val tempText = weatherData?.temperature?.let { String.format("%.1f°C", it) } ?: "--°C"
                        val descText = weatherData?.description ?: ""
                        Text("$tempText — $descText", style = MaterialTheme.typography.headlineMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        val humidityText = weatherData?.humidity?.let { "$it%" } ?: "--%"
                        val windText = weatherData?.windSpeed?.let { String.format("%.1f km/h", it) } ?: "-- km/h"
                        Text("Humidade: $humidityText | Vento: $windText", style = MaterialTheme.typography.titleSmall, color = Color.Black, fontWeight = FontWeight.Bold)

                        // Probabilidade de precipitação atual (se disponível) para dar contexto real
                        val popText = if (hourly.isNotEmpty()) {
                            val p = hourly.first().precipitationProbability
                            p?.let { String.format(Locale.getDefault(), "Chuva agora: %.0f%%", it * 100) }
                        } else null
                        if (!popText.isNullOrBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(popText!!, style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.height(8.dp))
                        val sunriseText = sunriseSunset?.first?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                        val sunsetText = sunriseSunset?.second?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                        Text("Nascer do sol: $sunriseText | Pôr do sol: $sunsetText", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(20.dp))
                // Previsão 24 horas
                Text("Previsão (24h)", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        hourly.forEach { h ->
                            val hourText = timeFormatter.format(Date(h.dt * 1000))
                            val tempText = String.format("%.0f°C", h.temp)
                            val desc = h.weather.firstOrNull()?.description ?: ""
                            val pop = h.precipitationProbability?.let { String.format("%.0f%%", it * 100) } ?: "--%"
                            Text("$hourText  •  $tempText  •  $desc  •  Chuva: $pop", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                // Previsão 5 dias
                Text("Previsão (5 dias)", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        val dayFormatter = remember { SimpleDateFormat("EEE, dd/MM", Locale("pt", "PT")) }
                        daily.forEach { d ->
                            val dayText = dayFormatter.format(Date(d.dt * 1000))
                            val minMax = String.format("%.0f° / %.0f°", d.temp.min, d.temp.max)
                            val desc = d.weather.firstOrNull()?.description ?: ""
                            val pop = d.precipitationProbability?.let { String.format("%.0f%%", it * 100) } ?: "--%"
                            Text("$dayText  •  $minMax  •  $desc  •  Chuva: $pop", style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                
            }
            // Rodapé de créditos movido para o Scaffold global
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWeatherScreen() {
    val navController = rememberNavController()
    WeatherScreen(navController)
}

// Pequeno rodapé de créditos do projeto
@Composable
private fun ProjectCreditsFooter() {
    Text(
        text = "Projeto desenvolvido por alunos do 2º ano da Universidade Europeia, Portugal",
        style = MaterialTheme.typography.labelSmall,
        color = Color.Black.copy(alpha = 0.75f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

// Ícones animados para estados de tempo: sol, chuva, nuvens
@Composable
private fun WeatherAnimatedIcon(isRaining: Boolean, isCloudy: Boolean) {
    val size = 80.dp
    when {
        isRaining -> RainAnimatedIcon(size)
        isCloudy -> CloudAnimatedIcon(size)
        else -> SunAnimatedIcon(size)
    }
}

@Composable
private fun SunAnimatedIcon(size: androidx.compose.ui.unit.Dp) {
    val transition = rememberInfiniteTransition(label = "sun")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sun-rot"
    )
    val scale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sun-scale"
    )
    Icon(
        Icons.Default.WbSunny,
        contentDescription = null,
        tint = Color(0xFFFFC107),
        modifier = Modifier.size(size).rotate(rotation).scale(scale)
    )
}

@Composable
private fun CloudAnimatedIcon(size: androidx.compose.ui.unit.Dp) {
    val transition = rememberInfiniteTransition(label = "cloud")
    val dx by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cloud-dx"
    )
    Box(modifier = Modifier.size(size)) {
        Icon(
            Icons.Default.Cloud,
            contentDescription = null,
            tint = Color(0xFF90A4AE),
            modifier = Modifier
                .fillMaxSize()
                .offset(x = dx.dp)
        )
    }
}

@Composable
private fun RainAnimatedIcon(sizeDp: androidx.compose.ui.unit.Dp) {
    val transition = rememberInfiniteTransition(label = "rain")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rain-progress"
    )
    // Pre-gerar posições horizontais dos pingos para estabilidade
    val drops = remember {
        List(14) { Random.nextFloat() } // 0..1 relativo à largura
    }
    Box(modifier = Modifier.size(sizeDp)) {
        // Pingos de chuva a cair (desenhados primeiro, abaixo da nuvem)
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height
            val cloudBottomY = h * 0.48f
            val dropLen = h * 0.22f
            val strokeWidth = kotlin.math.min(w, h) * 0.02f
            drops.forEachIndexed { idx, xRel ->
                val x = xRel * w
                // iniciar os pingos a partir da base da nuvem
                val travel = (h - cloudBottomY) + dropLen
                val yStart = (progress * travel - idx * (dropLen / 3)) % travel + cloudBottomY
                val start = Offset(x, yStart - dropLen)
                val end = Offset(x, yStart)
                drawLine(
                    color = Color(0xFF2196F3),
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth
                )
            }
        }
        // Nuvem por cima
        Icon(
            Icons.Default.Cloud,
            contentDescription = null,
            tint = Color(0xFF90A4AE),
            modifier = Modifier.fillMaxSize()
        )
    }
}