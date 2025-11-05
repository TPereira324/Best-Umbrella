package pt.iade.ei.bestumbrella1.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.iade.ei.bestumbrella1.data.Repository
import pt.iade.ei.bestumbrella1.models.AuthResponse
import pt.iade.ei.bestumbrella1.models.SessionManager

class AuthViewModel(private val repository: Repository, private val sessionManager: SessionManager) : ViewModel() {

    private val _loginResult = MutableLiveData<AuthResponse>()
    val loginResult: LiveData<AuthResponse> = _loginResult

    private val _registerResult = MutableLiveData<AuthResponse>()
    val registerResult: LiveData<AuthResponse> = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.loginUser(email, password)
                result.fold(
                    onSuccess = { response ->
                        response.token?.let { sessionManager.saveToken(it) }
                        sessionManager.saveName(response.name)
                        sessionManager.saveEmail(response.email)
                        _loginResult.value = AuthResponse(
                            success = true,
                            message = "Login realizado com sucesso",
                            userId = response.id,
                            userName = response.name,
                            userEmail = response.email,
                            token = response.token
                        )
                    },
                    onFailure = { exception ->
                        _loginResult.value = AuthResponse(
                            success = false,
                            message = exception.message ?: "Erro ao fazer login"
                        )
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro desconhecido"
                _loginResult.value = AuthResponse(
                    success = false,
                    message = e.message ?: "Erro ao fazer login"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.registerUser(name, email, password)
                result.fold(
                    onSuccess = { response ->
                        response.token?.let { sessionManager.saveToken(it) }
                        sessionManager.saveName(response.name)
                        sessionManager.saveEmail(response.email)
                        _registerResult.value = AuthResponse(
                            success = true,
                            message = "Registro realizado com sucesso",
                            userId = response.id,
                            userName = response.name,
                            userEmail = response.email,
                            token = response.token
                        )
                    },
                    onFailure = { exception ->
                        _registerResult.value = AuthResponse(
                            success = false,
                            message = exception.message ?: "Erro ao fazer registro"
                        )
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro desconhecido"
                _registerResult.value = AuthResponse(
                    success = false,
                    message = e.message ?: "Erro ao fazer registro"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserProfile() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.getUserProfile()
                result.fold(
                    onSuccess = { response ->
                        sessionManager.saveName(response.name)
                        sessionManager.saveEmail(response.email)
                    },
                    onFailure = { exception ->
                        _error.value = exception.message ?: "Erro ao obter perfil do usuário"
                    }
                )
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro desconhecido"
            } finally {
                _isLoading.value = false
            }
        }
    }
}