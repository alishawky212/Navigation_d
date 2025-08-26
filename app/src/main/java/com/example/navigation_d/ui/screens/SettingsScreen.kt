package com.example.navigation_d.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Settings screen composable
 * Displays app settings and configuration options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    // Example settings state
    var darkModeEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Dark mode setting
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Dark Mode")
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications setting
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Enable Notifications")
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
        }
    }
}