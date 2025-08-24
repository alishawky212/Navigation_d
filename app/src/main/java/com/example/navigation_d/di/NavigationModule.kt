package com.example.navigation_d.di

import com.example.navigation_d.features.auth.coordinator.AuthCoordinatorImpl
import com.example.navigation_d.features.main.coordinator.MainCoordinatorImpl
import com.example.navigation_d.features.orders.coordinator.OrdersCoordinatorImpl
import com.example.navigation_d.features.profile.navigation.ProfileNavigator
import com.example.navigation_d.features.profile.navigation.ProfileNavigatorImpl
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.RootCoordinatorImpl
import com.example.navigation_d.navigation.contract.RootCoordinator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun provideNavigator(): Navigator = Navigator("root")

    @Provides
    @Singleton
    fun provideRootCoordinator(
        navigator: Navigator
    ): RootCoordinator = RootCoordinatorImpl(navigator)

    @Provides
    @Singleton
    @Named("RootCoordinator")
    fun provideRootCoordinatorAsParent(rootCoordinator: RootCoordinator) = rootCoordinator

    @Provides
    @Singleton
    fun provideAuthCoordinator(
        navigator: Navigator,
        @Named("RootCoordinator") rootCoordinator: RootCoordinator
    ): Coordinator = AuthCoordinatorImpl( rootCoordinator)

    @Provides
    @Singleton
    fun provideMainCoordinator(
        ordersCoordinatorImpl: OrdersCoordinatorImpl,
        @Named("RootCoordinator") rootCoordinator: RootCoordinator
    ): Coordinator = MainCoordinatorImpl( rootCoordinator, ordersCoordinatorImpl)

    @Provides
    @Singleton
    fun provideOrdersCoordinator(
         rootCoordinator: MainCoordinatorImpl
    ): Coordinator = OrdersCoordinatorImpl( rootCoordinator)

    @Provides
    @Singleton
    fun provideProfileNavigator(
        navigator: Navigator,
        @Named("RootCoordinator") rootCoordinator: RootCoordinator
    ): ProfileNavigator = ProfileNavigatorImpl(navigator, rootCoordinator)
}
