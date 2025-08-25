package com.example.navigation_d.features.orders.coordinator

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.features.main.coordinator.MainCoordinatorImpl
import com.example.navigation_d.features.orders.navigation.ordersGraph
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.contract.OrdersCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


/**
 * Orders Coordinator interface for ViewModels - provides type-safe navigation methods
 * Extends base Coordinator for hierarchy consistency
 */
interface OrdersCoordinator : Coordinator {
    fun handleOrdersAction(action: OrdersCoordinatorAction)
}

/**
 * Implementation of OrdersCoordinator with pure action-based navigation
 */
@Singleton
class OrdersCoordinatorImpl @Inject constructor(
    @Named("MaineCoordinator") override val parent: Lazy<Coordinator>?
) : OrdersCoordinator {

    override fun setupNavigation(builder: NavGraphBuilder) {
       builder.ordersGraph()
    }

    override fun handle(action: CoordinatorAction) {
         when (action) {
            is OrdersCoordinatorAction -> {
                handleOrdersAction(action)
            }
            else -> parent?.get()?.handle(action)
        }
    }

    override fun handleOrdersAction(action: OrdersCoordinatorAction) {
        when (action) {
            is OrdersCoordinatorAction.ShowOrdersList -> {
                navigate("orders_list_screen")
            }
            is OrdersCoordinatorAction.ShowOrderDetails -> {
                navigate("order_details_screen", action.orderId)
            }
            is OrdersCoordinatorAction.BackToMain -> {
                // Use global back action
            }
        }
    }
}

