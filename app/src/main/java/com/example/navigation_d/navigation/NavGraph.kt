package com.example.navigation_d.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.navigation_d.features.auth.navigation.authGraph
import com.example.navigation_d.features.main.navigation.mainGraph
import com.example.navigation_d.features.orders.navigation.ordersGraph
import com.example.navigation_d.features.profile.screens.ProfileScreen

/**
 * Main navigation graph for the application - Coordinator + ViewModel Pattern Implementation
 * ViewModels use Coordinators for navigation logic, maintaining the coordinator hierarchy
 */
@Composable
fun MainNavGraph(
    navController: NavHostController,
    startDestination: String = NavigationRoutes.AUTH_GRAPH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Auth Graph - ViewModels use injected AuthCoordinator for navigation
        authGraph()
        
        // Main Graph - ViewModels use injected MainCoordinator for navigation
        mainGraph()
        
        // Orders Graph - ViewModels use injected OrdersCoordinator for navigation
        ordersGraph()
        
        // Profile Screen (standalone)
        composable(NavigationRoutes.Profile.PROFILE_SCREEN) {
            ProfileScreen()
        }
    }
}

