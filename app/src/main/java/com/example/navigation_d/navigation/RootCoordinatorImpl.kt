package com.example.navigation_d.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import com.example.navigation_d.navigation.contract.AppCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.GeneralAction
import com.example.navigation_d.navigation.contract.RootCoordinator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RootCoordinatorImpl @Inject constructor(
    override val navigator: Navigator,
    private val authCoordinator: Coordinator,
    private val mainCoordinator: Coordinator?
) : RootCoordinator {
    
    override val parent: Coordinator? = null
    
    private var _activeCoordinator by mutableStateOf<Coordinator?>(null)
    override val activeCoordinator: Coordinator? get() = _activeCoordinator


    @Composable
    override fun handle(action: CoordinatorAction) {
        when (action) {
            is AppCoordinatorAction -> handleAppAction(action)
            is GeneralAction -> handleGeneralAction(action)
            else -> _activeCoordinator?.handle(action)
        }
    }
    
    @Composable
    override fun start(action: AppCoordinatorAction) {
        handle(action)
    }
    
    private fun handleAppAction(action: AppCoordinatorAction) {
        when (action) {
            is AppCoordinatorAction.StartAuthFlow -> {
                _activeCoordinator = authCoordinator
                navigator.navigateTo("auth_graph")
            }
            is AppCoordinatorAction.StartMainFlow -> {
                _activeCoordinator = mainCoordinator
                navigator.navigateTo("main_graph")
            }
            is AppCoordinatorAction.NavigateToRoute -> {
                navigator.navigateTo(action.route)
            }
        }
    }
    
    private fun handleGeneralAction(action: GeneralAction) {
        when (action) {
            is GeneralAction.Back -> {
                // Handle back navigation
            }
            is GeneralAction.Done -> {
                // Handle done action
            }
            is GeneralAction.Cancel -> {
                // Handle cancel action
            }
        }
    }
    
    override fun navigate(route: String) {
        navigator.navigateTo(route)
    }

    override fun navigate(route: String, params: Any?) {
        navigator.navigateTo(route, params)
    }
    
    fun setActiveCoordinator(coordinator: Coordinator?) {
        _activeCoordinator = coordinator
    }
}
