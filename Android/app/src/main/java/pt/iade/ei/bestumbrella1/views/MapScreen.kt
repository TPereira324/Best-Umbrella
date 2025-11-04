package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

data class Station(
    val name: String,
    val location: LatLng,
    val available: Int,
    val total: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenWithMarkers(navController: NavController) {
    val context = LocalContext.current
    val lisboaCenter = LatLng(38.7682, -9.0985)

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
                    FilterChip(selected = true, onClick = {}, label = { Text("Todas", color = Color.Black, fontWeight = FontWeight.Bold) })
                    FilterChip(selected = false, onClick = {}, label = { Text("Disponíveis", color = Color.Black, fontWeight = FontWeight.Bold) })
                    FilterChip(selected = false, onClick = {}, label = { Text("Próximas", color = Color.Black, fontWeight = FontWeight.Bold) })
                }
                Text(
                    text = "🟢 Localização ativa",
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
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
                cameraPositionState = cameraPositionState
            ) {
                stations.forEach { station ->
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
                        anchor = Offset(0.5f, 1.0f)
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
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun PreviewMapScreenWithMarkers() {
    val navController = rememberNavController()
    MapScreenWithMarkers(navController)
}

// Gera um BitmapDescriptor com um círculo colorido e o emoji de guarda-chuva no centro
private fun umbrellaMarkerIcon(context: android.content.Context, available: Boolean): BitmapDescriptor {
    val density = context.resources.displayMetrics.density
    val sizePx = (48 * density).toInt()
    val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = (if (available) Color(0xFF1976D2) else Color(0xFF9E9E9E)).toArgb()
    }
    // Fundo circular
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
