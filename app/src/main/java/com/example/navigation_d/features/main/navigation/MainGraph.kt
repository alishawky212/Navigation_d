package com.example.navigation_d.features.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.navigation_d.features.main.screens.MainScreen
import com.example.navigation_d.navigation.NavigationRoutes

/**
 * Main navigation graph - Handles main app flow
 * ViewModels use injected MainCoordinator for navigation logic
 */
fun NavGraphBuilder.mainGraph() {
    navigation(
        route = NavigationRoutes.MAIN_GRAPH,
        startDestination = NavigationRoutes.Main.MAIN_SCREEN
    ) {
        composable(NavigationRoutes.Main.MAIN_SCREEN) {
            MainScreen()
        }
    }
}
