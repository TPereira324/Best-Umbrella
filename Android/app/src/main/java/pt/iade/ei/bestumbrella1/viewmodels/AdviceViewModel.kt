package pt.iade.ei.bestumbrella1.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.data.Repository

class AdviceViewModel(private val repository: Repository) : ViewModel() {
    val advice = mutableStateOf("Fetching advice...")

    init {
        fetchAdvice()
    }

    fun fetchAdvice() {
        viewModelScope.launch {
            val result = repository.getAdvice()
            result.fold(
                onSuccess = { response -> advice.value = response.slip.advice },
                onFailure = { e -> advice.value = "Error: ${e.message}" }
            )
        }
    }
}

