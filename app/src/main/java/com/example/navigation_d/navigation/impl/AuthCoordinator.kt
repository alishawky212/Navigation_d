package com.example.navigation_d.navigation.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.NavigationRoutes
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.ui.screens.LoginScreen
import com.example.navigation_d.ui.screens.SettingsScreen
import dagger.Lazy
import javax.inject.Inject

/**
 * Coordinator for the Auth flow
 * Handles login, registration, and related screens
 * Based on the Coordinator pattern to separate navigation logic from UI
 */
class AuthCoordinator @Inject constructor(
    override val parent: Lazy<Coordinator>
) : Coordinator {

    /**
     * Set up the auth navigation graph
     */
    override fun setupNavigation(builder: NavGraphBuilder) {
        // Create a nested navigation graph for the auth flow
        builder.navigation(
            startDestination = NavigationRoutes.Auth.LOGIN,
            route = NavigationRoutes.AUTH_GRAPH
        ) {
            // Login screen
            composable(NavigationRoutes.Auth.LOGIN) {
                LoginScreen(
                    onLoginClick = { userId ->
                        // Navigate to main flow on successful login
                        handle(AuthCoordinatorAction.LoginSuccess(userId))
                    },
                    onSettingsClick = {
                        // Navigate to settings
                        handle(AuthCoordinatorAction.ShowSettings)
                    }
                )
            }

            // Settings screen
            composable(NavigationRoutes.Auth.SETTINGS) {
                SettingsScreen(
                    onBackClick = {
                        // Navigate back
                        navigateBack()
                    }
                )
            }
        }
    }

    /**
     * Initialize the flow, usually by navigating to the start destination
     */
    fun start() {
        // Navigate to login screen
        handle(AuthCoordinatorAction.ShowLogin)
    }

    /**
     * Handle auth-specific actions and navigation events
     */
    override fun handle(action: CoordinatorAction): Boolean {
        // Handle auth-specific actions
        when (action) {
            is AuthCoordinatorAction.ShowLogin -> {
                navigate(NavigationRoutes.Auth.LOGIN)
                return true
            }
            is AuthCoordinatorAction.ShowSettings -> {
                navigate(NavigationRoutes.Auth.SETTINGS)
                return true
            }
            is AuthCoordinatorAction.LoginSuccess -> {
                // Delegate to parent (AppCoordinator) to handle login success
                return parent.get().handle(action)
            }
            else -> return super.handle(action)
        }
    }

    /**
     * Navigate back from the current screen
     * Uses the root coordinator for consistency in back navigation
     */
    override fun navigateBack(): Boolean {
        // Try to navigate back using the parent/root coordinator
        val root = findRootCoordinator()
        if (root?.canNavigateBack() == true) {
            return root.executeBackNavigation()
        }

        // If we can't go back, delegate to parent
        return super.navigateBack()
    }
}