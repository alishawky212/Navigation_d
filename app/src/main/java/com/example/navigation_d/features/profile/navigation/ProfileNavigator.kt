package com.example.navigation_d.features.profile.navigation

import androidx.compose.runtime.Composable
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.contract.ProfileCoordinatorAction
import com.example.navigation_d.navigation.contract.CoordinatorAction
import com.example.navigation_d.navigation.contract.GeneralAction
import javax.inject.Inject
import javax.inject.Singleton
import javax.inject.Named

/**
 * Interface for handling navigation within the Profile feature with parent-child hierarchy
 * Uses the reference coordinator pattern
 */
interface ProfileNavigator : Coordinator {
    /**
     * Handle profile-specific actions
     */
    fun handleProfileAction(action: ProfileCoordinatorAction)
}

/**
 * Implementation of ProfileNavigator with pure action-based navigation
 */
@Singleton
class ProfileNavigatorImpl @Inject constructor(
    private val navigator: Navigator,
    @Named("RootCoordinator") override val parent: Coordinator?
) : ProfileNavigator {

    @Composable
    override fun handle(action: CoordinatorAction) {
        when (action) {
            is ProfileCoordinatorAction -> handleProfileAction(action)
            else -> parent?.handle(action)
        }
    }

    override fun handleProfileAction(action: ProfileCoordinatorAction) {
        when (action) {
            is ProfileCoordinatorAction.ShowProfile -> {
                navigator.navigateTo("profile_screen")
            }
            is ProfileCoordinatorAction.ShowSettings -> {
                navigator.navigateTo("settings_screen")
            }
            is ProfileCoordinatorAction.ShowEditProfile -> {
                // Navigate to edit profile screen (placeholder)
                // navigator.navigateTo("edit_profile")
            }
            is ProfileCoordinatorAction.ShowSavedItems -> {
                // Navigate to saved items screen (placeholder)
                // navigator.navigateTo("saved_items")
            }
            is ProfileCoordinatorAction.ShowOrderHistory -> {
                navigator.navigateTo("orders_graph")
            }
        }
    }

    override fun navigate(route: String) {
        navigator.navigateTo(route)
    }
}
