package com.example.navigation_d.features.auth.coordinator

import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.features.auth.navigation.authGraph
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named

/**
 * Implementation of Auth coordinator with pure action-based navigation
 * Uses the base Coordinator interface directly
 */
@Singleton
class AuthCoordinator @Inject constructor(
    @Named("RootCoordinator") override val parent: Lazy<Coordinator>?
) : Coordinator {

    override fun handle(action: CoordinatorAction): Boolean {
        return when (action) {
            is AuthCoordinatorAction -> {
                handleAuthAction(action)
                true
            }
            else -> parent?.get()?.handle(action) ?: false
        }
    }

    override fun setupNavigation(builder: NavGraphBuilder) {
        builder.authGraph()
    }

    private var currentScreen: String = ""

    /**
     * Handle Auth-specific actions
     */
    private fun handleAuthAction(action: AuthCoordinatorAction) {
        when (action) {
            is AuthCoordinatorAction.ShowLogin -> {
                currentScreen = "login_screen"
                navigate("login_screen")
            }
            is AuthCoordinatorAction.ShowSettings -> {
                currentScreen = "settings_screen"
                navigate("settings_screen")
            }
            is AuthCoordinatorAction.LoginSuccess -> {
                currentScreen = "main_graph"
                navigate("main_graph")
            }
            is AuthCoordinatorAction.Logout -> {
                currentScreen = "login_screen"
                navigate("login_screen")
            }
        }
    }
}

