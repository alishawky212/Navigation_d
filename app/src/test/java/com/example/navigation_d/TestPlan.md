# Navigation Testing Plan

## Unit Tests

### Navigator Tests

1. **Basic Navigator Functionality**
    - Test initial state of the Navigator
    - Test navigateTo() functionality
    - Test popBackStack() functionality
    - Test canGoBack() functionality

2. **AppCoordinator Tests**
    - Test handling of StartAuthFlow action
    - Test handling of StartMainFlow action
    - Test handling of LoginSuccess action
    - Test handling of Logout action
    - Test proper child coordinator activation

3. **AuthCoordinator Tests**
    - Test handling of ShowLogin action
    - Test handling of ShowSettings action
    - Test handling of LoginSuccess action
    - Test proper back navigation

4. **MainCoordinator Tests**
    - Test handling of ShowMainScreen action
    - Test handling of ShowOrders action
    - Test handling of child coordinator activation
    - Test proper back navigation

5. **OrdersCoordinator Tests**
    - Test handling of ShowOrdersList action
    - Test handling of ShowOrderDetails action with parameters
    - Test proper back navigation

## UI Tests

1. **Authentication Flow**
    - Test initial screen is Login
    - Test navigation to Settings
    - Test back navigation from Settings
    - Test successful login navigates to Main screen

2. **Main Flow**
    - Test navigation to Orders screen
    - Test back navigation from Orders to Main
    - Test logout returns to Login screen

3. **Orders Flow**
    - Test navigation to Order Details from Orders list
    - Test back navigation from Order Details to Orders list
    - Test proper parameter passing (orderId)

4. **Back Navigation**
    - Test system back button works properly at all levels
    - Test back navigation correctly maintains state
    - Test back navigation from nested screens (Order Details → Orders List → Main)

5. **Navigation Between Flows**
    - Test proper flow when navigating between Auth and Main
    - Test state preservation when navigating between flows

## Test Implementation Strategy

1. **For Unit Tests:**
    - Use JUnit for testing
    - Use MockK or Mockito for mocking dependencies
    - Focus on testing coordinators in isolation
    - Test navigation logic without UI components

2. **For UI Tests:**
    - Use Compose UI testing framework
    - Test full navigation flows from end to end
    - Verify correct screens are displayed
    - Test user interactions trigger correct navigation
    - Test back navigation works as expected

## Test Coverage Goals

- 90%+ test coverage for navigation logic
- All navigation paths tested
- All edge cases covered (back navigation, parameter passing, etc.)
- Full testing of hierarchical coordinator relationships