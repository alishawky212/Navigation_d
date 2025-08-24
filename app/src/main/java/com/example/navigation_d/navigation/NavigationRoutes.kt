package com.example.navigation_d.navigation

object NavigationRoutes {
    // Graph routes - Aligned with Coordinator Hierarchy
    const val AUTH_GRAPH = "auth_graph"
    const val MAIN_GRAPH = "main_graph"
    const val ORDERS_GRAPH = "orders_graph"
    
    // Auth Coordinator screens
    object Auth {
        const val LOGIN = "login_screen"
        const val SETTINGS = "settings_screen"
    }
    
    // Main Coordinator screens
    object Main {
        const val MAIN_SCREEN = "main_screen"
    }
    
    // Orders Coordinator screens
    object Orders {
        const val ORDERS_LIST = "orders_list_screen"
        const val ORDER_DETAILS = "order_details_screen"
        
        // Helper function to create order details route with ID
        fun orderDetailsWithId(orderId: String) = "$ORDER_DETAILS/$orderId"
    }
    
    // Profile routes
    object Profile {
        const val PROFILE_SCREEN = "profile_screen"
    }
}
