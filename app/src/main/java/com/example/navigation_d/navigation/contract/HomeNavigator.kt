package com.example.navigation_d.navigation.contract

import com.example.navigation_d.navigation.Coordinator

/**
 * Interface for handling navigation within the Home feature
 * Uses the reference coordinator pattern
 */
interface HomeNavigator : Coordinator {
    fun navigateToDashboard()
    fun navigateToNotifications()
    fun navigateToSettings()
    fun navigateToItemDetails(itemId: String)
}
