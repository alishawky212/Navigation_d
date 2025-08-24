package com.example.navigation_d.navigation.contract

import com.example.navigation_d.navigation.Coordinator

/**
 * Interface for handling navigation within the Onboarding feature
 * Uses the reference coordinator pattern
 */
interface OnboardingNavigator : Coordinator {
    fun navigateToWelcome()
    fun navigateToLogin()
    fun navigateToSignUp()
    fun completeOnboarding()
}
