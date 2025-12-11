package pt.iade.ei.bestumbrella1.data

import pt.iade.ei.bestumbrella1.model.Daily
import pt.iade.ei.bestumbrella1.model.Hourly
import pt.iade.ei.bestumbrella1.model.OpenWeatherForecastResponse
import pt.iade.ei.bestumbrella1.model.OpenWeatherOneCallResponse
import pt.iade.ei.bestumbrella1.model.SessionManager
import pt.iade.ei.bestumbrella1.network.ApiService
import pt.iade.ei.bestumbrella1.network.UserProfileResponse
import pt.iade.ei.bestumbrella1.network.UserResponse
import pt.iade.ei.bestumbrella1.network.WeatherResponse

class Repository(private val apiService: ApiService, private val sessionManager: SessionManager) {
    private val weatherRepo = WeatherRepository()
    private val authRepo = AuthRepository(apiService, sessionManager)

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        phone: String?
    ): Result<UserResponse> = authRepo.registerUser(name, email, password, phone)

    suspend fun getOneCallForecast(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherOneCallResponse> {
        return weatherRepo.getOneCallForecast(latitude, longitude)
    }

    suspend fun getFiveDayForecast(
        latitude: Double,
        longitude: Double
    ): Result<OpenWeatherForecastResponse> {
        return weatherRepo.getFiveDayForecast(latitude, longitude)
    }

    fun mapForecastToHourlyDaily(forecast: OpenWeatherForecastResponse): Pair<List<Hourly>, List<Daily>> {
        return weatherRepo.mapForecastToHourlyDaily(forecast)
    }


    suspend fun loginUser(email: String, password: String): Result<UserResponse> =
        authRepo.loginUser(email, password)

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): Result<WeatherResponse> {
        return weatherRepo.getWeatherForecast(latitude, longitude)
    }


    suspend fun getUserProfile(): Result<UserProfileResponse> = authRepo.getUserProfile()
}
