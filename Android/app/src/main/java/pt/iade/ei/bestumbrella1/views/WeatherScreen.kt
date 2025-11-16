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
import androidx.compose.ui.graphics.vector.ImageVector
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
    val currentWeatherId by viewModel.currentWeatherId.observeAsState(null)
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        // Atualização periódica para refletir ícones e dados em tempo real
        val lat = 38.7223
        val lon = -9.1393
        while (true) {
            viewModel.getWeatherForecast(latitude = lat, longitude = lon)
            kotlinx.coroutines.delay(5 * 60 * 1000) // 5 minutos
        }
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
                        // Animação de chuva somente quando está REALMENTE a chover
                        val currentId = currentWeatherId ?: 0
                        val isRaining = (currentId in 200..599) || (currentId in 600..622)
                        val isCloudy = !isRaining && (currentId in 801..804)
                        WeatherAnimatedIcon(isRaining = isRaining, isCloudy = isCloudy)
                        Text("Lisboa, Portugal", style = MaterialTheme.typography.titleLarge, color = Color.Black, fontWeight = FontWeight.Bold)
                        val tempText = weatherData?.temperature?.let { String.format("%.1f°C", it) } ?: "--°C"
                        val descText = weatherData?.description ?: ""
                        Text("$tempText — $descText", style = MaterialTheme.typography.headlineMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        val humidityText = weatherData?.humidity?.let { "$it%" } ?: "--%"
                        val windText = weatherData?.windSpeed?.let { String.format("%.1f km/h", it) } ?: "-- km/h"
                        val popText = if (hourly.isNotEmpty()) {
                            val p = hourly.first().precipitationProbability
                            p?.let { String.format(Locale.getDefault(), "%.0f%%", it * 100) }
                        } else null

                        // Linha de chips: humidade, vento, chuva agora
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoChip(text = "Humidade $humidityText", icon = Icons.Default.WaterDrop)
                            InfoChip(text = "Vento $windText", icon = Icons.Default.Air)
                            if (!popText.isNullOrBlank()) {
                                InfoChip(text = "Chuva ${popText}", icon = Icons.Default.WaterDrop)
                            }
                        }

                        // Linha de chips: nascer/pôr do sol
                        Spacer(Modifier.height(8.dp))
                        val sunriseText = sunriseSunset?.first?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                        val sunsetText = sunriseSunset?.second?.let { timeFormatter.format(Date(it * 1000)) } ?: "--:--"
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoChip(text = "Nascer $sunriseText", icon = Icons.Default.WbSunny)
                            InfoChip(text = "Pôr $sunsetText", icon = Icons.Default.Brightness3)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                // Previsão 24 horas (horizontal, estilo chips)
                Text("Previsão (24h)", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    androidx.compose.foundation.lazy.LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(hourly.size) { index ->
                            val h = hourly[index]
                            val hourText = timeFormatter.format(Date(h.dt * 1000))
                            val tempText = String.format("%.0f°", h.temp)
                            val pop = h.precipitationProbability?.let { String.format("%.0f%%", it * 100) } ?: "--%"
                            val wid = h.weather.firstOrNull()?.id ?: 0
                            val icon = when {
                                wid in 200..599 || wid in 600..622 || (h.precipitationProbability ?: 0.0) >= 0.3 -> Icons.Default.WaterDrop
                                wid in 801..804 -> Icons.Default.Cloud
                                else -> Icons.Default.WbSunny
                            }
                            HourChip(hourText = hourText, tempText = tempText, popText = pop, icon = icon)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                // Previsão 5 dias (lista limpa)
                Text("Previsão (5 dias)", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    val dayFormatter = remember { SimpleDateFormat("EEE, dd/MM", Locale("pt", "PT")) }
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        daily.forEach { d ->
                            val dayText = dayFormatter.format(Date(d.dt * 1000))
                            val minMax = String.format("%.0f° / %.0f°", d.temp.min, d.temp.max)
                            val pop = d.precipitationProbability?.let { String.format("%.0f%%", it * 100) } ?: "--%"
                            val descLower = d.weather.firstOrNull()?.description?.lowercase(Locale.getDefault()) ?: ""
                            val icon = when {
                                descLower.contains("chuva") || (d.precipitationProbability ?: 0.0) >= 0.3 -> Icons.Default.WaterDrop
                                descLower.contains("nublado") || descLower.contains("nuvens") || descLower.contains("encoberto") -> Icons.Default.Cloud
                                else -> Icons.Default.WbSunny
                            }
                            Row(
                                Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(icon, contentDescription = null, tint = Color(0xFF1976D2))
                                    Spacer(Modifier.width(8.dp))
                                    Text(dayText, style = MaterialTheme.typography.bodyMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(minMax, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                                    Spacer(Modifier.width(12.dp))
                                    Text(pop, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                                }
                            }
                            Divider(color = Color.Black.copy(alpha = 0.08f))
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

@Composable
private fun HourChip(
    hourText: String,
    tempText: String,
    popText: String,
    icon: ImageVector
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF1976D2))
            Column {
                Text(hourText, style = MaterialTheme.typography.labelMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(tempText, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                    Text(popText, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InfoChip(text: String, icon: ImageVector) {
    AssistChip(
        onClick = {},
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = Color(0xFF1976D2))
        },
        label = { Text(text, color = Color.Black, fontWeight = FontWeight.Bold) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.White
        )
    )
}