package com.example.navigation_d.features.auth.coordinator

import androidx.compose.runtime.Composable
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.NavHostBuilder
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named


/**
 * Implementation of AuthCoordinator with pure action-based navigation
 */
@Singleton
class AuthCoordinatorImpl @Inject constructor(
    @Named("RootCoordinator") override val parent: Coordinator?
) : Coordinator {

    @Composable
    override fun handle(action: CoordinatorAction) {
        when (action) {
            is AuthCoordinatorAction -> handleAuthAction(action)
            else -> parent?.handle(action)
        }
    }

    override fun setupNavigation(builder: NavHostBuilder) {
        builder.composable("login_screen") {
            // LoginScreen composable will be added here
        }
        builder.composable("settings_screen") {
            // SettingsScreen composable will be added here
        }
    }

    private fun handleAuthAction(action: AuthCoordinatorAction) {
        when (action) {
            is AuthCoordinatorAction.ShowLogin -> {
                navigate("login_screen")
            }
            is AuthCoordinatorAction.ShowSettings -> {
                navigate("settings_screen")
            }
            is AuthCoordinatorAction.LoginSuccess -> {
               navigate("main_graph")
            }
            is AuthCoordinatorAction.Logout -> {
                navigate("login_screen")
            }
        }
    }
}

