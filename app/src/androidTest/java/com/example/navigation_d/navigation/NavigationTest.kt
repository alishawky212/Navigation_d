package com.example.navigation_d.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.navigation_d.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testLoginScreenIsDisplayedOnAppStart() {
        // Verify login screen is displayed initially
        composeTestRule.onNodeWithText("Login Screen").assertIsDisplayed()
    }

    @Test
    fun testNavigationToSettingsScreen() {
        // Navigate to settings screen
        composeTestRule.onNodeWithText("Settings").performClick()

        // Verify we're on the settings screen
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
    }

    @Test
    fun testNavigationToMainScreenAfterLogin() {
        // Enter username
        composeTestRule.onNodeWithText("Username").performTextInput("testUser")

        // Navigate to main screen by clicking login
        composeTestRule.onNodeWithText("Login").performClick()

        // Verify we're on the main screen
        composeTestRule.onNodeWithText("Welcome to the App!").assertIsDisplayed()
    }

    @Test
    fun testNavigationToOrdersScreen() {
        // First login
        composeTestRule.onNodeWithText("Username").performTextInput("testUser")
        composeTestRule.onNodeWithText("Login").performClick()

        // Navigate to orders screen
        composeTestRule.onNodeWithText("View Orders").performClick()

        // Verify we're on the orders screen
        composeTestRule.onNodeWithText("Orders").assertIsDisplayed()
    }

    @Test
    fun testOrdersListToDetailsNavigation() {
        // First login
        composeTestRule.onNodeWithText("Username").performTextInput("testUser")
        composeTestRule.onNodeWithText("Login").performClick()

        // Navigate to orders screen
        composeTestRule.onNodeWithText("View Orders").performClick()

        // Click on first order
        composeTestRule.onNodeWithText("Order #1001").performClick()

        // Verify we're on the order details screen
        composeTestRule.onNodeWithText("Order Summary").assertIsDisplayed()
    }

    @Test
    fun testBackNavigationFromOrdersToMain() {
        // First login
        composeTestRule.onNodeWithText("Username").performTextInput("testUser")
        composeTestRule.onNodeWithText("Login").performClick()

        // Navigate to orders screen
        composeTestRule.onNodeWithText("View Orders").performClick()

        // Navigate back
        composeTestRule.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        // Verify we're back on the main screen
        composeTestRule.onNodeWithText("Welcome to the App!").assertIsDisplayed()
    }
}