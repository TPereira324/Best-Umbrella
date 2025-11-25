package pt.iade.ei.bestumbrella1.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness3
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import pt.iade.ei.bestumbrella1.di.AppModule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(navController: NavController) {
    val context = LocalContext.current
    val controller: pt.iade.ei.bestumbrella1.controllers.WeatherController =
        remember { AppModule.provideWeatherController(context) }
    val weatherData by controller.weatherData.observeAsState()
    val isLoading by controller.isLoading.observeAsState(false)
    val error by controller.error.observeAsState()
    val hourly by controller.hourly.observeAsState(emptyList())
    val daily by controller.daily.observeAsState(emptyList())
    val sunriseSunset by controller.sunriseSunset.observeAsState(null)
    val currentWeatherId by controller.currentWeatherId.observeAsState(null)
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    LaunchedEffect(Unit) {
        val lat = 38.7223
        val lon = -9.1393
        while (true) {
            controller.getWeatherForecast(latitude = lat, longitude = lon)
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
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                        WeatherAnimatedIcon(
                            isRaining = isRaining,
                            isCloudy = isCloudy,
                            isNight = isNight
                        )
                        Text(
                            "Lisboa, Portugal",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        val tempText =
                            weatherData?.temperature?.let { String.format("%.1f°C", it) } ?: "--°C"
                        val descText = weatherData?.description ?: ""
                        Text(
                            tempText,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            descText,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(8.dp))
                        weatherData?.humidity?.let { "$it%" } ?: "--%"
                        weatherData?.windSpeed?.let { String.format("%.1f km/h", it) }
                            ?: "-- km/h"
                        if (hourly.isNotEmpty()) {
                            val p = hourly.first().precipitationProbability
                            p?.let { String.format(Locale.getDefault(), "%.0f%%", it * 100) }
                        } else null


                        Spacer(Modifier.height(10.dp))


                        Spacer(Modifier.height(8.dp))
                        val sunriseText =
                            sunriseSunset?.first?.let { timeFormatter.format(Date(it * 1000)) }
                                ?: "--:--"
                        val sunsetText =
                            sunriseSunset?.second?.let { timeFormatter.format(Date(it * 1000)) }
                                ?: "--:--"
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            InfoChip(
                                text = "Nascer $sunriseText",
                                icon = Icons.Default.WbSunny,
                                tint = Color(0xFFFFC107)
                            )
                            InfoChip(text = "Pôr $sunsetText", icon = Icons.Default.Brightness3)
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "Previsão (24h)",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    androidx.compose.foundation.lazy.LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(hourly.size) { index ->
                            val h = hourly[index]
                            val hourText = timeFormatter.format(Date(h.dt * 1000))
                            val tempText = String.format("%.0f°", h.temp)
                            val pop = h.precipitationProbability?.let {
                                String.format(
                                    "%.0f%%",
                                    it * 100
                                )
                            } ?: "--%"
                            val wid = h.weather.firstOrNull()?.id ?: 0
                            val icon = when {
                                wid in 200..599 || wid in 600..622 || (h.precipitationProbability
                                    ?: 0.0) >= 0.3 -> Icons.Default.WaterDrop

                                wid in 801..804 -> Icons.Default.Cloud
                                else -> Icons.Default.WbSunny
                            }
                            val chipTint =
                                if (wid in 801..804) Color(0xFF90A4AE) else Color(0xFF1976D2)
                            HourChip(
                                hourText = hourText,
                                tempText = tempText,
                                popText = pop,
                                icon = icon,
                                tint = chipTint
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    "Previsão (5 dias)",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    val dayFormatter =
                        remember { SimpleDateFormat("EEE, dd/MM", Locale("pt", "PT")) }
                    val todayCal = java.util.Calendar.getInstance()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        daily.forEach { d ->
                            val date = Date(d.dt * 1000)
                            val cal = java.util.Calendar.getInstance()
                            cal.time = date
                            val isSameDay =
                                cal.get(java.util.Calendar.YEAR) == todayCal.get(java.util.Calendar.YEAR) && cal.get(
                                    java.util.Calendar.DAY_OF_YEAR
                                ) == todayCal.get(java.util.Calendar.DAY_OF_YEAR)
                            val dayText = if (isSameDay) "Hoje" else dayFormatter.format(date)
                            val minMax = String.format("%.0f° / %.0f°", d.temp.min, d.temp.max)
                            val pop = d.precipitationProbability?.let {
                                String.format(
                                    "%.0f%%",
                                    it * 100
                                )
                            } ?: "--%"
                            val descLower =
                                d.weather.firstOrNull()?.description?.lowercase(Locale.getDefault())
                                    ?: ""
                            val icon = when {
                                descLower.contains("chuva") || (d.precipitationProbability
                                    ?: 0.0) >= 0.3 -> Icons.Default.WaterDrop

                                descLower.contains("nublado") || descLower.contains("nuvens") || descLower.contains(
                                    "encoberto"
                                ) -> Icons.Default.Cloud

                                else -> Icons.Default.WbSunny
                            }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    val dayTint = if (
                                        descLower.contains("nublado") || descLower.contains("nuvens") || descLower.contains(
                                            "encoberto"
                                        )
                                    ) Color(0xFF90A4AE) else Color(0xFF1976D2)
                                    Icon(icon, contentDescription = null, tint = dayTint)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        dayText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        minMax,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Black
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        pop,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF1976D2),
                                        fontWeight = FontWeight.Bold
                                    )
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

