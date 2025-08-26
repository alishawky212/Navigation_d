package com.example.navigation_d.navigation.contract

import androidx.compose.runtime.Composable
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.Navigator

/**
 * Root coordinator interface that serves as the main parent coordinator
 * Combines coordinator capabilities with hosting other coordinators
 * Responsible for all actual navigation operations through the Navigator
 */
interface RootCoordinator : HostCoordinator {
    val navigator: Navigator
    
    /**
     * Start the navigation flow with the specified action
     */
    @Composable
    fun start(action: AppCoordinatorAction)

    /**
     * Render the navigation host
     */
    @Composable
    fun renderNavHost()

    /**
     * Execute navigation to the specified route
     * This is the entry point for all navigation in the app
     */
    override fun executeNavigation(route: String, params: Any?)

    /**
     * Execute back navigation
     * @return true if navigation was successful
     */
    fun executeBackNavigation(): Boolean

    /**
     * Check if back navigation is possible
     * @return true if back navigation is possible
     */
    override fun canNavigateBack(): Boolean
}
