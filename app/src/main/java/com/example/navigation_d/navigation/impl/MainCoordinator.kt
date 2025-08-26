package com.example.navigation_d.navigation.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.NavigationRoutes
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import com.example.navigation_d.navigation.contract.OrdersCoordinatorAction
import com.example.navigation_d.ui.screens.MainScreen
import dagger.Lazy
import javax.inject.Inject

/**
 * Coordinator for the main app flow
 * Acts as a host coordinator for the orders flow
 * Based on the Coordinator pattern to separate navigation logic from UI
 */
class MainCoordinator @Inject constructor(
    override val parent: Lazy<Coordinator>,
    private val ordersCoordinatorProvider: Lazy<OrdersCoordinator>
) : HostCoordinator {

    // Track active child coordinator
    override var activeCoordinator: Coordinator? = null

    /**
     * Initialize the flow, usually by navigating to the start destination
     */
    fun start() {
        // Navigate to main screen
        handle(MainCoordinatorAction.ShowMainScreen)
    }

    /**
     * Set up the main navigation graph
     */
    override fun setupNavigation(builder: NavGraphBuilder) {
        // Create a nested navigation graph for the main flow
        builder.navigation(
            startDestination = NavigationRoutes.Main.MAIN_SCREEN,
            route = NavigationRoutes.MAIN_GRAPH
        ) {
            // Main screen
            composable(NavigationRoutes.Main.MAIN_SCREEN) {
                MainScreen(
                    onOrdersClick = {
                        // Navigate to orders flow
                        handle(MainCoordinatorAction.ShowOrders)
                    },
                    onProfileClick = {
                        // Navigate to profile (not implemented in this example)
                        handle(MainCoordinatorAction.ShowProfile)
                    },
                    onLogoutClick = {
                        // Logout and return to auth flow
                        handle(MainCoordinatorAction.Logout)
                    }
                )
            }

            // Set up orders coordinator navigation
            ordersCoordinatorProvider.get().setupNavigation(this)
        }
    }

    /**
     * Handle main flow actions and navigation events
     */
    override fun handle(action: CoordinatorAction): Boolean {
        // First check if active coordinator can handle the action
        activeCoordinator?.let {
            // Avoid infinite recursion by not delegating OrdersCoordinatorAction.BackToMain
            // back to the OrdersCoordinator
            if (!(it is OrdersCoordinator && action is OrdersCoordinatorAction.BackToMain)) {
                if (it.handle(action)) return true
            }
        }

        // Then handle main-specific actions
        when (action) {
            is MainCoordinatorAction.ShowMainScreen -> {
                navigate(NavigationRoutes.Main.MAIN_SCREEN)
                return true
            }

            is MainCoordinatorAction.ShowOrders -> {
                // Activate orders coordinator and navigate to orders list
                val ordersCoordinator = ordersCoordinatorProvider.get()
                activateCoordinator(ordersCoordinator)
                ordersCoordinator.handle(OrdersCoordinatorAction.ShowOrdersList)
                return true
            }

            is MainCoordinatorAction.Logout -> {
                // Delegate to parent (AppCoordinator) to handle logout
                return parent.get().handle(action)
            }

            else -> return super.handle(action)
        }
    }

    /**
     * Navigate back from the current screen
     * Handles delegation to child coordinators and parent as needed
     */
    override fun navigateBack(): Boolean {
       activeCoordinator = null
        return super.navigateBack()
    }

    /**
     * Activate a child coordinator
     */
    override fun activateCoordinator(coordinator: Coordinator) {
        activeCoordinator = coordinator
    }

    /**
     * Deactivate the current child coordinator
     */
    override fun deactivateCoordinator() {
        activeCoordinator = null
    }
}