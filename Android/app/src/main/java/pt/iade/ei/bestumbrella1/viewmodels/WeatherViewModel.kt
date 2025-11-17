package pt.iade.ei.bestumbrella1.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.data.Repository
import pt.iade.ei.bestumbrella1.network.WeatherResponse
import pt.iade.ei.bestumbrella1.network.Hourly
import pt.iade.ei.bestumbrella1.network.Daily
import pt.iade.ei.bestumbrella1.network.Alert

class WeatherViewModel(private val repository: Repository) : ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _hourly = MutableLiveData<List<Hourly>>()
    val hourly: LiveData<List<Hourly>> = _hourly

    private val _daily = MutableLiveData<List<Daily>>()
    val daily: LiveData<List<Daily>> = _daily

    private val _alerts = MutableLiveData<List<Alert>>()
    val alerts: LiveData<List<Alert>> = _alerts

    private val _sunriseSunset = MutableLiveData<Pair<Long?, Long?>>()
    val sunriseSunset: LiveData<Pair<Long?, Long?>> = _sunriseSunset

    // Condição atual (real-time) para dirigir ícones dinâmicos com precisão
    private val _currentWeatherId = MutableLiveData<Int?>()
    val currentWeatherId: LiveData<Int?> = _currentWeatherId

    fun getWeatherForecast(latitude: Double, longitude: Double) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.getWeatherForecast(latitude, longitude)
                result.fold(
                    onSuccess = { response ->
                        _weatherData.value = response
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao obter previsão do tempo"
                    }
                )

                val oneCall = repository.getOneCallForecast(latitude, longitude)
                oneCall.fold(
                    onSuccess = { oc ->
                        _hourly.value = oc.hourly?.take(24) ?: emptyList()
                        _daily.value = oc.daily?.take(5) ?: emptyList()
                        _alerts.value = oc.alerts ?: emptyList()
                        _sunriseSunset.value = Pair(oc.current?.sunrise, oc.current?.sunset)
                        _currentWeatherId.value = oc.current?.weather?.firstOrNull()?.id
                    },
                    onFailure = { e ->
                        val forecast = repository.getFiveDayForecast(latitude, longitude)
                        forecast.fold(
                            onSuccess = { fc ->
                                val (h, d) = repository.mapForecastToHourlyDaily(fc)
                                _hourly.value = h
                                _daily.value = d
                                _sunriseSunset.value = Pair(fc.city?.sunrise, fc.city?.sunset)
                                // Sem One Call, não há "current"; manter o último valor ou null
                                // _currentWeatherId.value = null
                            },
                            onFailure = { fe ->
                                _error.value = listOfNotNull(
                                    e.message,
                                    fe.message
                                ).joinToString("\n")
                            }
                        )
                    }
                )

                if ((_alerts.value ?: emptyList()).isEmpty() && (_daily.value ?: emptyList()).isNotEmpty()) {
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro desconhecido"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

