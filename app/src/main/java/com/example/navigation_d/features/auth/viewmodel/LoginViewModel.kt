package com.example.navigation_d.features.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import com.example.navigation_d.navigation.contract.NavigationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("AuthCoordinator") private val authCoordinator: Coordinator
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Simulate login process
            kotlinx.coroutines.delay(1000)
            
            // Navigate to main screen after successful login using action-based navigation
            authCoordinator.handle(AuthCoordinatorAction.LoginSuccess("user123"))
            
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun onSettingsClick() {
        authCoordinator.handle(AuthCoordinatorAction.ShowSettings)
    }

    fun onBackClick() {
        // Use NavigationAction.Back for consistent back handling
        authCoordinator.handle(NavigationAction.Back)
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val errorMessage: String? = null
)
