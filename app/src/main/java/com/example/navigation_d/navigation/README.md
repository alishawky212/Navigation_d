# Coordinator-Based Navigation Pattern

## Overview

This navigation system implements a hierarchical coordinator pattern for Android Compose apps. It
provides a clean, maintainable, and testable architecture for managing navigation flows.

## Key Components

### 1. Coordinator

The `Coordinator` interface is the core of the navigation system. Each coordinator:

- Manages a specific navigation flow or sub-flow
- Handles navigation actions
- Delegates to parent coordinators when appropriate
- Sets up its navigation graph

```kotlin
interface Coordinator {
    val parent: Lazy<Coordinator>?
    
    fun setupNavigation(builder: NavGraphBuilder)
    fun handle(action: CoordinatorAction): Boolean
    fun navigate(route: String)
    fun navigateBack(): Boolean
}
```

### 2. HostCoordinator

Extends `Coordinator` to manage child coordinators:

```kotlin
interface HostCoordinator : Coordinator {
    var activeCoordinator: Coordinator?
    fun activateCoordinator(coordinator: Coordinator)
    fun deactivateCoordinator()
}
```

### 3. RootCoordinator

The top-level coordinator that manages the entire navigation system:

```kotlin
interface RootCoordinator : HostCoordinator {
    val navigator: Navigator
    
    @Composable
    fun start(action: AppCoordinatorAction)
    
    @Composable
    fun renderNavHost()
    
    fun executeNavigation(route: String, params: Any?)
    fun executeBackNavigation(): Boolean
    fun canNavigateBack(): Boolean
}
```

### 4. Navigator

Handles the actual navigation operations using NavController:

```kotlin
class Navigator @Inject constructor() {
    fun setNavController(controller: NavHostController)
    fun navigateTo(route: String, params: Any? = null)
    fun popBackStack(): Boolean
    fun canGoBack(): Boolean
}
```

## Flow Implementation

The navigation system is organized into flows:

1. **Auth Flow** (AuthCoordinator)
    - Login Screen
    - Settings Screen

2. **Main Flow** (MainCoordinator)
    - Main Screen
    - Orders Flow (via OrdersCoordinator)

3. **Orders Flow** (OrdersCoordinator)
    - Orders List Screen
    - Order Details Screen

## Navigation Actions

Navigation is triggered through typed actions:

```kotlin
// Example actions
sealed interface AppCoordinatorAction : CoordinatorAction {
    object StartAuthFlow : AppCoordinatorAction
    object StartMainFlow : AppCoordinatorAction
}

sealed interface AuthCoordinatorAction : CoordinatorAction {
    object ShowLogin : AuthCoordinatorAction
    object ShowSettings : AuthCoordinatorAction
    data class LoginSuccess(val userId: String) : AuthCoordinatorAction
}
```

## Usage

### 1. Triggering Navigation

To navigate to a new screen, use the `handle()` method with the appropriate action:

```kotlin
// From a Composable screen
LoginScreen(
    onLoginClick = { userId ->
        handle(AuthCoordinatorAction.LoginSuccess(userId))
    },
    onSettingsClick = {
        handle(AuthCoordinatorAction.ShowSettings)
    }
)
```

### 2. Back Navigation

For back navigation, use the `navigateBack()` method:

```kotlin
SettingsScreen(
    onBackClick = {
        navigateBack()
    }
)
```

## Testing

The coordinator pattern makes testing easier by separating navigation logic from UI:

### Unit Testing

Test individual coordinators by mocking their dependencies:

```kotlin
@Test
fun `should activate main flow when handling LoginSuccess action`() {
    // When
    appCoordinator.handle(AuthCoordinatorAction.LoginSuccess("user123"))
    
    // Then
    verify(mockNavigator).navigateTo("main_graph")
}
```

### UI Testing

Test full navigation flows using Compose UI testing:

```kotlin
@Test
fun testNavigationToMainScreenAfterLogin() {
    // Enter username
    composeTestRule.onNodeWithText("Username").performClick()
    
    // Navigate to main screen by clicking login
    composeTestRule.onNodeWithText("Login").performClick()
    
    // Verify we're on the main screen
    composeTestRule.onNodeWithText("Welcome to the App!").assertIsDisplayed()
}
```

## Benefits

1. **Clean Separation of Concerns**: Navigation logic is separated from UI
2. **Testability**: Easy to test navigation flows in isolation
3. **Maintainability**: Clear organization of navigation logic by flow
4. **Flexibility**: Easy to add, remove, or modify flows
5. **Type Safety**: Navigation actions are type-safe