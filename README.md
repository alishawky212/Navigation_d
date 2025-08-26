# Android Navigation with Coordinator Pattern

A modern Android application showcasing a clean navigation architecture using the **Coordinator Pattern** with Jetpack Compose and Hilt for dependency injection.

## 🏗 Architecture

The app follows a **Simplified Coordinator Pattern** with MVVM architecture, focusing on clean
separation of concerns and maintainable navigation.

### Core Components
- **UI Layer**: Jetpack Compose for modern UI
- **Navigation**: Streamlined Coordinator Pattern with type-safe routing
- **Dependency Injection**: Hilt for dependency management
- **State Management**: ViewModel with StateFlow

### Navigation Flow
```
UI Components → ViewModel → Coordinator (handle) → Navigation
```

## 🚀 Features

### Streamlined Coordinator Pattern Implementation
- **RootCoordinator**: Manages top-level navigation
- **Direct Coordinator Implementations**: Cleaner implementations for Auth, Main, and Orders
  features
- **Clean Navigation**: Navigation logic separated from UI
- **Type Safety**: Sealed classes for navigation actions
- **Nested Coordinators**: Support for hierarchical navigation flows

### Technical Features

- **Clean Architecture**: MVVM + Simplified Coordinator pattern with action-based navigation
- **Dependency Injection**: Hilt with parent-child coordinator hierarchy
- **Preview Support**: All composables are previewable in Android Studio
- **Material 3 Theming**: Modern UI with Material 3 design system
- **State Management**: Reactive state management with ViewModels and StateFlow
- **Type Safety**: Strongly typed navigation with sealed class actions

### Back Navigation
- Screen-level back handling with `BackHandler`
- Each screen manages its own back navigation
- No global back action handling for simpler flow

## 🛠 Usage

### Handling Back Navigation
```kotlin
@Composable
fun MyScreen() {
    BackHandler(onBack = {
        // Handle back press
        viewModel.onBackClicked()
    })
    
    // Screen content
}
```

## 📦 Project Structure
```
com.example.navigation_d/
├── di/                          # Dependency injection
├── features/                    # Feature modules
│   ├── auth/                   # Authentication
│   │   ├── coordinator/        # Auth coordinator
│   │   ├── screens/            # Auth UI screens
│   │   └── viewmodel/          # Auth ViewModels
│   ├── main/                   # Main app flow
│   │   ├── coordinator/        # Main coordinator (host)
│   │   ├── screens/            # Main UI screens
│   │   └── viewmodel/          # Main ViewModels
│   └── orders/                 # Orders feature
│       ├── coordinator/        # Orders coordinator (nested)
│       ├── screens/            # Orders UI screens
│       └── viewmodel/          # Orders ViewModels
├── navigation/                  # Core navigation
│   ├── contract/               # Navigation action interfaces
│   ├── Coordinator.kt          # Base coordinator interfaces
│   ├── Navigator.kt            # Navigation implementation
│   └── NavigationRoutes.kt     # Route definitions
└── MainActivity.kt             # App entry point
```

## 🏆 Best Practices

1. **Coordinators**
   - One coordinator per feature
   - Direct implementation of base interfaces
   - Handle navigation only
   - Keep business logic in ViewModels

2. **Back Navigation**
   - Use `BackHandler` in composables
   - Keep back logic with UI components

3. **Dependency Injection**
   - Constructor injection
   - Use `@Named` for coordinator instances

4. **Testing**
   - Test navigation actions
   - Mock dependencies

## 🚀 Getting Started

1. Clone the repository
2. Open in Android Studio
3. Run on an emulator or device

## 🔄 Action-Based Navigation Flow

The app implements a clean action-based navigation architecture:

### Navigation Architecture
```
ViewModels → Actions → Coordinators → Navigator → NavController
```

### Usage Examples

**ViewModel to Coordinator Communication:**
```kotlin
// LoginViewModel
@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("AuthCoordinator") private val authCoordinator: Coordinator
) : ViewModel() {
    
    fun onLoginClick() {
        // Send action to coordinator
        authCoordinator.handle(
            AuthCoordinatorAction.LoginSuccess("user123")
        )
    }
    
    fun onSettingsClick() {
        authCoordinator.handle(AuthCoordinatorAction.ShowSettings)
    }
}

// OrdersViewModel
@HiltViewModel
class OrdersViewModel @Inject constructor(
    @Named("OrdersCoordinator") private val ordersCoordinator: Coordinator
) : ViewModel() {
    
    fun onOrderClick(orderId: String) {
        ordersCoordinator.handle(
            OrdersCoordinatorAction.ShowOrderDetails(orderId)
        )
    }
}
```

