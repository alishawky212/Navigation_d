package com.example.navigation_d.features.auth.coordinator

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.example.navigation_d.features.auth.navigation.authGraph
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.GeneralAction
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named


/**
 * Auth Coordinator interface for ViewModels - provides type-safe navigation methods
 * Extends base Coordinator for hierarchy consistency
 */
interface AuthCoordinator : Coordinator {
    fun handleAuthAction(action: AuthCoordinatorAction)
}

/**
 * Implementation of AuthCoordinator with pure action-based navigation
 */
@Singleton
class AuthCoordinatorImpl @Inject constructor(
    @Named("RootCoordinator") override val parent: Lazy<Coordinator>?
) : AuthCoordinator {

    override fun handle(action: CoordinatorAction) {
         when (action) {
            is AuthCoordinatorAction -> {
                handleAuthAction(action)
            }
            else -> parent?.get()?.handle(action)
        }
    }

    override fun setupNavigation(builder: NavGraphBuilder) {
        builder.authGraph()
    }

    private var currentScreen: String = ""

    override fun handleAuthAction(action: AuthCoordinatorAction) {
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

