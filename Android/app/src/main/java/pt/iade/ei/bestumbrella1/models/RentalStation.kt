package pt.iade.ei.bestumbrella1.models

data class RentalStation(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val availableUmbrellas: Int,
    val totalCapacity: Int
)

