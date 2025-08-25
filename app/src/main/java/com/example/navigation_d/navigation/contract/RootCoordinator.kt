package com.example.navigation_d.navigation.contract

import androidx.compose.runtime.Composable
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.Navigator

/**
 * Root coordinator interface that serves as the main parent coordinator
 * Combines coordinator capabilities with hosting other coordinators
 * Based on the reference pattern: extends HostCoordinator and includes navigator
 */
interface RootCoordinator : HostCoordinator {
    val navigator: Navigator
    
    /**
     * Handle general actions
     * @return true if the action was handled, false otherwise
     */

    @Composable
    fun start(action: AppCoordinatorAction)
    @Composable
    fun renderNavHost()
}
