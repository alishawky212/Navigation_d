package com.example.navigation_d.navigation

import androidx.compose.runtime.Composable

class NavHostBuilder {

    data class RouteConfiguration(
        val onActivated: (Any?) -> Unit = {},
        val composable: @Composable () -> Unit
    )

    private val routes = mutableMapOf<String, RouteConfiguration>()

    fun composable(
        screen: String,
        onActivated: (Any?) -> Unit = {},
        composable: @Composable () -> Unit
    ) {
        routes[screen] = RouteConfiguration(onActivated, composable)
    }

    operator fun get(route: String) = routes[route]
    
    fun getAllRoutes(): Map<String, RouteConfiguration> = routes.toMap()
}

@Composable
fun NavHost(
    navigator: Navigator,
    builder: NavHostBuilder.() -> Unit
) {
    val navHostBuilder = NavHostBuilder().apply(builder)
    val currentRoute = navigator.route
    val currentParam = navigator.param
    
    // Find and display the current route's composable
    navHostBuilder[currentRoute]?.let { routeConfig ->
        routeConfig.onActivated(currentParam)
        routeConfig.composable()
    }
}