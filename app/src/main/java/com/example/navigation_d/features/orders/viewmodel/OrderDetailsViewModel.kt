package com.example.navigation_d.features.orders.viewmodel

import androidx.lifecycle.SavedStateHandle
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
class OrderDetailsViewModel @Inject constructor(
    @Named("OrdersCoordinator") private val ordersCoordinator: OrdersCoordinator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val orderId: String = savedStateHandle.get<String>("orderId") ?: ""

    private val _uiState = MutableStateFlow(OrderDetailsUiState())
    val uiState: StateFlow<OrderDetailsUiState> = _uiState.asStateFlow()

    init {
        loadOrderDetails()
    }

    fun onBackClick() {
        // In the reference pattern, we use actions instead of direct navigation
        ordersCoordinator.handleOrdersAction(OrdersCoordinatorAction.BackToMain)
    }

    private fun loadOrderDetails() {
        // Simulate loading order details
        val orderDetails = OrderDetails(
            id = orderId,
            title = "Order #100$orderId",
            status = "Delivered",
            amount = "$45.50",
            date = "2024-01-15",
            items = listOf(
                "Product A - $20.00",
                "Product B - $15.50",
                "Shipping - $10.00"
            ),
            address = "123 Main St, City, State 12345"
        )
        _uiState.value = _uiState.value.copy(orderDetails = orderDetails, isLoading = false)
    }
}

data class OrderDetailsUiState(
    val orderDetails: OrderDetails? = null,
    val isLoading: Boolean = true
)

data class OrderDetails(
    val id: String,
    val title: String,
    val status: String,
    val amount: String,
    val date: String,
    val items: List<String>,
    val address: String
)
