package com.example.navigation_d.di

import com.example.navigation_d.features.auth.coordinator.AuthCoordinator
import com.example.navigation_d.features.main.coordinator.MainCoordinator
import com.example.navigation_d.features.orders.coordinator.OrdersCoordinator
import com.example.navigation_d.navigation.Coordinator
import com.example.navigation_d.navigation.HostCoordinator
import com.example.navigation_d.navigation.RootCoordinatorImpl
import com.example.navigation_d.navigation.contract.RootCoordinator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    @Named("AuthCoordinator")
    abstract fun bindAuthCoordinator(impl: AuthCoordinator): Coordinator


    @Binds
    @Singleton
    @Named("MainCoordinator")
    abstract fun bindMainCoordinator(impl: MainCoordinator): HostCoordinator

    @Binds
    @Singleton
    @Named("MainCoordinator")
    abstract fun bindMainCoordinatorBase(impl: MainCoordinator): Coordinator


    @Binds
    @Singleton
    @Named("OrdersCoordinator")
    abstract fun bindOrdersCoordinator(impl: OrdersCoordinator): Coordinator

    @Binds
    @Singleton
    @Named("RootCoordinator")
    abstract fun bindRootCoordinator(impl: RootCoordinatorImpl): RootCoordinator


    @Binds
    @Singleton
    @Named("RootCoordinator")
    abstract fun bindRootCoordinatorBase(impl: RootCoordinatorImpl): Coordinator
}