**Coordinator Implementation:**
```kotlin
@Singleton
class AuthCoordinator @Inject constructor(
    @Named("RootCoordinator") override val parent: Lazy<Coordinator>?
) : Coordinator {

    override fun handle(action: CoordinatorAction): Boolean {
        return when (action) {
            is AuthCoordinatorAction -> {
                when (action) {
                    is AuthCoordinatorAction.ShowLogin -> {
                        navigate("login_screen")
                    }
                    is AuthCoordinatorAction.LoginSuccess -> {
                        navigate("main_graph")
                    }
                    is AuthCoordinatorAction.ShowSettings -> {
                        navigate("settings_screen")
                    }
                    is AuthCoordinatorAction.Logout -> {
                        navigate("login_screen")
                    }
                }
                true
            }
            else -> parent?.get()?.handle(action) ?: false
        }
    }
}
```

### Hierarchical Navigation Structure

```
🚀 App Start (RootCoordinator)
    ↓
🔐 Authentication (AuthCoordinator)
    ├── LoginScreen → AuthCoordinatorAction.LoginSuccess
    └── SettingsScreen → AuthCoordinatorAction.ShowSettings
    ↓
🏠 Main (MainCoordinator) ← HOST  
    └── MainScreen → MainCoordinatorAction.ShowOrders/ShowProfile
    ↓
📦 Orders (OrdersCoordinator) ← NESTED
    ├── OrdersListScreen → OrdersCoordinatorAction.ShowOrdersList
    └── OrderDetailsScreen → OrdersCoordinatorAction.ShowOrderDetails
```

### Action-Based Coordinators

Each coordinator handles specific navigation actions:

- **RootCoordinator**: Manages app-level navigation and active child coordinators
- **AuthCoordinator**: Handles authentication flow with `AuthCoordinatorAction`
- **MainCoordinator**: Manages main app navigation with `MainCoordinatorAction`
- **OrdersCoordinator**: Controls order screens with `OrdersCoordinatorAction`

## 🧪 Testing

The app includes comprehensive preview support:

### Composable Previews
1. Open any composable file (e.g., `LoginScreen.kt`, `MainScreen.kt`)
2. Click the split or design view in Android Studio
3. The preview should appear in the right panel

### Navigation Testing
- All navigation flows are testable through the coordinator interfaces
- Mock coordinators can be provided for isolated testing

## Navigation Sample

This project demonstrates a hierarchical coordinator-based navigation pattern for Android Compose
apps.

## Navigation Hierarchy

The navigation structure follows this hierarchy:

```
AppCoordinator (RootCoordinator)
├── AuthCoordinator
│   ├── LoginScreen
│   └── SettingsScreen
└── MainCoordinator (HostCoordinator)
    ├── MainScreen
    └── OrdersCoordinator
        ├── OrdersListScreen
        └── OrderDetailsScreen
```

### Key Components

1. **AppCoordinator**: The root coordinator that manages navigation between auth and main flows.
    - Implements `RootCoordinator` interface
    - Contains the main NavHost
    - Manages child coordinators (Auth and Main)

2. **AuthCoordinator**: Handles the authentication flow.
    - Manages login and settings screens
    - Reports login success to parent coordinator

3. **MainCoordinator**: Manages the main app flow after authentication.
    - Implements `HostCoordinator` to manage child coordinators
    - Hosts the OrdersCoordinator
    - Handles main screen and delegation to child flows

4. **OrdersCoordinator**: Manages the orders-related screens.
    - Handles orders list and order details navigation
    - Manages parameters for order details navigation

### Screens

- **LoginScreen**: User authentication entry point
- **SettingsScreen**: App settings configuration
- **MainScreen**: Home screen after login
- **OrdersListScreen**: Displays list of orders
- **OrderDetailsScreen**: Shows detailed information about a specific order

## Implementation Details

### Navigation Contract

The navigation is driven by typed actions defined in `CoordinatorAction` sealed interface
hierarchies:

- `AppCoordinatorAction`: Top-level navigation actions
- `AuthCoordinatorAction`: Auth-specific actions
- `MainCoordinatorAction`: Main flow actions
- `OrdersCoordinatorAction`: Orders-specific actions

### Dependency Injection

Coordinators are provided through Dagger Hilt, with proper hierarchical dependencies:

- `NavigationModule`: Binds coordinator implementations to their interfaces

### Coordinator Pattern Benefits

1. **Modular Navigation**: Each flow has its own coordinator
2. **Type-Safe Navigation**: Actions are strongly typed
3. **Hierarchical Delegation**: Actions bubble up if not handled
4. **Independent Testing**: Each coordinator can be tested in isolation
5. **Separation of Concerns**: Navigation logic is separate from UI

