package com.example.navigation_d.features.main.coordinator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.navigation_d.navigation.NavHostBuilder
import com.example.navigation_d.features.orders.coordinator.OrdersCoordinatorImpl
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named

/**
 * Implementation of MainCoordinator with pure action-based navigation
 */
@Singleton
class MainCoordinatorImpl @Inject constructor(
    @Named("RootCoordinator") override val parent: Coordinator?,
    private val ordersCoordinator: OrdersCoordinatorImpl
) : HostCoordinator {


    private var _activeCoordinator by mutableStateOf<Coordinator?>(null)

    override var rootBuilder: NavHostBuilder? = null

    override fun setupNavigation(builder: NavHostBuilder) {
        builder.composable("main_screen") {
            // MainScreen composable will be added here
        }
        builder.composable("profile_screen") {
            // ProfileScreen composable will be added here
        }
        rootBuilder = builder
    }
    @Composable
    override fun handle(action: CoordinatorAction) {
        when (action) {
            is MainCoordinatorAction -> handleMainAction(action)
            else -> parent?.handle(action)
        }
    }


    private fun handleMainAction(action: MainCoordinatorAction) {
        when (action) {
            is MainCoordinatorAction.ShowMainScreen -> {
               navigate("main_screen")
            }
            is MainCoordinatorAction.ShowOrders -> {
                _activeCoordinator = ordersCoordinator
                navigate("orders_graph")
            }
            is MainCoordinatorAction.ShowProfile -> {
                navigate("profile_screen")
            }
            is MainCoordinatorAction.Logout -> {
                navigate("auth_graph")
            }
        }
        if(rootBuilder != null)
            _activeCoordinator?.setupNavigation(rootBuilder!!)
    }

    override val activeCoordinator: Coordinator?
        get() = _activeCoordinator
}

