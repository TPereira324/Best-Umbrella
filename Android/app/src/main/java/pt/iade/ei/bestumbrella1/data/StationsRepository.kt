package pt.iade.ei.bestumbrella1.data

import com.google.android.gms.maps.model.LatLng
import pt.iade.ei.bestumbrella1.model.Station
import pt.iade.ei.bestumbrella1.model.StationData

class StationsRepository {
    fun getStations(): List<Station> = StationData.stations

    fun nearbyStations(center: LatLng, limit: Int = 5): List<Station> {
        return StationData.stations.sortedBy { StationData.distanceKm(it.location, center) }
            .take(limit)
    }
}
