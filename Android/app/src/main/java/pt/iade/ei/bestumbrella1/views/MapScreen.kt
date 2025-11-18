package pt.iade.ei.bestumbrella1.views

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.AssistChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptor
import pt.iade.ei.bestumbrella1.views.map.FilterBar
import pt.iade.ei.bestumbrella1.views.map.Station
import pt.iade.ei.bestumbrella1.views.map.StationBottomSheet
import pt.iade.ei.bestumbrella1.views.map.StationFilter
import pt.iade.ei.bestumbrella1.views.map.distanceKm
import pt.iade.ei.bestumbrella1.views.map.umbrellaMarkerIcon
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.width
import androidx.core.content.ContextCompat
import pt.iade.ei.bestumbrella1.di.AppModule
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.BuildConfig
import pt.iade.ei.bestumbrella1.models.UmbrellaData
import kotlin.math.round








@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreenWithMarkers(navController: NavController, focusStation: String? = null) {
    val context = LocalContext.current
    val sessionManager = AppModule.provideSessionManager(context)
    val scope = rememberCoroutineScope()
    var rentalStartMs by remember { mutableStateOf<Long?>(null) }
    var rentalQr by remember { mutableStateOf<String?>(null) }
    var elapsedMs by remember { mutableStateOf(0L) }
    var showEndSheet by remember { mutableStateOf(false) }
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

    LaunchedEffect(focusStation) {
        val target = focusStation?.let { name -> stations.find { it.name.equals(name, ignoreCase = true) } }
        if (target != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(target.location, 16.5f)
            selectedStation = target
        }
    }
    LaunchedEffect(Unit) {
        rentalStartMs = sessionManager.getRentalStartMs()
        rentalQr = sessionManager.getRentalQrCode()
    }
    LaunchedEffect(rentalStartMs) {
        if (rentalStartMs != null) {
            while (rentalStartMs != null) {
                elapsedMs = System.currentTimeMillis() - (rentalStartMs ?: 0L)
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    var hasLocationPermission by remember { mutableStateOf(false) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasLocationPermission = result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }
    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        hasLocationPermission = fine || coarse
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White, contentColor = Color(0xFF1976D2)) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("weather") },
                    icon = {
                        Icon(
                            Icons.Default.Cloud,
                            contentDescription = "Tempo",
                            tint = Color.Black
                        )
                    },
                    label = { Text("Tempo", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("history") },
                    icon = {
                        Icon(
                            Icons.Default.History,
                            contentDescription = "Histórico",
                            tint = Color.Black
                        )
                    },
                    label = { Text("Histórico", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.Map,
                            contentDescription = "Mapa",
                            tint = Color.Black
                        )
                    },
                    label = { Text("Mapa", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("qrscanner") },
                    icon = {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            contentDescription = "Scanner",
                            tint = Color.Black
                        )
                    },
                    label = { Text("Scanner", color = Color.Black, fontWeight = FontWeight.Bold) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.Black
                        )
                    },
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
                properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
                uiSettings = MapUiSettings(myLocationButtonEnabled = hasLocationPermission)
            ) {
                val center = cameraPositionState.position.target
                fun distanceKm(a: LatLng, b: LatLng): Double {
                    val R = 6371.0
                    val dLat = Math.toRadians(b.latitude - a.latitude)
                    val dLon = Math.toRadians(b.longitude - a.longitude)
                    val lat1 = Math.toRadians(a.latitude)
                    val lat2 = Math.toRadians(b.latitude)
                    val aa =
                        Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(
                            dLon / 2
                        ) * Math.sin(dLon / 2)
                    val c = 2 * Math.atan2(Math.sqrt(aa), Math.sqrt(1 - aa))
                    return R * c
                }

                val filtered = when (currentFilter) {
                    StationFilter.ALL -> stations
                    StationFilter.AVAILABLE -> stations.filter { it.available > 0 }
                    StationFilter.NEARBY -> stations.sortedBy { distanceKm(it.location, center) }
                        .take(5)
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
            if (rentalStartMs != null) {
                val totalSeconds = (elapsedMs / 1000).toInt()
                val h = totalSeconds / 3600
                val m = (totalSeconds % 3600) / 60
                val s = totalSeconds % 60
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                        .fillMaxWidth(0.9f),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = Color(0xFF0D47A1))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Uso do guarda-chuva: %02d:%02d:%02d".format(h, m, s),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.weight(1f))
                        AssistChip(
                            onClick = { showEndSheet = true },
                            label = { Text("Terminar", color = Color.Black, fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
            if (showEndSheet && rentalStartMs != null && rentalQr != null) {
                val umbrella = UmbrellaData.findByQrCode(rentalQr!!)
                val totalSeconds = (elapsedMs / 1000).toInt()
                val hDisp = totalSeconds / 3600
                val mDisp = (totalSeconds % 3600) / 60
                val sDisp = totalSeconds % 60
                val minutesRounded = (((elapsedMs + 59999L) / 60000L).toInt()).coerceAtLeast(1)
                val baseFee = 0.30
                fun ratePerMinute(tipo: String?): Double = when (tipo?.lowercase(Locale.ROOT)) {
                    "manual" -> 0.15
                    "compacto" -> 0.15
                    "automático", "automatico" -> 0.15
                    else -> 0.15
                }
                val ratePerMin = ratePerMinute(umbrella?.tipo)
                val amount = round((baseFee + minutesRounded * ratePerMin) * 100) / 100.0

                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                    onDismissRequest = { showEndSheet = false }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Terminar uso", style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Duração: %02d:%02d:%02d".format(hDisp, mDisp, sDisp), color = Color.Black)
                        Text("Desbloqueio: €${"%.2f".format(baseFee)}", color = Color.Black)
                        Text("Tarifa: €${"%.2f".format(ratePerMin)} / minuto", color = Color.Black)
                        Text("Total a pagar: €${"%.2f".format(amount)}", color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                showEndSheet = false
                                val valueStr = String.format(Locale.US, "%.2f", amount)
                                if (BuildConfig.PAYPAL_TEST_MODE) {
                                    scope.launch { sessionManager.stopRental() }
                                    navController.navigate("map")
                                } else {
                                    navController.navigate("paypalCheckout/${valueStr}/${rentalQr!!}/end")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Text("Pagar e terminar", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showEndSheet = false },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Cancelar", fontWeight = FontWeight.Bold) }
                        Spacer(Modifier.height(8.dp))
                        Text("Nota: após 24h poderá aplicar-se multa.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
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
                    StationBottomSheet(station)
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
