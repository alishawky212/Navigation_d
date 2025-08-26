package com.example.navigation_d.navigation.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.NavigationRoutes
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.OrdersCoordinatorAction
import com.example.navigation_d.ui.screens.OrderDetailsScreen
import com.example.navigation_d.ui.screens.OrdersListScreen
import dagger.Lazy
import javax.inject.Inject

/**
 * Coordinator for the orders flow
 * Handles navigation between orders list and order details
 * Based on the Coordinator pattern to separate navigation logic from UI
 */
class OrdersCoordinator @Inject constructor(
    override val parent: Lazy<Coordinator>
) : Coordinator {

    /**
     * Initialize the flow, usually by navigating to the start destination
     */
    fun start() {
        // Navigate to orders list screen
        handle(OrdersCoordinatorAction.ShowOrdersList)
    }

    /**
     * Set up the orders navigation graph
     */
    override fun setupNavigation(builder: NavGraphBuilder) {
        // Create a nested navigation graph for the orders flow
        builder.navigation(
            startDestination = NavigationRoutes.Orders.ORDERS_LIST,
            route = NavigationRoutes.ORDERS_GRAPH
        ) {
            // Orders list screen
            composable(NavigationRoutes.Orders.ORDERS_LIST) {
                OrdersListScreen(
                    onOrderClick = { orderId ->
                        // Navigate to order details
                        handle(OrdersCoordinatorAction.ShowOrderDetails(orderId))
                    },
                    onBackClick = {
                        // Simply navigate back by popping the back stack
                        // This is more generic and doesn't require specific coordinator handling
                        navigateBack()
                    }
                )
            }

            // Order details screen with orderId parameter
            composable(
                route = "${NavigationRoutes.Orders.ORDER_DETAILS}/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { backStackEntry ->
                // Extract the orderId from the route
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""

                OrderDetailsScreen(
                    orderId = orderId,
                    onBackClick = {
                        // Navigate back to orders list
                        navigateBack()
                    }
                )
            }
        }
    }

    /**
     * Handle orders-specific actions and navigation events
     */
    override fun handle(action: CoordinatorAction): Boolean {
        // Handle orders-specific actions
        when (action) {
            is OrdersCoordinatorAction.ShowOrdersList -> {
                navigate(NavigationRoutes.Orders.ORDERS_LIST)
                return true
            }

            is OrdersCoordinatorAction.ShowOrderDetails -> {
                // Navigate to order details with the orderId parameter
                val orderDetailsRoute = "${NavigationRoutes.Orders.ORDER_DETAILS}/${action.orderId}"
                navigate(orderDetailsRoute)
                return true
            }

            is OrdersCoordinatorAction.BackToMain -> {
                // IMPORTANT: Do NOT call navigateBack() here - that would cause an infinite loop
                // Instead, directly delegate to parent
                return navigateBack()
            }

            else -> return super.handle(action)
        }
    }

    /**
     * Navigate back from the current screen
     * Uses the root coordinator for consistency in back navigation
     */
    override fun navigateBack(): Boolean {
        // Get the current route to determine the right back action
        val root = findRootCoordinator()
        val currentRoute = root?.navigator?.route ?: ""

        // If we're on the orders list screen, we should navigate back to main
        if (currentRoute == NavigationRoutes.Orders.ORDERS_LIST) {
            // When leaving the orders flow, directly call parent's handle with BackToMain action
            // This avoids the recursive loop through our own handle method
            return parent.get().navigateBack()
        }

        // For any other screen in this flow, just do normal back navigation
        if (root?.canNavigateBack() == true) {
            return root.executeBackNavigation()
        }

        // If we can't go back, delegate to parent
        return super.navigateBack()
    }
}