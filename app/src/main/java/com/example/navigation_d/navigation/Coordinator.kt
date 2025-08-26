package com.example.navigation_d.navigation

import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.NavigationAction
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
     * Navigate back to the previous screen
     * @return true if back navigation was handled, false otherwise
     */
    fun navigateBack(): Boolean {
        // Default implementation delegates to parent if available
        // Each coordinator should override this with their own implementation
        // that checks internal navigation state before delegating
        return parent?.get()?.navigateBack() ?: false
    }

    /**
     * Navigate up to the parent flow
     * @return true if up navigation was handled, false otherwise
     */
    fun navigateUp(): Boolean {
        // Default implementation delegates to parent
        return parent?.get()?.navigateUp() ?: false
    }

    /**
     * Check if this coordinator can handle back navigation
     * @return true if back navigation is possible, false otherwise
     */
    fun canNavigateBack(): Boolean {
        // Default implementation checks if parent can navigate back
        return parent?.get()?.canNavigateBack() ?: false
    }

    /**
     * Set up navigation routes for this coordinator
     */
    fun setupNavigation(builder: NavGraphBuilder) {
        // Default implementation can be overridden
    }
    
    /**
     * Handle coordinator actions
     * @param action The action to handle
     * @return true if the action was handled, false otherwise
     */
    fun handle(action: CoordinatorAction): Boolean {
        // Handle common navigation actions by default
        if (action is NavigationAction) {
            return when (action) {
                is NavigationAction.Back -> navigateBack()
                is NavigationAction.Up -> navigateUp()
                is NavigationAction.Home -> parent?.get()?.handle(action) ?: false
            }
        }
        return false
    }
}

/**
 * Interface that tracks the active coordinator for nested navigation
 */
interface Host {
    val activeCoordinator: Coordinator?

    /**
     * Activate a child coordinator
     * @param coordinator The coordinator to activate
     */
    fun activateCoordinator(coordinator: Coordinator) {}

    /**
     * Deactivate the current child coordinator
     */
    fun deactivateCoordinator() {}
}

/**
 * Combines the Coordinator and Host interfaces, enabling a coordinator to host other coordinators
 * Used for nested navigation patterns where one coordinator manages child coordinators
 */
interface HostCoordinator : Coordinator, Host
