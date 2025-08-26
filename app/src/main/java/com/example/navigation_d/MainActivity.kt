package com.example.navigation_d

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.contract.AppCoordinatorAction
import com.example.navigation_d.navigation.contract.RootCoordinator
import com.example.navigation_d.navigation.impl.AppCoordinator
import com.example.navigation_d.ui.theme.Navigation_dTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main activity that hosts the app's navigation
 * Uses the coordinator pattern for navigation management
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator
    
    @Inject
    lateinit var appCoordinator: AppCoordinator
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            NavigationApp(rootCoordinator = appCoordinator)
        }
    }
}

/**
 * Main composable that sets up the app's navigation structure
 * Delegates to the RootCoordinator for managing navigation flows
 */
@Composable
fun NavigationApp(rootCoordinator: RootCoordinator) {
    Navigation_dTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Use Column to ensure sequential composition
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Start with the auth flow - this must happen first
                rootCoordinator.start(AppCoordinatorAction.StartAuthFlow)

                // Then render the navigation host - this must happen after start
                rootCoordinator.renderNavHost()
            }
        }
    }
}
