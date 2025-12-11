package pt.iade.ei.bestumbrella1.view

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.model.Station
import pt.iade.ei.bestumbrella1.model.StationFilter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapMarkersContent(
    focusStation: String? = null,
    selectedStation: Station?,
    onSelectStation: (Station?) -> Unit,
    onReserved: () -> Unit,
) {
    val context = LocalContext.current
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val sessionManager = rememberSessionManager()
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
    val stationsRepo = rememberStationsRepository()
    var stations by remember { mutableStateOf(stationsRepo.getStations()) }

    androidx.compose.runtime.LaunchedEffect(focusStation) {
        val target =
            focusStation?.let { name -> stations.find { it.name.equals(name, ignoreCase = true) } }
        if (target != null) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(target.location, 16.5f)
            onSelectStation(target)
        }
    }

    val mapProperties = remember(hasLocationPermission) {
        MapProperties(isMyLocationEnabled = hasLocationPermission)
    }
    val uiSettings = remember(hasLocationPermission) {
        MapUiSettings(myLocationButtonEnabled = hasLocationPermission)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        uiSettings = uiSettings
    ) {
        val center = cameraPositionState.position.target

        val filtered = when (currentFilter) {
            StationFilter.ALL -> stations
            StationFilter.AVAILABLE -> stations.filter { it.available > 0 }
            StationFilter.NEARBY -> stationsRepo.nearbyStations(center)
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
                    scope.launch { sessionManager.startRental("MAP") }
                    val updatedList = stations.map {
                        if (it.name == station.name && it.available > 0) it.copy(available = it.available - 1) else it
                    }
                    stations = updatedList
                    updatedList.find { it.name == station.name } ?: station
                    onReserved()
                    onSelectStation(null)
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
