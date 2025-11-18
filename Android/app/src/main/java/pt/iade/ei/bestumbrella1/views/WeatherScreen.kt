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
import pt.iade.ei.bestumbrella1.views.weather.HourChip
import pt.iade.ei.bestumbrella1.views.weather.InfoChip
import pt.iade.ei.bestumbrella1.di.AppModule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
        val lat = 38.7223
        val lon = -9.1393
        while (true) {
            viewModel.getWeatherForecast(latitude = lat, longitude = lon)
            kotlinx.coroutines.delay(5 * 60 * 1000)
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

                        val currentId = currentWeatherId ?: 0
                        val isRaining = (currentId in 200..599) || (currentId in 600..622)
                        val isCloudy = !isRaining && (currentId in 801..804)
                        val nowSec = System.currentTimeMillis() / 1000
                        val sr = sunriseSunset?.first ?: 0
                        val ss = sunriseSunset?.second ?: 0
                        val isNight = sr > 0 && ss > 0 && (nowSec < sr || nowSec >= ss)
                        WeatherAnimatedIcon(isRaining = isRaining, isCloudy = isCloudy, isNight = isNight)
                        Text("Lisboa, Portugal", style = MaterialTheme.typography.titleLarge, color = Color.Black, fontWeight = FontWeight.Bold)
                        val tempText = weatherData?.temperature?.let { String.format("%.1f°C", it) } ?: "--°C"
                        val descText = weatherData?.description ?: ""
                        Text(tempText, style = MaterialTheme.typography.headlineLarge, color = Color.Black, fontWeight = FontWeight.Bold)
                        Text(descText, style = MaterialTheme.typography.titleMedium, color = Color.Black)
                        Spacer(Modifier.height(8.dp))
                        val humidityText = weatherData?.humidity?.let { "$it%" } ?: "--%"
                        val windText = weatherData?.windSpeed?.let { String.format("%.1f km/h", it) } ?: "-- km/h"
                        val popText = if (hourly.isNotEmpty()) {
                            val p = hourly.first().precipitationProbability
                            p?.let { String.format(Locale.getDefault(), "%.0f%%", it * 100) }
                        } else null


                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoChip(text = "Humidade $humidityText", icon = Icons.Default.WaterDrop)
                            InfoChip(text = "Vento $windText", icon = Icons.Default.Air)
                            if (!popText.isNullOrBlank()) {
                                InfoChip(text = "Chuva ${popText}", icon = Icons.Default.WaterDrop)
                            }
                        }


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

                Text("Previsão (5 dias)", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    val dayFormatter = remember { SimpleDateFormat("EEE, dd/MM", Locale("pt", "PT")) }
                    val todayCal = java.util.Calendar.getInstance()
                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                        daily.forEach { d ->
                            val date = Date(d.dt * 1000)
                            val cal = java.util.Calendar.getInstance()
                            cal.time = date
                            val isSameDay = cal.get(java.util.Calendar.YEAR) == todayCal.get(java.util.Calendar.YEAR) && cal.get(java.util.Calendar.DAY_OF_YEAR) == todayCal.get(java.util.Calendar.DAY_OF_YEAR)
                            val dayText = if (isSameDay) "Hoje" else dayFormatter.format(date)
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
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewWeatherScreen() {
    val navController = rememberNavController()
    WeatherScreen(navController)
}

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