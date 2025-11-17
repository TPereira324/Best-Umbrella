package pt.iade.ei.bestumbrella1.network

import com.google.gson.annotations.SerializedName

data class OpenWeatherCurrentResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val clouds: Clouds,
    val rain: Rain?,
    val sys: Sys?,
    val dt: Long?
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String,
    val icon: String?
)

data class Wind(
    val speed: Double
)

data class Clouds(
    val all: Int
)

data class Rain(
    @SerializedName("1h") val oneHour: Double?
)

data class Sys(
    val sunrise: Long?,
    val sunset: Long?
)


data class OpenWeatherOneCallResponse(
    val timezone: String?,
    val current: Current?,
    val hourly: List<Hourly>?,
    val daily: List<Daily>?,
    val alerts: List<Alert>?
)

data class Current(
    val dt: Long,
    val sunrise: Long?,
    val sunset: Long?,
    val temp: Double,
    val humidity: Int,
    val wind_speed: Double,
    val weather: List<Weather>
)

data class Hourly(
    val dt: Long,
    val temp: Double,
    val weather: List<Weather>,
    @SerializedName("pop") val precipitationProbability: Double?
)

data class Daily(
    val dt: Long,
    val sunrise: Long?,
    val sunset: Long?,
    val temp: DailyTemp,
    val weather: List<Weather>,
    @SerializedName("pop") val precipitationProbability: Double?
)

data class DailyTemp(
    val min: Double,
    val max: Double
)

data class Alert(
    val sender_name: String?,
    val event: String?,
    val start: Long?,
    val end: Long?,
    val description: String?
)


data class OpenWeatherForecastResponse(
    val list: List<ForecastItem>,
    val city: ForecastCity?
)

data class ForecastItem(
    val dt: Long,
    val main: ForecastMain,
    val weather: List<Weather>,
    @SerializedName("pop") val precipitationProbability: Double?
)

data class ForecastMain(
    val temp: Double,
    @SerializedName("temp_min") val tempMin: Double?,
    @SerializedName("temp_max") val tempMax: Double?
)

data class ForecastCity(
    val name: String?,
    val sunrise: Long?,
    val sunset: Long?
)