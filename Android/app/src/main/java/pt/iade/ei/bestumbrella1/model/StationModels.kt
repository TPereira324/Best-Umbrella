package pt.iade.ei.bestumbrella1.model

import com.google.android.gms.maps.model.LatLng

data class Station(
    val name: String,
    val location: LatLng,
    val available: Int,
    val total: Int
)

enum class StationFilter { ALL, AVAILABLE, NEARBY }

object StationData {
    val stations: List<Station> = listOf(
        Station("IADE", LatLng(38.7818, -9.10251), 3, 6),
        Station("Parque das Nações", LatLng(38.76800, -9.09400), 6, 10),
        Station("Metro Moscavide", LatLng(38.77639, -9.10169), 8, 10),
        Station("Metro Oriente", LatLng(38.76784, -9.09935), 4, 8),
        Station("Terreiro do Paço", LatLng(38.70667, -9.13528), 10, 15),
        Station("Baixa-Chiado", LatLng(38.71056, -9.14000), 8, 12),
        Station("Marquês de Pombal", LatLng(38.724686, -9.150442), 12, 20),
        Station("Rossio", LatLng(38.713718, -9.139681), 7, 12),
    )

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
}
