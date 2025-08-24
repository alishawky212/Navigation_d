package com.example.navigation_d.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.navigation_d.features.auth.coordinator.AuthCoordinator
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onDarkThemeToggle(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(darkTheme = enabled)
    }

    fun onNotificationsToggle(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    fun onLogoutClick() {
        authCoordinator.handleAuthAction(AuthCoordinatorAction.Logout)
    }

    fun onBackClick() {
        // In the reference pattern, we use actions instead of direct navigation
        authCoordinator.handleAuthAction(AuthCoordinatorAction.ShowSettings)
    }
}

data class SettingsUiState(
    val darkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false
)
