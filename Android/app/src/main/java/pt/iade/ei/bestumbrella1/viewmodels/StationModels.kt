package pt.iade.ei.bestumbrella1.views.map

import com.google.android.gms.maps.model.LatLng

data class Station(
    val name: String,
    val location: LatLng,
    val available: Int,
    val total: Int
)

enum class StationFilter { ALL, AVAILABLE, NEARBY }