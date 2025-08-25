package com.example.navigation_d.features.main.coordinator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.features.main.navigation.mainGraph
import com.example.navigation_d.features.orders.coordinator.OrdersCoordinator
import com.example.navigation_d.features.orders.navigation.ordersGraph
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.GeneralAction
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import com.example.navigation_d.navigation.contract.OrdersCoordinatorAction
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named

/**
 * Main Coordinator interface for ViewModels - provides type-safe navigation methods
 * Extends HostCoordinator for hierarchy consistency
 */
interface MainCoordinator : HostCoordinator {
    fun handleMainAction(action: MainCoordinatorAction)
}

/**
 * Implementation of MainCoordinator with pure action-based navigation
 */
@Singleton
class MainCoordinatorImpl @Inject constructor(
    @Named("RootCoordinator") override val parent: Lazy<Coordinator>?,
    @Named("OrdersCoordinator") private val ordersCoordinator: Lazy<OrdersCoordinator>
) : MainCoordinator {

    private var _activeCoordinator by mutableStateOf<Coordinator?>(null)

    override var rootBuilder: NavGraphBuilder? = null

    override fun setupNavigation(builder: NavGraphBuilder) {
        builder.mainGraph()
        // Include orders as nested graph within main
        builder.ordersGraph()
    }

    override fun handle(action: CoordinatorAction) {
         when {
            action is MainCoordinatorAction -> {
                handleMainAction(action)
            }
            else -> parent?.get()?.handle(action) ?: false
        }
    }

    override fun handleMainAction(action: MainCoordinatorAction) {
        if (_activeCoordinator != null){
            _activeCoordinator?.handle(action)
            return
        }
        when (action) {
            is MainCoordinatorAction.ShowMainScreen -> {
               navigate("main_screen")
            }
            is MainCoordinatorAction.ShowOrders -> {
                _activeCoordinator = ordersCoordinator.get()
                navigate("orders_graph")
            }
            is MainCoordinatorAction.ShowProfile -> {
                navigate("profile_screen")
            }
            is MainCoordinatorAction.Logout -> {
                navigate("auth_graph")
            }
            else -> parent?.get()?.handle(action)
        }
        if(rootBuilder != null)
            _activeCoordinator?.setupNavigation(rootBuilder!!)
    }

    override val activeCoordinator: Coordinator?
        get() = _activeCoordinator
}

