package com.example.navigation_d.features.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.navigation_d.features.main.coordinator.MainCoordinator
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainCoordinator: Coordinator
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onOrdersClick() {
        mainCoordinator.handle(MainCoordinatorAction.ShowOrders)
    }

    fun onProfileClick() {
        mainCoordinator.handleMainAction(MainCoordinatorAction.ShowProfile)
    }

    fun onLogoutClick() {
        mainCoordinator.handleMainAction(MainCoordinatorAction.Logout)
    }

    fun onBackClick() {
        // In the reference pattern, we use actions instead of direct navigation
        mainCoordinator.handleMainAction(MainCoordinatorAction.Logout)
    }
}

data class MainUiState(
    val userName: String = "John Doe",
    val isLoading: Boolean = false
)