## Usage

To navigate, dispatch an appropriate action to the current coordinator:

```kotlin
// Example: Navigate to orders from main screen
handle(MainCoordinatorAction.ShowOrders)

// Example: Show order details with parameter
handle(OrdersCoordinatorAction.ShowOrderDetails(orderId))

// Example: Navigate back
handle(NavigationAction.Back)
```

The coordinator system will handle the action at the appropriate level and perform the navigation.

## 📦 Dependencies

### Core Dependencies
- **AndroidX Core KTX** - Android extensions
- **Jetpack Compose** - Modern UI toolkit
- **Navigation Component** - Type-safe navigation
- **Hilt** - Dependency injection framework
- **Material 3** - Material Design components

### Navigation Dependencies
- **Compose Navigation** - Navigation for Compose
- **Hilt Navigation Compose** - Hilt integration for Compose navigation

### Architecture Dependencies
- **ViewModel** - UI state management
- **Lifecycle** - Android lifecycle components

## 🔧 Recent Updates

### Version 4.0 - Simplified Coordinator Pattern

- ✅ **Streamlined Coordinator Pattern** - Removed unnecessary interface layers for cleaner
  architecture
- ✅ **Direct Base Interface Implementation** - Coordinators now directly implement `Coordinator` or
  `HostCoordinator`
- ✅ **Unified Action Handling** - All actions handled through the standard `handle()` method
- ✅ **Simplified DI Structure** - Cleaner dependency injection with fewer bindings
- ✅ **Improved Type Safety** - Maintained strong typing with less boilerplate

### Version 3.0 - Pure Action-Based Navigation Architecture
- ✅ **Pure Action-Based Navigation** - Migrated to 100% action-based navigation using sealed classes
- ✅ **Parent-Child Coordinator Hierarchy** - Implemented proper coordinator hierarchy with
  delegation
- ✅ **Enhanced Type Safety** - All navigation uses strongly-typed sealed class actions

### Simplified Architecture Benefits
- 🎯 **Type Safety**: Sealed class actions provide compile-time safety and IDE support
- 🧹 **Clean Code**: Fewer files and more direct implementations
- 🔄 **Extensibility**: Easy to add new actions without changing coordinator interfaces
- 🧪 **Testability**: Actions are easily mockable and testable
- 📦 **Modularity**: Each feature has its own coordinator and action types
- 🏗️ **Maintainability**: Simpler codebase with fewer layers
- 🚀 **Scalability**: Parent-child hierarchy supports complex navigation flows

## 🤝 Contributing

1. Fork the repository
2. Create a new branch
3. Make your changes
4. Submit a pull request

### Development Guidelines
- **Action-Based Navigation**: Use sealed class actions for all navigation commands
- **Direct Implementation**: Implement base interfaces directly for cleaner code
- **Coordinator Hierarchy**: Use parent-child relationships for nested flows
- **ViewModel Integration**: Inject coordinators into ViewModels and use base interface types
- **Preview Support**: Add preview support for all new composables
- **Material 3 Theming**: Maintain consistent Material 3 theming across screens

### Adding New Navigation Features

1. **Define Actions**: Create sealed class actions in `CoordinatorAction.kt`
```kotlin
sealed class NewFeatureAction : CoordinatorAction {
    object ShowFeature : NewFeatureAction()
    data class ShowDetails(val id: String) : NewFeatureAction()
}
```

2. **Implement Coordinator**:
```kotlin
@Singleton
class NewFeatureCoordinator @Inject constructor(
    @Named("ParentCoordinator") override val parent: Lazy<Coordinator>?
) : Coordinator {
    
    override fun handle(action: CoordinatorAction): Boolean {
        return when (action) {
            is NewFeatureAction -> {
                when (action) {
                    is NewFeatureAction.ShowFeature -> navigate("feature_screen")
                    is NewFeatureAction.ShowDetails -> navigate("details_screen", action.id)
                }
                true
            }
            else -> parent?.get()?.handle(action) ?: false
        }
    }
}
```

3. **Register in DI Module**:
```kotlin
@Binds
@Singleton
@Named("NewFeatureCoordinator")
abstract fun bindNewFeatureCoordinator(impl: NewFeatureCoordinator): Coordinator
```

4. **Use in ViewModel**:
```kotlin
@HiltViewModel
class NewFeatureViewModel @Inject constructor(
   @Named("NewFeatureCoordinator") private val coordinator: Coordinator
) : ViewModel() {
    
    fun onFeatureClick() {
       coordinator.handle(NewFeatureAction.ShowFeature)
    }
}
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
