package com.example.navigation_d.di

import com.example.navigation_d.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module that provides navigation-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    /**
     * Provide the Navigator singleton
     */
    @Provides
    @Singleton
    fun provideNavigator(): Navigator = Navigator()
}