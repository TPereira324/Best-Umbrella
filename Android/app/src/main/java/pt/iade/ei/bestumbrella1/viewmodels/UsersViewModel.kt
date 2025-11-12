package pt.iade.ei.bestumbrella1.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.data.Repository
import pt.iade.ei.bestumbrella1.network.UserProfileResponse

class UsersViewModel(private val repository: Repository) : ViewModel() {
    private val _users = MutableStateFlow<List<UserProfileResponse>>(emptyList())
    val users: StateFlow<List<UserProfileResponse>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchUsers() {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.getAllUsers()
            result.fold(
                onSuccess = { list ->
                    _users.value = list
                    _error.value = null
                },
                onFailure = { e ->
                    _error.value = e.message
                }
            )
            _isLoading.value = false
        }
    }
}

