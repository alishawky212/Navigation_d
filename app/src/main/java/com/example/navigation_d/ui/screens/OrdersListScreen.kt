package com.example.navigation_d.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Data class representing an order item
 */
data class OrderItem(
    val id: String,
    val name: String,
    val price: String,
    val status: String
)

/**
 * Orders list screen composable
 * Shows a list of orders that can be clicked to view details
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreen(
    onOrderClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    // Sample order data
    val orders = listOf(
        OrderItem("1", "Order #1001", "$25.99", "Shipped"),
        OrderItem("2", "Order #1002", "$64.50", "Processing"),
        OrderItem("3", "Order #1003", "$12.75", "Delivered"),
        OrderItem("4", "Order #1004", "$99.99", "Pending"),
        OrderItem("5", "Order #1005", "$32.50", "Cancelled")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orders") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                items(orders) { order ->
                    OrderItemCard(
                        order = order,
                        onClick = { onOrderClick(order.id) }
                    )
                }
            }
        }
    }
}

/**
 * Order item card composable
 * Displays details for a single order in the list
 */
@Composable
private fun OrderItemCard(
    order: OrderItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = order.name,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Status: ${order.status}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = order.price,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}