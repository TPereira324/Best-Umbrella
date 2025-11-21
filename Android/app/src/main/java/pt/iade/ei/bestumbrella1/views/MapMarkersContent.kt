package pt.iade.ei.bestumbrella1.views

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.views.map.Station
import pt.iade.ei.bestumbrella1.views.map.StationBottomSheet
import pt.iade.ei.bestumbrella1.views.map.StationFilter
import pt.iade.ei.bestumbrella1.views.map.umbrellaMarkerIcon
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapMarkersContent(
    navController: NavController,
    focusStation: String? = null,
    selectedStation: Station?,
    onSelectStation: (Station?) -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val sessionManager = pt.iade.ei.bestumbrella1.di.AppModule.provideSessionManager(context)
    var hasLocationPermission by remember { mutableStateOf(false) }
    val locationPermissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        hasLocationPermission = result[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        val fine = androidx.core.content.ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val coarse = androidx.core.content.ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        hasLocationPermission = fine || coarse
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    val lisboaCenter = LatLng(38.7682, -9.0985)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lisboaCenter, 14.8f)
    }

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

    androidx.compose.runtime.LaunchedEffect(focusStation) {
        val target =
            focusStation?.let { name -> stations.find { it.name.equals(name, ignoreCase = true) } }
        if (target != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(target.location, 16.5f)
            onSelectStation(target)
        }
    }

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
                onClick = {
                    onSelectStation(station)
                    true
                }
            )
        }
    }

    selectedStation?.let { station ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onSelectStation(null) }
        ) {
            StationBottomSheet(station)
            androidx.compose.foundation.layout.Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    onSelectStation(null)
                    scope.launch { sessionManager.startRental("MAP") }
                    navController.navigate("map")
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                )
            ) {
                Text("Reservar Guarda-chuva")
            }
        }
    }
}