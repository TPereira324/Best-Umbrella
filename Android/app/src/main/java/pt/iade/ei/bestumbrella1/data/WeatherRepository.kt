package pt.iade.ei.bestumbrella1.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.iade.ei.bestumbrella1.BuildConfig
import pt.iade.ei.bestumbrella1.network.Daily
import pt.iade.ei.bestumbrella1.network.DailyTemp
import pt.iade.ei.bestumbrella1.network.ForecastItem
import pt.iade.ei.bestumbrella1.network.Hourly
import pt.iade.ei.bestumbrella1.network.OpenWeatherCurrentResponse
import pt.iade.ei.bestumbrella1.network.OpenWeatherForecastResponse
import pt.iade.ei.bestumbrella1.network.OpenWeatherOneCallResponse
import pt.iade.ei.bestumbrella1.network.OpenWeatherRetrofit
import pt.iade.ei.bestumbrella1.network.Weather
import pt.iade.ei.bestumbrella1.network.WeatherResponse

class WeatherRepository {
    suspend fun getOneCallForecast(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherOneCallResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = OpenWeatherRetrofit.api.getOneCall(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    units = "metric",
                    lang = "pt",
                    exclude = "minutely"
                )
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao obter previsão (One Call): HTTP ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getFiveDayForecast(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherForecastResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = OpenWeatherRetrofit.api.getFiveDayForecast(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    units = "metric",
                    lang = "pt"
                )
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Falha ao obter previsão (forecast 5 dias): HTTP ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun mapForecastToHourlyDaily(forecast: OpenWeatherForecastResponse): Pair<List<Hourly>, List<Daily>> {
        val hourly = forecast.list.take(8 * 3)
            .take(8)
            .map { it.toHourly() }

        val byDay = forecast.list.groupBy { item ->
            (item.dt / 86400L)
        }
        val daily = byDay.entries.sortedBy { it.key }.take(5).map { (_, items) ->
            val temps = items.mapNotNull { it.main.temp }
            val min = items.mapNotNull { it.main.tempMin }.minOrNull() ?: temps.minOrNull() ?: 0.0
            val max = items.mapNotNull { it.main.tempMax }.maxOrNull() ?: temps.maxOrNull() ?: 0.0
            val dt = items.firstOrNull()?.dt ?: 0L
            val weather: List<Weather> = items.firstOrNull()?.weather ?: emptyList()
            val popAvg =
                items.mapNotNull { it.precipitationProbability }.average().takeIf { !it.isNaN() }
            Daily(
                dt = dt,
                sunrise = null,
                sunset = null,
                temp = DailyTemp(min = min, max = max),
                weather = weather,
                precipitationProbability = popAvg
            )
        }
        return Pair(hourly, daily)
    }

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): Result<WeatherResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = OpenWeatherRetrofit.api.getCurrentWeather(
                    latitude = latitude,
                    longitude = longitude,
                    apiKey = BuildConfig.WEATHER_API_KEY,
                    units = "metric",
                    lang = "pt"
                )
                if (response.isSuccessful && response.body() != null) {
                    val mapped = mapOpenWeatherToWeatherResponse(response.body()!!)
                    Result.success(mapped)
                } else {
                    Result.failure(Exception("Falha ao obter clima em tempo real: HTTP ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun mapOpenWeatherToWeatherResponse(open: OpenWeatherCurrentResponse): WeatherResponse {
        val temp = open.main.temp
        val desc = open.weather.firstOrNull()?.description ?: ""
        val humidity = open.main.humidity
        val wind = open.wind.speed
        val rainProbability = 0.0
        return WeatherResponse(
            temperature = temp,
            description = desc,
            humidity = humidity,
            windSpeed = wind,
            rainProbability = rainProbability,
            isSuccessful = true
        )
    }

    private fun ForecastItem.toHourly(): Hourly {
        val pop = precipitationProbability
        return Hourly(
            dt = dt,
            temp = main.temp,
            weather = weather,
            precipitationProbability = pop
        )
    }
}