package pt.iade.ei.bestumbrella1.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pt.iade.ei.bestumbrella1.R
import pt.iade.ei.bestumbrella1.models.RentalStation
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    
    private val rentalStations = listOf(
        RentalStation(1, "IADE", 38.7818, -9.10251, 3, 6),
        RentalStation(2, "Parque das Nações", 38.76800, -9.09400, 6, 10),
        RentalStation(3, "Metro Moscavide", 38.77639, -9.10169, 8, 10),
        RentalStation(4, "Metro Oriente", 38.76784, -9.09935, 4, 8),
        RentalStation(1, "Terreiro do Paço", 38.70667, -9.13528, 10, 15),
        RentalStation(3, "Rossio", 38.713718, -9.139681, 7, 12),
        RentalStation(4, "Marquês de Pombal", 38.724686, -9.150442, 12, 20)


    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        findViewById<FloatingActionButton>(R.id.fab_my_location).setOnClickListener {
            getDeviceLocation()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        addRentalStationsToMap()
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val currentLatLng = LatLng(it.latitude, it.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    } ?: run {
                        val lisbonLatLng = LatLng(38.7223, -9.1393)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lisbonLatLng, 12f))
                    }
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Erro ao obter localização", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addRentalStationsToMap() {
        Log.d("MapActivity", "Adicionando ${rentalStations.size} estações ao mapa")
        rentalStations.forEach { station ->
            val position = LatLng(station.latitude, station.longitude)
            Log.d("MapActivity", "Estação: ${station.name}, Posição: ${station.latitude}, ${station.longitude}, Guarda-chuvas: ${station.availableUmbrellas}/${station.totalCapacity}")
            
            val snippet = "Disponíveis: ${station.availableUmbrellas}/${station.totalCapacity}\n" +
                    String.format(Locale.US, "Lat: %.5f | Lng: %.5f", station.latitude, station.longitude)
            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(station.name)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_rental_station))
            )
        }
        Log.d("MapActivity", "Todas as estações foram adicionadas ao mapa")
    }

    @RequiresPermission(anyOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                    map.isMyLocationEnabled = true
                    getDeviceLocation()
                }
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
                val lisbonLatLng = LatLng(38.7223, -9.1393)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(lisbonLatLng, 12f))
            }
        }
    }
}