package com.example.navigation_d.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.navigation_d.features.profile.navigation.ProfileNavigator
import com.example.navigation_d.navigation.contract.ProfileCoordinatorAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileNavigator: ProfileNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onSettingsClick() {
        profileNavigator.handleProfileAction(ProfileCoordinatorAction.ShowSettings)
    }

    fun onEditProfileClick() {
        profileNavigator.handleProfileAction(ProfileCoordinatorAction.ShowEditProfile)
    }

    fun onBackClick() {
        // In the reference pattern, we use actions instead of direct navigation
        profileNavigator.handleProfileAction(ProfileCoordinatorAction.ShowProfile)
    }
}

data class ProfileUiState(
    val userName: String = "John Doe",
    val userEmail: String = "john.doe@example.com",
    val isLoading: Boolean = false
)
