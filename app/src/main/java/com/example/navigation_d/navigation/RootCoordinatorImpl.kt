package com.example.navigation_d.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.navigation_d.navigation.contract.AppCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.GeneralAction
import com.example.navigation_d.navigation.contract.RootCoordinator
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import javax.inject.Provider

@Singleton
class RootCoordinatorImpl @Inject constructor(
    override val navigator: Navigator,
    @Named("AutheCoordinator") private val authCoordinator: Lazy<Coordinator>,
    @Named("MaineCoordinator") private val mainCoordinator: Lazy<Coordinator>
) : RootCoordinator {
    
    override val parent: Lazy<Coordinator>?? = null
    
    private var _activeCoordinator by mutableStateOf<Coordinator?>(null)
    override val activeCoordinator: Coordinator? get() = _activeCoordinator


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
    
    /**
     * Centralized NavHost rendering method - responsible for showing all nav graphs
     */
    @Composable
    override fun renderNavHost() {
        val navController = rememberNavController()
        
        // Observe navigator state and trigger navigation
        LaunchedEffect(navigator.route) {
            if (navigator.route.isNotEmpty()) {
                navController.navigate(navigator.route) {
                    launchSingleTop = true
                }
            }
        }
        
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.AUTH_GRAPH
        ) {
            // Set up ALL coordinator graphs, not just the active one
            authCoordinator.get().setupNavigation(this)
            mainCoordinator.get().setupNavigation(this)
        }
    }
    
    private fun handleAppAction(action: AppCoordinatorAction): Boolean {
        return when (action) {
            is AppCoordinatorAction.StartAuthFlow -> {
                _activeCoordinator = authCoordinator.get()
                navigator.navigateTo("auth_graph")
                true
            }
            is AppCoordinatorAction.StartMainFlow -> {
                _activeCoordinator = mainCoordinator.get()
                navigator.navigateTo("main_graph")
                true
            }
            is AppCoordinatorAction.NavigateToRoute -> {
                navigator.navigateTo(action.route)
                true
            }
            else -> false
        }
    }
    
    private fun handleGeneralAction(action: GeneralAction) {
        when (action) {
            is GeneralAction.Done -> {
                // Handle done action
                _activeCoordinator?.handle(action) ?: false
            }
            is GeneralAction.Cancel -> {
                // Handle cancel action
                _activeCoordinator?.handle(action) ?: false
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
