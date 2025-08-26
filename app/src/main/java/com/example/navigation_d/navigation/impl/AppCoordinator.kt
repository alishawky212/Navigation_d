package com.example.navigation_d.navigation.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.NavigationRoutes
import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.contract.AppCoordinatorAction
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import com.example.navigation_d.navigation.contract.RootCoordinator
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Root coordinator that manages navigation between auth and main flows
 * Implements the RootCoordinator interface and serves as the top-level coordinator
 */
@Singleton
class AppCoordinator @Inject constructor(
    override val navigator: Navigator
) : RootCoordinator {

    // No parent as this is the root coordinator
    override val parent: Lazy<Coordinator>? = null

    // Track active child coordinator
    override var activeCoordinator: Coordinator? = null
        private set

    // Remember the latest navigation controller for reuse
    private var navController: NavHostController? = null

    // Child coordinators
    private val authCoordinator: AuthCoordinator by lazy {
        AuthCoordinator(dagger.Lazy { this as Coordinator })
    }

    private val mainCoordinator: MainCoordinator by lazy {
        MainCoordinator(
            parent = dagger.Lazy { this as Coordinator },
            ordersCoordinatorProvider = dagger.Lazy { ordersCoordinator }
        )
    }

    private val ordersCoordinator: OrdersCoordinator by lazy {
        OrdersCoordinator(dagger.Lazy { mainCoordinator as Coordinator })
    }



    @Composable
    override fun start(action: AppCoordinatorAction) {
        // Remember the starting action to handle app state changes
        val startAction = remember { action }

        // Set up the navigation controller
        val controller = rememberNavController()
        navController = controller
        navigator.setNavController(controller)

        // Handle the starting action
        handle(startAction)
    }

    @Composable
    override fun renderNavHost() {
        // Make sure we have a controller
        val controller = navController
            ?: throw IllegalStateException("NavHostController not set. Make sure to call start() first.")

        // Set up the main navigation host that will contain all other nav graphs
        NavHost(
            navController = controller,
            startDestination = NavigationRoutes.AUTH_GRAPH
        ) {
            // Set up auth coordinator navigation
            authCoordinator.setupNavigation(this)

            // Set up main coordinator navigation
            mainCoordinator.setupNavigation(this)
        }
    }

    private fun activateAuthFlow() {
        activateCoordinator(authCoordinator)
        navigator.navigateTo(NavigationRoutes.AUTH_GRAPH)
    }

    private fun activateMainFlow() {
        activateCoordinator(mainCoordinator)
        navigator.navigateTo(NavigationRoutes.MAIN_GRAPH)
    }

    override fun handle(action: CoordinatorAction): Boolean {
        // Then handle app-level actions
        when (action) {
            is AppCoordinatorAction.StartAuthFlow -> {
                activateAuthFlow()
                return true
            }
            is AppCoordinatorAction.StartMainFlow -> {
                activateMainFlow()
                return true
            }
            is AuthCoordinatorAction.LoginSuccess -> {
                activateMainFlow()
                return true
            }
            is MainCoordinatorAction.Logout -> {
                activateAuthFlow()
                return true
            }
            else -> return super.handle(action)
        }
    }

    override fun activateCoordinator(coordinator: Coordinator) {
        activeCoordinator = coordinator
    }

    override fun deactivateCoordinator() {
        activeCoordinator = null
    }

    /**
     * Execute navigation to the specified route
     * This is the central navigation method that all other coordinators delegate to
     */
    override fun executeNavigation(route: String, params: Any?) {
        navigator.navigateTo(route, params)
    }

    /**
     * Execute back navigation
     * @return true if navigation was successful
     */
    override fun executeBackNavigation(): Boolean {
        return navigator.popBackStack()
    }

    /**
     * Check if back navigation is possible
     * @return true if back navigation is possible
     */
    override fun canNavigateBack(): Boolean {
        return navigator.canGoBack()
    }

    /**
     * Override the navigate method to use our executeNavigation
     */
    override fun navigate(route: String) {
        executeNavigation(route, null)
    }

    /**
     * Override the navigate method with params to use our executeNavigation
     */
    override fun navigate(route: String, params: Any?) {
        executeNavigation(route, params)
    }

    /**
     * Override navigateBack to use our executeBackNavigation
     */
    override fun navigateBack(): Boolean {
        return executeBackNavigation()
    }
}