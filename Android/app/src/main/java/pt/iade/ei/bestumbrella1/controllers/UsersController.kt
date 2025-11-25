package pt.iade.ei.bestumbrella1.controllers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.iade.ei.bestumbrella1.network.UserProfileResponse

class UsersController() : ViewModel() {
    private val _users = MutableStateFlow<List<UserProfileResponse>>(emptyList())
    val users: StateFlow<List<UserProfileResponse>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
}
