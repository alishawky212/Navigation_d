package com.example.navigation_d.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleNavigatorTest {

    @Test
    fun testInitialNavigatorState() {
        val navigator = Navigator()
        assertEquals("auth_graph", navigator.route)
    }
}