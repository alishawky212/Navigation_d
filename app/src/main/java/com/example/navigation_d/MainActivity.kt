package com.example.navigation_d

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.navigation_d.navigation.NavHost
import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.contract.AppCoordinatorAction
import com.example.navigation_d.navigation.contract.RootCoordinator
import com.example.navigation_d.ui.theme.Navigation_dTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator
    
    @Inject
    lateinit var rootCoordinator: RootCoordinator
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            NavigationApp(
                navigator = navigator,
                rootCoordinator = rootCoordinator,
                onFinish = { finish() }
            )
        }
        
        // Handle deep links when the app is opened from a deep link
        intent?.data?.let { uri ->
            if (intent?.action == Intent.ACTION_VIEW) {
                // In the reference pattern, deep links can be handled through actions
                navigator.navigateTo("auth_graph")
            }
        }
    }
    
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Handle deep links when the app is already open
        intent?.data?.let { uri ->
            if (intent.action == Intent.ACTION_VIEW) {
                // In the reference pattern, deep links can be handled through actions
                navigator.navigateTo("auth_graph")
            }
        }
    }
}

@Composable
fun NavigationApp(
    navigator: Navigator,
    rootCoordinator: RootCoordinator,
    onFinish: () -> Unit
) {
    // Start the root coordinator with initial auth flow
    LaunchedEffect(Unit) {
        rootCoordinator.start(AppCoordinatorAction.StartAuthFlow)
    }
    
    Navigation_dTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Use custom NavHost with the coordinator pattern
            NavHost(navigator = navigator) {
                rootCoordinator.activeCoordinator?.setupNavigation(this)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationAppPreview() {
    val mockNavigator = Navigator("login_screen")
    val mockRootCoordinator = object : RootCoordinator {
        override val parent: com.example.navigation_d.navigation.Coordinator? = null
        override val activeCoordinator: com.example.navigation_d.navigation.Coordinator? = null
        override val navigator: Navigator = mockNavigator
        
        @Composable
        override fun handle(action: com.example.navigation_d.navigation.contract.CoordinatorAction) {}
        
        @Composable
        override fun start(action: com.example.navigation_d.navigation.contract.AppCoordinatorAction) {}
        
        override fun navigate(route: String) {}
        override fun navigate(route: String, params: Any?) {}
    }
    
    NavigationApp(
        navigator = mockNavigator,
        rootCoordinator = mockRootCoordinator,
        onFinish = {}
    )
}
