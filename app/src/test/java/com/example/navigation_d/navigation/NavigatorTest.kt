package com.example.navigation_d.navigation

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.testing.TestNavHostController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NavigatorTest {

    private lateinit var navigator: Navigator

    @Mock
    private lateinit var mockNavController: NavHostController

    @Before
    fun setup() {
        navigator = Navigator()
        navigator.setNavController(mockNavController)
    }

    @Test
    fun `navigateTo should call navigate on NavController`() {
        // When
        navigator.navigateTo("test_route")

        // Then
        verify(mockNavController).navigate("test_route")
    }

    @Test
    fun `popBackStack should call popBackStack on NavController`() {
        // Given
        `when`(mockNavController.popBackStack()).thenReturn(true)

        // When
        val result = navigator.popBackStack()

        // Then
        verify(mockNavController).popBackStack()
        assert(result)
    }

    @Test
    fun `canGoBack should check previousBackStackEntry`() {
        // Given
        `when`(mockNavController.previousBackStackEntry).thenReturn(null)

        // When
        val result = navigator.canGoBack()

        // Then
        assert(!result)
    }
}