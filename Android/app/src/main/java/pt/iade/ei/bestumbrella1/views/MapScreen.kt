package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
 

data class Station(
    val name: String,
    val location: LatLng,
    val available: Int,
    val total: Int
)

private enum class StationFilter { ALL, AVAILABLE, NEARBY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenWithMarkers(navController: NavController) {
    val context = LocalContext.current
    val lisboaCenter = LatLng(38.7682, -9.0985)
    var selectedStation by remember { mutableStateOf<Station?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    

    
    
    

    

    var currentFilter by remember { mutableStateOf(StationFilter.ALL) }

    val stations = listOf(
        Station("IADE", LatLng(38.7818, -9.10251), 3, 6),
        Station("Parque das Nações", LatLng(38.76800, -9.09400), 6, 10),
        Station("Metro Moscavide", LatLng(38.77639, -9.10169), 8, 10),
        Station("Metro Oriente", LatLng(38.76784, -9.09935), 4, 8),
        Station("Terreiro do Paço", LatLng(38.70667, -9.13528), 10, 15),
        Station("Baixa-Chiado", LatLng(38.71056, -9.14000), 8, 12),
        Station("Marquês de Pombal", LatLng(38.724686, -9.150442), 12, 20),
        Station("Rossio", LatLng(38.713718, -9.139681), 7, 12),

        )
        val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lisboaCenter, 14.8f)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Best Umbrella ☂️", color = Color.Black, fontWeight = FontWeight.Bold) },

                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFBBDEFB))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val totalCount = stations.size
                    val availableCount = stations.count { it.available > 0 }
                    val center = cameraPositionState.position.target
                    fun distanceKm(a: LatLng, b: LatLng): Double {
                        val R = 6371.0
                        val dLat = Math.toRadians(b.latitude - a.latitude)
                        val dLon = Math.toRadians(b.longitude - a.longitude)
                        val lat1 = Math.toRadians(a.latitude)
                        val lat2 = Math.toRadians(b.latitude)
                        val aa = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
                        val c = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1 - aa))
                        return R * c
                    }
                    val nearbyStations = remember(stations, center) {
                        stations.sortedBy { distanceKm(it.location, center) }.take(5)
                    }
                    val nearbyCount = nearbyStations.size

                    FilterChip(
                        selected = currentFilter == StationFilter.ALL,
                        onClick = { currentFilter = StationFilter.ALL },
                        label = { Text("Todas ($totalCount)", color = Color.Black, fontWeight = FontWeight.Bold) }
                    )
                    FilterChip(
                        selected = currentFilter == StationFilter.AVAILABLE,
                        onClick = { currentFilter = StationFilter.AVAILABLE },
                        label = { Text("Disponíveis ($availableCount)", color = Color.Black, fontWeight = FontWeight.Bold) }
                    )
                    FilterChip(
                        selected = currentFilter == StationFilter.NEARBY,
                        onClick = { currentFilter = StationFilter.NEARBY },
                        label = { Text("Próximas ($nearbyCount)", color = Color.Black, fontWeight = FontWeight.Bold) }
                    )
                }
                
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, contentColor = Color(0xFF1976D2)) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Map, contentDescription = "Mapa", tint = Color.Black) },
                    label = { Text("Mapa", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("qrscanner") },
                    icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "Scanner", tint = Color.Black) },
                    label = { Text("Scanner", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("weather") },
                    icon = { Icon(Icons.Default.Cloud, contentDescription = null, tint = Color.Black) },
                    label = { Text("Tempo", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Histórico", tint = Color.Black) },
                    label = { Text("Histórico", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color.Black) },
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

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(),
                uiSettings = MapUiSettings()
            ) {
                val center = cameraPositionState.position.target
                fun distanceKm(a: LatLng, b: LatLng): Double {
                    val R = 6371.0
                    val dLat = Math.toRadians(b.latitude - a.latitude)
                    val dLon = Math.toRadians(b.longitude - a.longitude)
                    val lat1 = Math.toRadians(a.latitude)
                    val lat2 = Math.toRadians(b.latitude)
                    val aa = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2)
                    val c = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1 - aa))
                    return R * c
                }
                val filtered = when (currentFilter) {
                    StationFilter.ALL -> stations
                    StationFilter.AVAILABLE -> stations.filter { it.available > 0 }
                    StationFilter.NEARBY -> stations.sortedBy { distanceKm(it.location, center) }.take(5)
                }
                filtered.forEach { station ->
                    val snippet = "Disponíveis: ${station.available}/${station.total}\n" +
                            String.format(
                                Locale.US,
                                "Lat: %.5f | Lng: %.5f",
                                station.location.latitude,
                                station.location.longitude
                            )
                    val iconDescriptor = umbrellaMarkerIcon(
                        context = context,
                        available = station.available > 0
                    )
                    Marker(
                        state = MarkerState(position = station.location),
                        title = station.name,
                        snippet = snippet,
                        icon = iconDescriptor,
                        anchor = Offset(0.5f, 1.0f),
                        onClick = {
                            selectedStation = station
                            true
                        }
                    )
                }
            }
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("qrscanner") },
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "Scanner") },
                    text = { Text("Scanner") },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .shadow(8.dp, shape = MaterialTheme.shapes.medium)
                )
            

            selectedStation?.let { station ->
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { selectedStation = null }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = station.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = "${station.available} disponíveis",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            )
                        }

                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = String.format(
                                Locale.US,
                                "\uD83D\uDCCD %.1f km de distância",
                                0.3
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )

                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Informações da Estação",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("☂ Guarda-chuvas", color = Color.Black, fontWeight = FontWeight.Bold)
                                    Text("${station.available} de ${station.total}", color = Color.Black)
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("⏱ Tempo máximo", color = Color.Black, fontWeight = FontWeight.Bold)
                                    Text("24 horas", color = Color.Black)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "⚠️ Multa aplicada após 24h",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("€ Tarifa", color = Color.Black, fontWeight = FontWeight.Bold)
                                    Text("€0.50/hora", color = Color.Black)
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Como funciona",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp)) {
                                Text("1. Reserve um guarda-chuva nesta estação", color = Color.Black)
                                Text("2. Desbloqueie usando o código QR", color = Color.Black)
                                Text("3. Use durante o tempo necessário", color = Color.Black)
                                Text("4. Devolva em qualquer estação Best Umbrella", color = Color.Black)
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                selectedStation = null
                                navController.navigate("payment")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0D47A1),
                                contentColor = Color.White
                            )
                        ) {
                            Text("Reservar Guarda-chuva")
                        }
                    }
                }
            }
        }
    }
    }


@Preview(showBackground = true)
@Composable
fun PreviewMapScreenWithMarkers() {
    val navController = rememberNavController()
    MapScreenWithMarkers(navController)
}
private fun umbrellaMarkerIcon(context: android.content.Context, available: Boolean): BitmapDescriptor {
    val density = context.resources.displayMetrics.density
    val sizePx = (48 * density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = (if (available) Color(0xFF1976D2) else Color(0xFF9E9E9E)).toArgb()
    }

    val radius = sizePx / 2f
    canvas.drawCircle(radius, radius, radius, bgPaint)

    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = sizePx * 0.6f
    }
    val fm = textPaint.fontMetrics
    val textCenterY = sizePx / 2f - (fm.ascent + fm.descent) / 2f
    canvas.drawText("☂", sizePx / 2f, textCenterY, textPaint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
