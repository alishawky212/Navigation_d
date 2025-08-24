package com.example.navigation_d.features.orders.coordinator

import androidx.compose.runtime.Composable
import com.example.navigation_d.features.main.coordinator.MainCoordinatorImpl
import com.example.navigation_d.navigation.NavHostBuilder
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.contract.OrdersCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named


/**
 * Implementation of OrdersCoordinator with pure action-based navigation
 */
@Singleton
class OrdersCoordinatorImpl @Inject constructor(
    override val parent: MainCoordinatorImpl?
) : Coordinator {

    override fun setupNavigation(builder: NavHostBuilder) {
        builder.composable("orders_list_screen") {
            // OrdersListScreen composable will be added here
        }
        builder.composable("order_details_screen") { param ->
            // OrderDetailsScreen composable will be added here
            // param contains the orderId
        }
    }

    @Composable
    override fun handle(action: CoordinatorAction) {
        when (action) {
            is OrdersCoordinatorAction -> handleOrdersAction(action)
            else -> parent?.handle(action)
        }
    }

    private fun handleOrdersAction(action: OrdersCoordinatorAction) {
        when (action) {
            is OrdersCoordinatorAction.ShowOrdersList -> {
                navigate("orders_list_screen")
            }
            is OrdersCoordinatorAction.ShowOrderDetails -> {
                navigate("order_details_screen", action.orderId)
            }
            is OrdersCoordinatorAction.BackToMain -> {
                // Handle back navigation - use navigator directly
               navigate("main_screen")
            }
        }
    }
}

