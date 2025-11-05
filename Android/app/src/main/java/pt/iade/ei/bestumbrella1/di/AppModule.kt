package pt.iade.ei.bestumbrella1.di

import android.content.Context
import pt.iade.ei.bestumbrella1.network.ApiService
import pt.iade.ei.bestumbrella1.data.Repository
import pt.iade.ei.bestumbrella1.models.SessionManager
import pt.iade.ei.bestumbrella1.network.RetrofitClient
import pt.iade.ei.bestumbrella1.viewmodels.AuthViewModel
import pt.iade.ei.bestumbrella1.viewmodels.WeatherViewModel
object AppModule {
    
    private var repository: Repository? = null
    private var sessionManager: SessionManager? = null
    private var authViewModel: AuthViewModel? = null
    private var weatherViewModel: WeatherViewModel? = null
    
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
    
    fun provideAuthViewModel(context: Context): AuthViewModel {
        return authViewModel ?: AuthViewModel(
            provideRepository(context),
            provideSessionManager(context)
        ).also {
            authViewModel = it
        }
    }
    
    fun provideWeatherViewModel(context: Context): WeatherViewModel {
        return weatherViewModel ?: WeatherViewModel(provideRepository(context)).also {
            weatherViewModel = it
        }
    }
    

    fun clearInstances() {
        repository = null
        authViewModel = null
        weatherViewModel = null
    }
}