package com.example.navigation_d.features.main.coordinator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.features.main.navigation.mainGraph
import com.example.navigation_d.features.orders.navigation.ordersGraph
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import com.example.navigation_d.navigation.contract.NavigationAction
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named

/**
 * Implementation of Main coordinator with pure action-based navigation
 * Uses the HostCoordinator interface directly instead of a specific interface
 */
@Singleton
class MainCoordinator @Inject constructor(
    @Named("RootCoordinator") override val parent: Lazy<Coordinator>?,
    @Named("OrdersCoordinator") private val ordersCoordinator: Lazy<Coordinator>
) : HostCoordinator {

    private var _activeCoordinator by mutableStateOf<Coordinator?>(null)

    override var rootBuilder: NavGraphBuilder? = null

    override fun setupNavigation(builder: NavGraphBuilder) {
        builder.mainGraph()
        // Include orders as nested graph within main
        builder.ordersGraph()
    }

    override fun handle(action: CoordinatorAction): Boolean {
        return when {
            // If we have an active child coordinator, try it first
            _activeCoordinator != null -> {
                _activeCoordinator!!.handle(action) || handleMainActionInternal(action)
            }
            // Handle main-specific actions
            action is MainCoordinatorAction -> {
                handleMainAction(action)
                true
            }
            // Check for navigation actions
            action is NavigationAction -> {
                when (action) {
                    is NavigationAction.Back -> navigateBack()
                    is NavigationAction.Up -> navigateUp()
                    is NavigationAction.Home -> {
                        // If we're already on main screen, do nothing
                        if (_activeCoordinator == null) {
                            navigate("main_screen")
                            true
                        } else {
                            // Deactivate child and go to main screen
                            deactivateCoordinator()
                            navigate("main_screen")
                            true
                        }
                    }
                }
                true
            }
            // Delegate to parent for unknown actions
            else -> parent?.get()?.handle(action) ?: false
        }
    }

    /**
     * Handle Main-specific actions
     */
    private fun handleMainAction(action: MainCoordinatorAction) {
        when (action) {
            is MainCoordinatorAction.ShowMainScreen -> {
                deactivateCoordinator() // Deactivate child coordinator
                navigate("main_screen")
            }
            is MainCoordinatorAction.ShowOrders -> {
                activateCoordinator(ordersCoordinator.get())
                navigate("orders_graph")
            }
            is MainCoordinatorAction.ShowProfile -> {
                deactivateCoordinator() // Deactivate child coordinator
                navigate("profile_screen")
            }
            is MainCoordinatorAction.Logout -> {
                deactivateCoordinator() // Clean up child coordinator
                navigate("auth_graph")
            }
        }
    }

    /**
     * Internal helper for handling main actions with boolean return
     */
    private fun handleMainActionInternal(action: CoordinatorAction): Boolean {
        return if (action is MainCoordinatorAction) {
            handleMainAction(action)
            true
        } else false
    }

    override val activeCoordinator: Coordinator?
        get() = _activeCoordinator

    override fun activateCoordinator(coordinator: Coordinator) {
        _activeCoordinator = coordinator
    }

    override fun deactivateCoordinator() {
        _activeCoordinator = null
    }

    override fun navigateBack(): Boolean {
        // First let child coordinator try to handle back
        if (_activeCoordinator?.navigateBack() == true) {
            return true
        }

        // If child exists but couldn't handle back, deactivate it and return to main screen
        if (_activeCoordinator != null) {
            deactivateCoordinator()
            navigate("main_screen")
            return true
        }

        // Otherwise delegate to parent
        return parent?.get()?.navigateBack() ?: false
    }

    override fun navigateUp(): Boolean {
        // In MainCoordinator, up always means go back to main screen
        if (_activeCoordinator != null) {
            deactivateCoordinator()
            navigate("main_screen")
            return true
        }
        return false
    }

    override fun canNavigateBack(): Boolean {
        // We can navigate back if child can navigate back or if we have an active child
        return _activeCoordinator?.canNavigateBack() == true || _activeCoordinator != null
    }
}
