package pt.iade.ei.bestumbrella1.network

import pt.iade.ei.bestumbrella1.model.OpenWeatherCurrentResponse
import pt.iade.ei.bestumbrella1.model.OpenWeatherForecastResponse
import pt.iade.ei.bestumbrella1.model.OpenWeatherOneCallResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pt"
    ): Response<OpenWeatherCurrentResponse>

    @GET("data/2.5/onecall")
    suspend fun getOneCall(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pt",
        @Query("exclude") exclude: String = "minutely"
    ): Response<OpenWeatherOneCallResponse>

    @GET("data/2.5/forecast")
    suspend fun getFiveDayForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pt"
    ): Response<OpenWeatherForecastResponse>
}