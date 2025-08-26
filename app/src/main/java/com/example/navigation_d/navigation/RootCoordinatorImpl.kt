package com.example.navigation_d.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
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
     * Centralized NavHost rendering method - responsible for showing the root navigation graph
     * and delegating to child coordinator graphs correctly.
     */
    @Composable
    override fun renderNavHost() {
        val navController = rememberNavController()

        // Set the NavController for system back integration
        LaunchedEffect(navController) {
            navigator.setNavController(navController)
        }

        // Handle system back button - explicitly delegate to NavController for proper system integration
        BackHandler(enabled = true) {
            Log.d("RootCoordinator", "System back button pressed")
            // Try to handle back press with our coordinator system first
            if (!navigateBack()) {
                Log.d("RootCoordinator", "Coordinator couldn't handle back, delegating to system")
                // We're at the root of the navigation stack in our coordinator system
                // Let the system handle the back press
                if (!navController.popBackStack()) {
                    Log.d("RootCoordinator", "No more back stack entries, exiting app")
                    // This is truly the end - no more back stack entries
                    // In a real app, you might want to show a confirmation dialog here
                }
            }
        }

        // Observe current destination for debugging
        val currentBackStackEntry = navController.currentBackStackEntryAsState().value
        LaunchedEffect(currentBackStackEntry) {
            val route = currentBackStackEntry?.destination?.route
            Log.d("Navigation", "Current destination: $route")
            val previousBackStackEntry = navController.previousBackStackEntry
            Log.d(
                "Navigation",
                "Previous destination: ${previousBackStackEntry?.destination?.route}"
            )
            Log.d("Navigation", "Can pop back stack: ${previousBackStackEntry != null}")
        }

        // Determine which graph to show based on active coordinator
        val currentGraphRoute = when (activeCoordinator) {
            mainCoordinator.get() -> NavigationRoutes.MAIN_GRAPH
            else -> NavigationRoutes.AUTH_GRAPH
        }

        NavHost(
            navController = navController,
            startDestination = currentGraphRoute
        ) {
            // Set up navigation graphs based on active coordinator
            authCoordinator.get().setupNavigation(this)
            mainCoordinator.get().setupNavigation(this)
        }

        // Initialize the activeCoordinator at start only if it's not set (initial launch)
        LaunchedEffect(Unit) {
            if (_activeCoordinator == null) {
                _activeCoordinator = authCoordinator.get()
            }
        }
    }
    
    private fun handleAppAction(action: AppCoordinatorAction): Boolean {
        return when (action) {
            is AppCoordinatorAction.StartAuthFlow -> {
                _activeCoordinator = authCoordinator.get()
                navigator.navigateTo(NavigationRoutes.AUTH_GRAPH)
                true
            }
            is AppCoordinatorAction.StartMainFlow -> {
                _activeCoordinator = mainCoordinator.get()
                // Make main graph the root of back stack so back button exits app
                navigator.navigateToAsRoot(NavigationRoutes.MAIN_GRAPH)
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

    /**
     * Navigate to a route and make it the root of the back stack
     */
    fun navigateToAsRoot(route: String, params: Any? = null) {
        navigator.navigateToAsRoot(route, params)
    }

    override fun navigateBack(): Boolean {
        // First try to let the active coordinator handle it
//        if (_activeCoordinator?.navigateBack() == true) {
//            return true
//        }

        // If we have an active coordinator but it couldn't handle back navigation,
        // we need to check if we're at the start destination of that coordinator's graph
        if (_activeCoordinator != null) {
            // If we're in main coordinator, go back to auth coordinator
            if (_activeCoordinator == mainCoordinator.get()) {
                navigator.popBackStack()
                return true
            }

            // If we're in auth coordinator and it couldn't handle back,
            // and we're at its start destination (login screen), 
            // return false to let system handle app exit
            if (_activeCoordinator == authCoordinator.get()) {
                _activeCoordinator = null
                navigator.popBackStack()
                return false // Let system handle exiting
            }
        }

        // If we're already at the root level with no active coordinator,
        // we need to check if there's anything in the Android back stack to pop
        val navController = navigator.getNavController()
        val canGoBack = navController?.previousBackStackEntry != null

        if (canGoBack) {
            // There's still something in the Android back stack, so pop it
            return navigator.popBackStack()
        }

        // We're truly at the end - no coordinator and no back stack
        // Return false to let the system handle it (exit the app)
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
