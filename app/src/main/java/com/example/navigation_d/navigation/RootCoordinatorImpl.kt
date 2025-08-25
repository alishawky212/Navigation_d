package com.example.navigation_d.navigation

import androidx.activity.compose.BackHandler
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
import com.example.navigation_d.navigation.contract.NavigationAction
import com.example.navigation_d.navigation.contract.RootCoordinator
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class RootCoordinatorImpl @Inject constructor(
    override val navigator: Navigator,
    @Named("AuthCoordinator") private val authCoordinator: Lazy<Coordinator>,
    @Named("MainCoordinator") private val mainCoordinator: Lazy<Coordinator>
) : RootCoordinator {

    override val parent: Lazy<Coordinator>? = null
    
    private var _activeCoordinator by mutableStateOf<Coordinator?>(null)
    override val activeCoordinator: Coordinator? get() = _activeCoordinator

    override fun handle(action: CoordinatorAction): Boolean {
        // Check for navigation actions first
        if (action is NavigationAction) {
            return when (action) {
                is NavigationAction.Back -> navigateBack()
                is NavigationAction.Up -> navigateUp()
                is NavigationAction.Home -> {
                    // Navigate to home screen
                    navigator.resetTo(NavigationRoutes.MAIN_GRAPH)
                    _activeCoordinator = mainCoordinator.get()
                    true
                }
            }
        }

        // Then check other action types
        return when (action) {
            is AppCoordinatorAction -> handleAppAction(action)
            is GeneralAction -> handleGeneralAction(action)
            else -> _activeCoordinator?.handle(action) ?: false
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

        // Set the NavController for system back integration
        LaunchedEffect(navController) {
            navigator.setNavController(navController)
        }

        // Handle system back button
        BackHandler(enabled = true) {
            // Handle system back through our coordinator system first,
            // then fall back to Navigator's system back handling
            if (!navigateBack()) {
                navigator.handleSystemBack()
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
        }
    }

    private fun handleGeneralAction(action: GeneralAction): Boolean {
        return when (action) {
            is GeneralAction.Done -> {
                // Handle done action - delegate to active coordinator or handle locally
                _activeCoordinator?.handle(action) ?: false
            }
            is GeneralAction.Cancel -> {
                // Handle cancel action - delegate to active coordinator or handle locally
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

    override fun navigateBack(): Boolean {
        // First try to let the active coordinator handle it
        if (_activeCoordinator?.navigateBack() == true) {
            return true
        }

        // If no active coordinator or it didn't handle back, we're at root level
        // Return false to indicate system should handle it (e.g., exit app)
        return false
    }

    override fun navigateUp(): Boolean {
        // In RootCoordinator, up navigation is the same as back navigation
        return navigateBack()
    }

    override fun canNavigateBack(): Boolean {
        // Check if active coordinator can navigate back
        return _activeCoordinator?.canNavigateBack() == true
    }

    override fun activateCoordinator(coordinator: Coordinator) {
        _activeCoordinator = coordinator
    }

    override fun deactivateCoordinator() {
        _activeCoordinator = null
    }
}
