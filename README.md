# Android Navigation with Coordinator Pattern

A modern Android application showcasing a clean navigation architecture using the **Coordinator Pattern** with Jetpack Compose and Hilt for dependency injection.

## 🏗 Architecture

The app follows a **Coordinator Pattern** with MVVM architecture, focusing on clean separation of concerns and maintainable navigation.

### Core Components
- **UI Layer**: Jetpack Compose for modern UI
- **Navigation**: Coordinator Pattern with type-safe routing
- **Dependency Injection**: Hilt for dependency management
- **State Management**: ViewModel with StateFlow

### Navigation Flow
```
UI Components → ViewModel → Coordinator → Navigation
```

## 🚀 Features

### Coordinator Pattern Implementation
- **RootCoordinator**: Manages top-level navigation
- **Feature Coordinators**: Dedicated coordinators for Auth, Main, and Orders
- **Clean Navigation**: Navigation logic separated from UI
- **Type Safety**: Sealed classes for navigation actions

### Back Navigation
- Screen-level back handling with `BackHandler`
- Each screen manages its own back navigation
- No global back action handling for simpler flow

## 🛠 Usage

### Creating a New Feature
1. Create a new package under `features/`
2. Define your coordinator interface:
   ```kotlin
   interface FeatureCoordinator : Coordinator {
       fun handleFeatureAction(action: FeatureAction)
   }
   ```
3. Implement the coordinator:
   ```kotlin
   class FeatureCoordinatorImpl @Inject constructor(
       private val navigator: Navigator,
       @Named("ParentCoordinator") override val parent: Lazy<Coordinator>?
   ) : FeatureCoordinator {
       override fun handleFeatureAction(action: FeatureAction) {
           when (action) {
               is FeatureAction.NavigateToScreen -> navigator.navigateTo(action.route)
           }
       }
   }
   ```

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
│   ├── main/                   # Main app flow
│   └── orders/                 # Orders feature
├── navigation/                  # Core navigation
│   ├── contract/               # Navigation interfaces
│   ├── AppNavigatorImpl.kt     # Navigation implementation
│   └── NavigationRoutes.kt     # Route definitions
└── MainActivity.kt             # App entry point
```

## 🏆 Best Practices

1. **Coordinators**
   - One coordinator per feature
   - Handle navigation only
   - Keep business logic in ViewModels

2. **Back Navigation**
   - Use `BackHandler` in composables
   - Keep back logic with UI components

3. **Dependency Injection**
   - Constructor injection
   - Use `@Named` for multiple instances

4. **Testing**
   - Test navigation actions
   - Mock dependencies

## 🚀 Getting Started

1. Clone the repository
2. Open in Android Studio
3. Run on an emulator or device

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Create a Pull Request

## 📄 License

MIT License - see [LICENSE](LICENSE)
```

### Screen Features
- **Authentication Flow**: Login screen with navigation to main app
- **Main Dashboard**: Central hub with access to orders and profile
- **Orders Management**: List and detail views for order management
- **Profile Management**: User profile with settings and preferences
- **Settings**: Theme toggle, notifications, and app preferences

### Technical Features
- **Clean Architecture**: MVVM + Coordinator pattern with action-based navigation
- **Dependency Injection**: Hilt with parent-child coordinator hierarchy
- **Preview Support**: All composables are previewable in Android Studio
- **Material 3 Theming**: Modern UI with Material 3 design system
- **State Management**: Reactive state management with ViewModels and StateFlow
- **Type Safety**: Strongly typed navigation with sealed class actions

## 🔄 Action-Based Navigation Flow

The app implements a clean action-based navigation architecture:

### Navigation Architecture
```
ViewModels → Actions → Coordinators → AppNavigator → NavController
```

### Usage Examples

**ViewModel to Coordinator Communication:**
```kotlin
// LoginViewModel
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authCoordinator: AuthCoordinator
) : ViewModel() {
    
    fun onLoginClick() {
        // Send action to coordinator
        authCoordinator.handleAuthAction(
            AuthCoordinatorAction.LoginSuccess("user123")
        )
    }
    
    fun onSettingsClick() {
        authCoordinator.handleAuthAction(AuthCoordinatorAction.ShowSettings)
    }
}

// OrdersViewModel
@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersCoordinator: OrdersCoordinator
) : ViewModel() {
    
    fun onOrderClick(orderId: String) {
        ordersCoordinator.handleOrdersAction(
            OrdersCoordinatorAction.ShowOrderDetails(orderId)
        )
    }
}
```

**Coordinator Implementation:**
```kotlin
@Singleton
class AuthCoordinatorImpl @Inject constructor(
    private val appNavigator: AppNavigator,
    @Named("RootCoordinator") override val parent: BaseNavigator?
) : AuthCoordinator {

    override fun handleAuthAction(action: AuthCoordinatorAction) {
        when (action) {
            is AuthCoordinatorAction.ShowLogin -> 
                appNavigator.navigateToLogin()
            is AuthCoordinatorAction.LoginSuccess -> 
                appNavigator.navigateToMainApp()
            is AuthCoordinatorAction.ShowSettings -> 
                appNavigator.navigateToSettings()
            is AuthCoordinatorAction.Logout -> 
                parent?.handleAction(action) // Delegate to parent
        }
    }
}
```

