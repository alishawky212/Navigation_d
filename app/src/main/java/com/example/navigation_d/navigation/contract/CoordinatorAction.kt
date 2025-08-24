package com.example.navigation_d.navigation.contract

/**
 * Sealed interface representing actions that can be handled by coordinators
 * This enables flexible, data-carrying navigation commands
 */
sealed interface CoordinatorAction

/**
 * General actions that can be handled by any coordinator
 */
sealed class GeneralAction : CoordinatorAction {
    data class Done(val data: Any? = null) : GeneralAction()
    data class Cancel(val reason: String? = null) : GeneralAction()
    object Back : GeneralAction()
}

/**
 * App-level coordinator actions for root navigation
 */
sealed class AppCoordinatorAction : CoordinatorAction {
    object StartAuthFlow : AppCoordinatorAction()
    object StartMainFlow : AppCoordinatorAction()
    data class NavigateToRoute(val route: String) : AppCoordinatorAction()
}

/**
 * Auth-specific coordinator actions
 */
sealed class AuthCoordinatorAction : CoordinatorAction {
    object ShowLogin : AuthCoordinatorAction()
    object ShowSettings : AuthCoordinatorAction()
    data class LoginSuccess(val userId: String) : AuthCoordinatorAction()
    object Logout : AuthCoordinatorAction()
}

/**
 * Main app coordinator actions
 */
sealed class MainCoordinatorAction : CoordinatorAction {
    object ShowMainScreen : MainCoordinatorAction()
    object ShowOrders : MainCoordinatorAction()
    object ShowProfile : MainCoordinatorAction()
    object Logout : MainCoordinatorAction()
}

/**
 * Orders-specific coordinator actions
 */
sealed class OrdersCoordinatorAction : CoordinatorAction {
    object ShowOrdersList : OrdersCoordinatorAction()
    data class ShowOrderDetails(val orderId: String) : OrdersCoordinatorAction()
    object BackToMain : OrdersCoordinatorAction()
}

/**
 * Profile-specific coordinator actions
 */
sealed class ProfileCoordinatorAction : CoordinatorAction {
    object ShowProfile : ProfileCoordinatorAction()
    object ShowSettings : ProfileCoordinatorAction()
    object ShowEditProfile : ProfileCoordinatorAction()
    object ShowSavedItems : ProfileCoordinatorAction()
    object ShowOrderHistory : ProfileCoordinatorAction()
}
