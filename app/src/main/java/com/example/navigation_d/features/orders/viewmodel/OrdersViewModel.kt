package com.example.navigation_d.features.orders.viewmodel

import androidx.lifecycle.ViewModel
import com.example.navigation_d.features.orders.coordinator.OrdersCoordinator
import com.example.navigation_d.navigation.contract.OrdersCoordinatorAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OrdersViewModel @Inject constructor(
   @Named("OrdersCoordinator") private val ordersCoordinator: OrdersCoordinator
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrdersUiState())
    val uiState: StateFlow<OrdersUiState> = _uiState.asStateFlow()

    fun onOrderClick(orderId: String) {
        ordersCoordinator.handleOrdersAction(OrdersCoordinatorAction.ShowOrderDetails(orderId))
    }

    fun onBackClick() {
        // In the reference pattern, we use actions instead of direct navigation
        ordersCoordinator.handleOrdersAction(OrdersCoordinatorAction.BackToMain)
    }

    fun loadOrders() {
        // Simulate loading orders
        val mockOrders = listOf(
            Order("1", "Order #1001", "Pending", "$29.99"),
            Order("2", "Order #1002", "Delivered", "$45.50"),
            Order("3", "Order #1003", "Processing", "$12.99"),
            Order("4", "Order #1004", "Cancelled", "$67.25")
        )
        _uiState.value = _uiState.value.copy(orders = mockOrders, isLoading = false)
    }
}

data class OrdersUiState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = true
)

data class Order(
    val id: String,
    val title: String,
    val status: String,
    val amount: String
)
