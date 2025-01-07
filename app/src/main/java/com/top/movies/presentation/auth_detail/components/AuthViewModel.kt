package com.top.movies.presentation.auth_detail.components

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.top.movies.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val entered = MutableStateFlow(false)
    val loginError = MutableStateFlow<String?>(null)
    val toastMessage = MutableStateFlow<String?>(null)

    fun registerUser(username: String, password: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                val result = authRepository.registerUser(username, password)
                onResult(Result.success(result))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }


    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            if (authRepository.loginUser(username, password)) {
                entered.value = true
                loginError.value = null // Очистить ошибку при успешном входе
            } else {
                entered.value = false
                loginError.value = "Invalid username or password"
            }
        }
    }

    fun validateFields(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            toastMessage.value = "Username cannot be empty"
            return false
        }
        if (password.length <= 3) {
            toastMessage.value = "Password must be longer than 3 characters"
            return false
        }
        return true
    }

}
