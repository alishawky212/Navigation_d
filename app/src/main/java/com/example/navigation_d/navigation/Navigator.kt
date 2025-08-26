package com.example.navigation_d.navigation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
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
        Log.d("Navigator", "NavController set")
    }

    /**
     * Navigate to a new route with optional parameters
     */
    fun navigateTo(route: String, params: Any? = null) {
        Log.d("Navigator", "Navigating to $route")
        _currentRoute = route

        // Determine if this is a top-level destination
        val isTopLevelDestination = route == AUTH_GRAPH || route == NavigationRoutes.MAIN_GRAPH

        try {
            // Actually navigate using NavController if available
            navController?.navigate(route) {
                // For top-level destinations, set different behavior
                if (isTopLevelDestination) {
                    // Pop up to the first destination and save state when navigating between graphs
                    popUpTo(navController!!.graph.findStartDestination().id) {
                        saveState = true
                    }
                }
                // Single top means only one instance of this destination on the back stack
                launchSingleTop = true
                // Restore state when navigating back to a destination
                restoreState = true
            }
            Log.d("Navigator", "Navigation complete. Current route: $route")
        } catch (e: Exception) {
            Log.e("Navigator", "Navigation failed: ${e.message}")
        }
    }

    /**
     * Navigate to a route and make it the root of the back stack
     * This clears all previous entries from the back stack
     */
    fun navigateToAsRoot(route: String, params: Any? = null) {
        Log.d("Navigator", "Setting $route as root")
        _currentRoute = route

        try {
            // Navigate and clear the entire back stack
            navController?.navigate(route) {
                launchSingleTop = true
                // This clears the back stack
                popUpTo(AUTH_GRAPH) { inclusive = true }
            }
            Log.d("Navigator", "Root navigation complete")
        } catch (e: Exception) {
            Log.e("Navigator", "Root navigation failed: ${e.message}")
        }
    }

    /**
     * Reset navigation to a specific route and clear back stack
     */
    fun resetTo(route: String, params: Any? = null) {
        Log.d("Navigator", "Resetting to $route")
        _currentRoute = route

        try {
            // Actually navigate using NavController if available and clear back stack
            navController?.navigate(route) {
                launchSingleTop = true
                // Clear back stack when resetting to a new route
                popUpTo(0) {
                    inclusive = true
                }
            }
            Log.d("Navigator", "Reset complete")
        } catch (e: Exception) {
            Log.e("Navigator", "Reset failed: ${e.message}")
        }
    }

    /**
     * Clear current route state
     */
    fun clear() {
        _currentRoute = ""
    }

    /**
     * Pop the back stack to exit the current destination
     * Used for system-level navigation like app exit
     *
     * @return true if the back stack was popped successfully, false otherwise
     */
    fun popBackStack(): Boolean {
        return try {
            Log.d("Navigator", "Popping back stack")
            val result = navController?.popBackStack() ?: false
            Log.d("Navigator", "Back stack popped: $result")
            result
        } catch (e: Exception) {
            Log.e("Navigator", "Error popping back stack: ${e.message}")
            false
        }
    }

    /**
     * Get access to the NavController for specific operations
     * Use with caution - prefer using Navigator methods when possible
     */
    fun getNavController(): NavController? {
        return navController
    }

    /**
     * Check if back navigation is possible with the current NavController
     */
    fun canGoBack(): Boolean {
        return navController?.previousBackStackEntry != null
    }

    /**
     * Log the current back stack for debugging
     */
    fun logBackStack() {
        val controller = navController ?: return
        try {
            // Use the back stack entry API which is publicly accessible
            val currentEntry = controller.currentBackStackEntry
            val previousEntry = controller.previousBackStackEntry

            Log.d("Navigator", "Current destination: ${currentEntry?.destination?.route}")
            Log.d("Navigator", "Previous destination: ${previousEntry?.destination?.route}")
        } catch (e: Exception) {
            Log.e("Navigator", "Error logging back stack: ${e.message}")
        }
    }
}