### Hierarchical Navigation Structure

```
🚀 App Start (RootCoordinator)
    ↓
🔐 Authentication Graph (AuthCoordinator)
    ├── LoginScreen → AuthCoordinatorAction.LoginSuccess
    └── SettingsScreen → AuthCoordinatorAction.ShowSettings
    ↓
🏠 Main Graph (MainCoordinator)  
    └── MainScreen → MainCoordinatorAction.ShowOrders/ShowProfile
    ↓
📦 Orders Graph (OrdersCoordinator)
    ├── OrdersListScreen → OrdersCoordinatorAction.ShowOrdersList
    └── OrderDetailsScreen → OrdersCoordinatorAction.ShowOrderDetails
    ↓
👤 Profile Graph (ProfileNavigator)
    ├── ProfileScreen → ProfileCoordinatorAction.ShowProfile
    ├── EditProfileScreen → ProfileCoordinatorAction.ShowEditProfile
    └── SavedItemsScreen → ProfileCoordinatorAction.ShowSavedItems
```

### Action-Based Coordinators

Each coordinator handles specific navigation actions:

- **RootCoordinator**: Manages app-level navigation and active child coordinators
- **AuthCoordinator**: Handles authentication flow with `AuthCoordinatorAction`
- **MainCoordinator**: Manages main app navigation with `MainCoordinatorAction`
- **OrdersCoordinator**: Controls order screens with `OrdersCoordinatorAction`
- **ProfileNavigator**: Manages profile features with `ProfileCoordinatorAction`

## 🧪 Testing

The app includes comprehensive preview support:

### Composable Previews
1. Open any composable file (e.g., `LoginScreen.kt`, `MainScreen.kt`)
2. Click the split or design view in Android Studio
3. The preview should appear in the right panel

### Navigation Testing
- All navigation flows are testable through the coordinator interfaces
- Mock navigators are provided for preview and testing purposes
- Deep link handling can be tested through the AppNavigator interface

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

### Version 3.0 - Pure Action-Based Navigation Architecture
- ✅ **Pure Action-Based Navigation** - Migrated from hybrid to 100% action-based navigation using sealed classes
- ✅ **Parent-Child Coordinator Hierarchy** - Implemented proper coordinator hierarchy with delegation
- ✅ **Cleaned Architecture** - Removed all unused direct navigation methods for cleaner codebase
- ✅ **Enhanced Type Safety** - All navigation uses strongly-typed sealed class actions
- ✅ **Improved DI Structure** - Hilt `@Provides` methods for coordinator parent injection
- ✅ **ViewModel Updates** - All ViewModels now use pure action-based navigation commands
- ✅ **Build Optimization** - Resolved all compilation errors and achieved successful build

### Version 2.0 - Coordinator Pattern Implementation
- ✅ **Migrated to Coordinator Pattern** - Implemented modular navigation architecture
- ✅ **Multi-Graph Navigation** - Separated Auth, Main, Orders, and Profile into distinct navigation graphs
- ✅ **Interface Abstractions** - Created clean navigation contracts for better testability
- ✅ **Dependency Injection Improvements** - Fixed DI binding issues and improved module structure

### Action-Based Architecture Benefits
- **🎯 Type Safety**: Sealed class actions provide compile-time safety and IDE support
- **🧹 Clean Code**: Single responsibility - coordinators only handle actions
- **🔄 Extensibility**: Easy to add new actions without changing coordinator interfaces
- **🧪 Testability**: Actions are easily mockable and testable
- **📦 Modularity**: Each feature has its own action types and coordinator
- **🏗️ Maintainability**: Clear separation of navigation concerns with action delegation
- **🚀 Scalability**: Parent-child hierarchy supports complex navigation flows

## 🤝 Contributing

1. Fork the repository
2. Create a new branch
3. Make your changes
4. Submit a pull request

### Development Guidelines
- **Action-Based Navigation**: Use sealed class actions for all navigation commands
- **Coordinator Hierarchy**: Implement parent-child relationships with proper delegation
- **Interface Abstractions**: Use proper navigation interfaces for testability
- **ViewModel Integration**: Inject coordinators into ViewModels and use action-based commands
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

2. **Create Coordinator Interface**:
```kotlin
interface NewFeatureCoordinator : BaseNavigator {
    fun handleNewFeatureAction(action: NewFeatureAction)
}
```

3. **Implement Coordinator**:
```kotlin
@Singleton
class NewFeatureCoordinatorImpl @Inject constructor(
    private val appNavigator: AppNavigator,
    @Named("RootCoordinator") override val parent: BaseNavigator?
) : NewFeatureCoordinator {
    
    override fun handleNewFeatureAction(action: NewFeatureAction) {
        when (action) {
            is NewFeatureAction.ShowFeature -> appNavigator.navigateToFeature()
            is NewFeatureAction.ShowDetails -> appNavigator.navigateToDetails(action.id)
        }
    }
}
```

4. **Update ViewModel**:
```kotlin
@HiltViewModel
class NewFeatureViewModel @Inject constructor(
    private val coordinator: NewFeatureCoordinator
) : ViewModel() {
    
    fun onFeatureClick() {
        coordinator.handleNewFeatureAction(NewFeatureAction.ShowFeature)
    }
}
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
