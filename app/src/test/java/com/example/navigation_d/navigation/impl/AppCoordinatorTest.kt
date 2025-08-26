package com.example.navigation_d.navigation.impl

import androidx.navigation.NavHostController
import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.contract.AppCoordinatorAction
import com.example.navigation_d.navigation.contract.AuthCoordinatorAction
import com.example.navigation_d.navigation.contract.MainCoordinatorAction
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AppCoordinatorTest {

    @Mock
    private lateinit var mockNavigator: Navigator

    @Mock
    private lateinit var mockNavController: NavHostController

    @Mock
    private lateinit var mockAuthCoordinator: AuthCoordinator

    @Mock
    private lateinit var mockMainCoordinator: MainCoordinator

    private lateinit var appCoordinator: AppCoordinator

    @Before
    fun setup() {
        // Create the coordinator under test with mocked dependencies
        appCoordinator = AppCoordinator(mockNavigator)

        // Mock necessary behavior
        // Set up the coordinator's child coordinators using test doubles
        val authCoordinatorField = AppCoordinator::class.java.getDeclaredField("authCoordinator")
        authCoordinatorField.isAccessible = true
        authCoordinatorField.set(appCoordinator, mockAuthCoordinator)

        val mainCoordinatorField = AppCoordinator::class.java.getDeclaredField("mainCoordinator")
        mainCoordinatorField.isAccessible = true
        mainCoordinatorField.set(appCoordinator, mockMainCoordinator)
    }

    @Test
    fun `should activate auth flow when handling StartAuthFlow action`() {
        // When
        appCoordinator.handle(AppCoordinatorAction.StartAuthFlow)

        // Then
        verify(mockNavigator).navigateTo("auth_graph")
    }

    @Test
    fun `should activate main flow when handling StartMainFlow action`() {
        // When
        appCoordinator.handle(AppCoordinatorAction.StartMainFlow)

        // Then
        verify(mockNavigator).navigateTo("main_graph")
    }

    @Test
    fun `should activate main flow when handling LoginSuccess action`() {
        // When
        appCoordinator.handle(AuthCoordinatorAction.LoginSuccess("user123"))

        // Then
        verify(mockNavigator).navigateTo("main_graph")
    }

    @Test
    fun `should activate auth flow when handling Logout action`() {
        // When
        appCoordinator.handle(MainCoordinatorAction.Logout)

        // Then
        verify(mockNavigator).navigateTo("auth_graph")
    }
}