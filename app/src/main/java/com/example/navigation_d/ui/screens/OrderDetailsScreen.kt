package com.example.navigation_d.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
 * Data class representing an order item detail
 */
data class OrderDetail(
    val name: String,
    val quantity: Int,
    val price: String
)

/**
 * Order details screen composable
 * Shows detailed information about a specific order
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(
    orderId: String,
    onBackClick: () -> Unit
) {
    // In a real app, we would fetch this data based on the orderId
    val orderDetails = when (orderId) {
        "1" -> OrderDetails(
            id = "1",
            orderNumber = "#1001",
            date = "May 15, 2023",
            status = "Shipped",
            total = "$25.99",
            items = listOf(
                OrderDetail("Bluetooth Headphones", 1, "$19.99"),
                OrderDetail("HDMI Cable", 1, "$5.99")
            )
        )

        "2" -> OrderDetails(
            id = "2",
            orderNumber = "#1002",
            date = "May 18, 2023",
            status = "Processing",
            total = "$64.50",
            items = listOf(
                OrderDetail("Wireless Mouse", 1, "$29.99"),
                OrderDetail("Keyboard", 1, "$34.50")
            )
        )

        else -> OrderDetails(
            id = orderId,
            orderNumber = "#${1000 + orderId.toInt()}",
            date = "May 20, 2023",
            status = "Pending",
            total = "$45.00",
            items = listOf(
                OrderDetail("Generic Item", 1, "$45.00")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order ${orderDetails.orderNumber}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Order summary card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    DetailRow("Order Number", orderDetails.orderNumber)
                    DetailRow("Date", orderDetails.date)
                    DetailRow("Status", orderDetails.status)
                    DetailRow("Total", orderDetails.total)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Order items card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Items",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    orderDetails.items.forEachIndexed { index, item ->
                        Column {
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Qty: ${item.quantity}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = item.price,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            // Add divider between items but not after the last one
                            if (index < orderDetails.items.size - 1) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider()
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Data class for full order details
 */
data class OrderDetails(
    val id: String,
    val orderNumber: String,
    val date: String,
    val status: String,
    val total: String,
    val items: List<OrderDetail>
)

/**
 * Helper composable for displaying a label-value row
 */
@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}