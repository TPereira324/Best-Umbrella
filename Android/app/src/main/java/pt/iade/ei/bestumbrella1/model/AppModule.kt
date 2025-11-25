package pt.iade.ei.bestumbrella1.di

import android.content.Context
import pt.iade.ei.bestumbrella1.data.Repository
import pt.iade.ei.bestumbrella1.model.SessionManager
import pt.iade.ei.bestumbrella1.network.ApiService
import pt.iade.ei.bestumbrella1.network.RetrofitClient
import pt.iade.ei.bestumbrella1.controllers.AuthController
import pt.iade.ei.bestumbrella1.controllers.WeatherController

object AppModule {

    private var repository: Repository? = null
    private var sessionManager: SessionManager? = null
    private var authController: AuthController? = null
    private var weatherController: WeatherController? = null

    fun provideSessionManager(context: Context): SessionManager {
        return sessionManager ?: SessionManager(context).also {
            sessionManager = it
        }
    }

    fun provideRepository(context: Context): Repository {
        return repository ?: Repository(
            RetrofitClient.api as ApiService,
            provideSessionManager(context)
        ).also {
            repository = it
        }
    }

    fun provideAuthController(context: Context): AuthController {
        return authController ?: AuthController(
            provideRepository(context),
            provideSessionManager(context)
        ).also {
            authController = it
        }
    }


    fun provideWeatherController(context: Context): WeatherController {
        return weatherController ?: WeatherController(provideRepository(context)).also {
            weatherController = it
        }
    }


    fun clearInstances() {
        repository = null
        authController = null
        weatherController = null
    }
}
