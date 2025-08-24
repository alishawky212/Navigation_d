package com.example.navigation_d.navigation

import androidx.compose.runtime.Composable
import com.example.navigation_d.navigation.contract.CoordinatorAction

/**
 * Core interface that all coordinators implement based on the reference pattern.
 * Each coordinator can navigate, set up navigation, and handle actions.
 * It also holds a reference to its parent coordinator for hierarchical navigation.
 */
interface Coordinator {
    /**
     * Reference to parent coordinator for hierarchical navigation
     */
    val parent: Coordinator?
    
    /**
     * Navigate to a route, delegating to parent if not handled locally
     */
    fun navigate(route: String) {
        parent?.navigate(route)
    }

    fun navigate(route: String, params: Any?) {
        parent?.navigate(route, params)
    }
    
    /**
     * Set up navigation routes for this coordinator
     */
    fun setupNavigation(builder: NavHostBuilder) {
        // Default implementation can be overridden
    }
    
    /**
     * Handle coordinator actions
     */
    @Composable
    fun handle(action: CoordinatorAction)
}

/**
 * Interface that tracks the active coordinator
 */
interface Host {
    val activeCoordinator: Coordinator?
    var rootBuilder: NavHostBuilder?
        get() = null
        set(value) {}
}

/**
 * Combines the Coordinator and Host interfaces, enabling a coordinator to host other coordinators
 */
interface HostCoordinator : Coordinator, Host
