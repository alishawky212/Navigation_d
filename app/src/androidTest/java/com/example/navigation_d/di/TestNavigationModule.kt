package com.example.navigation_d.di

import com.example.navigation_d.navigation.Navigator
import com.example.navigation_d.navigation.impl.AppCoordinator
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NavigationModule::class]
)
object TestNavigationModule {

    @Provides
    @Singleton
    fun provideNavigator(): Navigator = Navigator()

    @Provides
    @Singleton
    fun provideAppCoordinator(navigator: Navigator): AppCoordinator = AppCoordinator(navigator)
}