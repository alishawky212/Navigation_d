package com.example.navigation_d.di

import com.example.navigation_d.features.auth.coordinator.AuthCoordinator
import com.example.navigation_d.features.auth.coordinator.AuthCoordinatorImpl
import com.example.navigation_d.features.main.coordinator.MainCoordinator
import com.example.navigation_d.features.main.coordinator.MainCoordinatorImpl
import com.example.navigation_d.features.orders.coordinator.OrdersCoordinator
import com.example.navigation_d.features.orders.coordinator.OrdersCoordinatorImpl
import com.example.navigation_d.navigation.Coordinator
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
    abstract fun bindAuthCoordinator(impl: AuthCoordinatorImpl): AuthCoordinator

    @Binds
    @Singleton
    @Named("AutheCoordinator")
    abstract fun bindAutheCoordinator(impl: AuthCoordinatorImpl): Coordinator


    @Binds
    @Singleton
    @Named("MainCoordinator")
    abstract fun bindMainCoordinator(impl: MainCoordinatorImpl): MainCoordinator

    @Binds
    @Singleton
    @Named("MaineCoordinator")
    abstract fun bindMaineCoordinator(impl: MainCoordinatorImpl): Coordinator


    @Binds
    @Singleton
    @Named("OrdersCoordinator")
    abstract fun bindOrdersCoordinator(impl: OrdersCoordinatorImpl): OrdersCoordinator
    @Binds
    @Singleton
    @Named("OrderssCoordinator")
    abstract fun bindOrderssCoordinator(impl: OrdersCoordinatorImpl): Coordinator

    @Binds
    @Singleton
    @Named("RooteCoordinator")
    abstract fun bindRooteCoordinator(impl: RootCoordinatorImpl): RootCoordinator


    @Binds
    @Singleton
    @Named("RootCoordinator")
    abstract fun bindRootCoordinator(impl: RootCoordinatorImpl): Coordinator
}

