package pt.iade.ei.bestumbrella1.network

data class WeatherResponse(
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val rainProbability: Double,
    val isSuccessful: Boolean = true
)

