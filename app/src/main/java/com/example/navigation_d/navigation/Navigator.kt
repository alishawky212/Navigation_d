package com.example.navigation_d.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.navigation_d.navigation.NavigationRoutes.AUTH_GRAPH
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    private var _currentRoute by mutableStateOf(AUTH_GRAPH)
    private var navController: NavController? = null

    val route: String get() = _currentRoute

    /**
     * Set the NavController for navigation integration
     */
    fun setNavController(controller: NavController) {
        navController = controller
    }

    /**
     * Navigate to a new route with optional parameters
     */
    fun navigateTo(route: String, params: Any? = null) {
        _currentRoute = route

        // Actually navigate using NavController if available
        navController?.navigate(route) {
            launchSingleTop = true
        }
    }

    /**
     * Handle system back button press by delegating to NavController
     * @return true if back navigation was handled, false otherwise
     */
    fun handleSystemBack(): Boolean {
        return navController?.let { controller ->
            if (controller.previousBackStackEntry != null) {
                controller.popBackStack()
                true
            } else {
                false
            }
        } ?: false
    }

    /**
     * Reset navigation to a specific route and clear back stack
     */
    fun resetTo(route: String, params: Any? = null) {
        _currentRoute = route

        // Actually navigate using NavController if available and clear back stack
        navController?.navigate(route) {
            launchSingleTop = true
            // Clear back stack when resetting to a new route
            popUpTo(navController?.graph?.startDestinationId ?: 0) {
                inclusive = true
            }
        }
    }

    /**
     * Clear current route state
     */
    fun clear() {
        _currentRoute = ""
    }
}