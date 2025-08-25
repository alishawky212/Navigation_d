package com.example.navigation_d.navigation

import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.navigation.contract.CoordinatorAction
import dagger.Lazy

/**
 * Core interface that all coordinators implement based on the reference pattern.
 * Each coordinator can navigate, set up navigation, and handle actions.
 * It also holds a reference to its parent coordinator for hierarchical navigation.
 */
interface Coordinator {
    /**
     * Reference to parent coordinator for hierarchical navigation
     */
    val parent: Lazy<Coordinator>?
    
    /**
     * Navigate to a route, delegating to parent if not handled locally
     */
    fun navigate(route: String) {
        parent?.get()?.navigate(route)
    }

    fun navigate(route: String, params: Any?) {
        parent?.get()?.navigate(route, params)
    }
    
    /**
     * Set up navigation routes for this coordinator
     */
    fun setupNavigation(builder: NavGraphBuilder) {
        // Default implementation can be overridden
    }
    
    /**
     * Handle coordinator actions
     * @return true if the action was handled, false otherwise
     */
    fun handle(action: CoordinatorAction)
}

/**
 * Interface that tracks the active coordinator
 */
interface Host {
    val activeCoordinator: Coordinator?
    var rootBuilder: NavGraphBuilder?
        get() = null
        set(value) {}
}

/**
 * Combines the Coordinator and Host interfaces, enabling a coordinator to host other coordinators
 */
interface HostCoordinator : Coordinator, Host
