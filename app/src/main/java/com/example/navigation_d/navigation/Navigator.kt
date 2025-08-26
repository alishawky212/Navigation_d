package com.example.navigation_d.navigation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.navigation_d.navigation.NavigationRoutes.AUTH_GRAPH
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Navigator class that handles navigation operations using NavController
 * This is a central component for coordinating navigation across the app
 */
@Singleton
class Navigator @Inject constructor() {

    // Track the current route for state management
    private var _currentRoute by mutableStateOf(AUTH_GRAPH)

    // NavHostController reference for performing navigation
    private var navController: NavHostController? = null

    // Expose current route as read-only property
    val route: String get() = _currentRoute

    /**
     * Set the NavHostController for navigation integration
     * Called by the root coordinator during initialization
     */
    fun setNavController(controller: NavHostController) {
        navController = controller
        Log.d(TAG, "NavHostController set")
    }

    /**
     * Navigate to a new route with optional parameters
     * @param route The destination route
     * @param params Optional parameters for the destination
     */
    fun navigateTo(route: String, params: Any? = null) {
        Log.d(TAG, "Navigating to $route")
        _currentRoute = route

        // Determine if this is a top-level destination
        val isTopLevelDestination = route == AUTH_GRAPH || route == NavigationRoutes.MAIN_GRAPH

        try {
            // Actually navigate using NavHostController if available
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
            Log.d(TAG, "Navigation complete. Current route: $route")
        } catch (e: Exception) {
            Log.e(TAG, "Navigation failed: ${e.message}")
        }
    }

    /**
     * Navigate to a route and make it the root of the back stack
     * This clears all previous entries from the back stack
     * @param route The destination route
     * @param params Optional parameters for the destination
     */
    fun navigateToAsRoot(route: String, params: Any? = null) {
        Log.d(TAG, "Setting $route as root")
        _currentRoute = route

        try {
            // Navigate and clear the entire back stack
            navController?.navigate(route) {
                launchSingleTop = true
                // This clears the back stack
                popUpTo(AUTH_GRAPH) { inclusive = true }
            }
            Log.d(TAG, "Root navigation complete")
        } catch (e: Exception) {
            Log.e(TAG, "Root navigation failed: ${e.message}")
        }
    }

    /**
     * Reset navigation to a specific route and clear back stack
     * @param route The destination route
     * @param params Optional parameters for the destination
     */
    fun resetTo(route: String, params: Any? = null) {
        Log.d(TAG, "Resetting to $route")
        _currentRoute = route

        try {
            // Actually navigate using NavHostController if available and clear back stack
            navController?.navigate(route) {
                launchSingleTop = true
                // Clear back stack when resetting to a new route
                popUpTo(0) {
                    inclusive = true
                }
            }
            Log.d(TAG, "Reset complete")
        } catch (e: Exception) {
            Log.e(TAG, "Reset failed: ${e.message}")
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
     * @return true if the back stack was popped successfully, false otherwise
     */
    fun popBackStack(): Boolean {
        return try {
            Log.d(TAG, "Popping back stack")
            val result = navController?.popBackStack() ?: false
            Log.d(TAG, "Back stack popped: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error popping back stack: ${e.message}")
            false
        }
    }

    /**
     * Get access to the NavHostController for specific operations
     * Use with caution - prefer using Navigator methods when possible
     * @return The current NavHostController instance, or null if not initialized
     */
    fun getNavController(): NavHostController? {
        return navController
    }

    /**
     * Check if back navigation is possible with the current NavHostController
     * @return true if back navigation is possible, false otherwise
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

            Log.d(TAG, "Current destination: ${currentEntry?.destination?.route}")
            Log.d(TAG, "Previous destination: ${previousEntry?.destination?.route}")
        } catch (e: Exception) {
            Log.e(TAG, "Error logging back stack: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "Navigator"
    }
}