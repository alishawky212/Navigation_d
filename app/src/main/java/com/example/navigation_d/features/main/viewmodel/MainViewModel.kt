package com.example.navigation_d.features.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import com.example.navigation_d.navigation.contract.NavigationAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("MainCoordinator") private val mainCoordinator: HostCoordinator
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onOrdersClick() {
        mainCoordinator.handle(MainCoordinatorAction.ShowOrders)
    }

    fun onProfileClick() {
        mainCoordinator.handle(MainCoordinatorAction.ShowProfile)
    }

    fun onLogoutClick() {
        mainCoordinator.handle(MainCoordinatorAction.Logout)
    }

    fun onBackClick() {
        // Use NavigationAction.Back for consistent back handling
        mainCoordinator.handle(NavigationAction.Back)
    }
}

data class MainUiState(
    val userName: String = "John Doe",
    val isLoading: Boolean = false
)
