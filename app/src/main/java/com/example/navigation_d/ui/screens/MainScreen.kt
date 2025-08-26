package com.example.navigation_d.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main screen composable
 * Entry point for the main app experience after login
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onOrdersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Main Screen") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Welcome to the App!")

            Spacer(modifier = Modifier.height(32.dp))

            // Orders button
            Button(
                onClick = onOrdersClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("View Orders")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile button
            Button(
                onClick = onProfileClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Profile")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Logout button
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Logout")
            }
        }
    }
}