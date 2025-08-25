package com.example.navigation_d.features.orders.coordinator

import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.features.orders.navigation.ordersGraph
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.NavigationAction
import com.example.navigation_d.navigation.contract.OrdersCoordinatorAction
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Implementation of Orders coordinator with pure action-based navigation
 * Uses the base Coordinator interface directly
 */
@Singleton
class OrdersCoordinator @Inject constructor(
    @Named("MainCoordinator") override val parent: Lazy<Coordinator>?
) : Coordinator {

    // Track the current screen within the orders flow
    private var currentScreen: String = "orders_list_screen"
    private var navigationHistory = mutableListOf<String>()

    override fun setupNavigation(builder: NavGraphBuilder) {
        builder.ordersGraph()
    }

    override fun handle(action: CoordinatorAction): Boolean {
        return when (action) {
            is OrdersCoordinatorAction -> {
                handleOrdersAction(action)
                true
            }
            is NavigationAction -> {
                when (action) {
                    is NavigationAction.Back -> navigateBack()
                    is NavigationAction.Up -> navigateUp()
                    is NavigationAction.Home -> parent?.get()?.handle(action) ?: false
                }
            }
            else -> parent?.get()?.handle(action) ?: false
        }
    }

    /**
     * Handle Orders-specific actions
     */
    private fun handleOrdersAction(action: OrdersCoordinatorAction) {
        when (action) {
            is OrdersCoordinatorAction.ShowOrdersList -> {
                // If we're already showing details, add to history
                if (currentScreen != "orders_list_screen") {
                    navigationHistory.add(currentScreen)
                }
                currentScreen = "orders_list_screen"
                navigate("orders_list_screen")
            }
            is OrdersCoordinatorAction.ShowOrderDetails -> {
                // Add current screen to history before navigating
                navigationHistory.add(currentScreen)
                currentScreen = "order_details_screen"
                navigate("order_details_screen", action.orderId)
            }
            is OrdersCoordinatorAction.BackToMain -> {
                // Clear our history and go back to main
                navigationHistory.clear()
                currentScreen = "orders_list_screen"
                // Let parent handle it
                parent?.get()?.navigateUp() ?: false
            }
        }
    }

    override fun navigate(route: String) {
        parent?.get()?.navigate(route)
    }

    override fun navigate(route: String, params: Any?) {
        parent?.get()?.navigate(route, params)
    }

    override fun navigateBack(): Boolean {
        // If we have internal navigation history, go back
        if (navigationHistory.isNotEmpty()) {
            currentScreen = navigationHistory.removeLastOrNull() ?: "orders_list_screen"
            navigate(currentScreen)
            return true
        }

        // If we're on the main orders screen with no history, delegate to parent
        return if (currentScreen == "orders_list_screen") {
            parent?.get()?.navigateBack() ?: false
        } else {
            // Otherwise go back to orders list
            currentScreen = "orders_list_screen"
            navigate(currentScreen)
            true
        }
    }

    override fun navigateUp(): Boolean {
        // Up navigation always goes to the orders list
        if (currentScreen != "orders_list_screen") {
            currentScreen = "orders_list_screen"
            navigate(currentScreen)
            return true
        }
        // If already on orders list, delegate to parent
        return parent?.get()?.navigateUp() ?: false
    }

    override fun canNavigateBack(): Boolean {
        // We can navigate back if we have history or aren't on the list screen
        return navigationHistory.isNotEmpty() || currentScreen != "orders_list_screen"
    }
}
