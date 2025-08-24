package com.example.navigation_d.features.orders.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.navigation_d.features.orders.screens.OrderDetailsScreen
import com.example.navigation_d.features.orders.screens.OrdersListScreen
import com.example.navigation_d.navigation.NavigationRoutes

/**
 * Orders navigation graph - Handles order-related screens
 * ViewModels use injected OrdersCoordinator for navigation logic
 */
fun NavGraphBuilder.ordersGraph() {
    navigation(
        route = NavigationRoutes.ORDERS_GRAPH,
        startDestination = NavigationRoutes.Orders.ORDERS_LIST
    ) {
        composable(NavigationRoutes.Orders.ORDERS_LIST) {
            OrdersListScreen()
        }
        
        composable("${NavigationRoutes.Orders.ORDER_DETAILS}/{orderId}") {
            OrderDetailsScreen()
        }
    }
}
