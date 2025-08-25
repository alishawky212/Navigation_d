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
import androidx.compose.ui.Modifier
import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.contract.RootCoordinator
import com.example.navigation_d.ui.theme.Navigation_dTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var navigator: Navigator
    
    @Inject
    @Named("RootCoordinator")
    lateinit var rootCoordinator: RootCoordinator
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            NavigationApp(
                rootCoordinator = rootCoordinator,
            )
        }
    }
}

@Composable
fun NavigationApp(
    rootCoordinator: RootCoordinator,
) {
    Navigation_dTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Use centralized NavHost rendering from RootCoordinator
            rootCoordinator.renderNavHost()
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun NavigationAppPreview() {
//    NavigationApp(
//        navigator = mockNavigator,
//        rootCoordinator = mockRootCoordinator,
//        onFinish = {}
//    )
//}
