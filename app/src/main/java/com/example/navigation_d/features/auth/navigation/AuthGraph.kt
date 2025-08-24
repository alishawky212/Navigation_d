package com.example.navigation_d.features.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.navigation_d.features.auth.screens.LoginScreen
import com.example.navigation_d.features.profile.screens.SettingsScreen
import com.example.navigation_d.navigation.NavigationRoutes

/**
 * Auth navigation graph - Handles authentication flow
 * ViewModels use injected AuthCoordinator for navigation logic
 */
fun NavGraphBuilder.authGraph() {
    navigation(
        route = NavigationRoutes.AUTH_GRAPH,
        startDestination = NavigationRoutes.Auth.LOGIN
    ) {
        composable(NavigationRoutes.Auth.LOGIN) {
            LoginScreen()
        }
        
        composable(NavigationRoutes.Auth.SETTINGS) {
            SettingsScreen()
        }
    }
}
