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
import android.util.Log
import com.example.navigation_d.navigation.NavigationRoutes

/**
 * Implementation of Orders coordinator with pure action-based navigation
 * Uses the base Coordinator interface directly
 */
@Singleton
class OrdersCoordinator @Inject constructor(
    @Named("MainCoordinator") override val parent: Lazy<Coordinator>?
) : Coordinator {

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
                navigate(NavigationRoutes.Orders.ORDERS_LIST)
            }
            is OrdersCoordinatorAction.ShowOrderDetails -> {
                navigate(NavigationRoutes.Orders.orderDetailsWithId(action.orderId))
            }
            is OrdersCoordinatorAction.BackToMain -> {
                // Let parent handle going back to main
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
        Log.d("OrdersCoordinator", "navigateBack called")
        // Delegate back navigation to parent coordinator
        return parent?.get()?.navigateBack() ?: false
    }

    override fun navigateUp(): Boolean {
        // Delegate up navigation to parent coordinator
        return parent?.get()?.navigateUp() ?: false
    }

    override fun canNavigateBack(): Boolean {
        // Delegate back navigation capability check to parent
        return parent?.get()?.canNavigateBack() ?: false
    }
}
