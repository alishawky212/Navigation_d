package com.example.navigation_d.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.navigation_d.navigation.NavigationRoutes.AUTH_GRAPH
import javax.inject.Inject

class Navigator @Inject constructor() {

    private data class Route(val name: String, val param: Any?)

    private var _route by mutableStateOf(Route(AUTH_GRAPH, null))

    val route get() = _route.name
    val param get() = _route.param

    fun navigateTo(route: String, param: Any? = null) {
        _route = Route(route, param)
    }

    // Reset method to clear the navigation stack
    fun resetTo(route: String) {
        _route = Route(route, null)
    }
}