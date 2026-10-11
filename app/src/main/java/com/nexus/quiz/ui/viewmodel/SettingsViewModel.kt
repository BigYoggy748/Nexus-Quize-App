package com.nexus.quiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.quiz.data.local.PreferencesManager
import com.nexus.quiz.data.repository.NexusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val repository: NexusRepository
) : ViewModel() {

    val apiKey: StateFlow<String> = preferencesManager.apiKeyFlow
    val selectedModel: StateFlow<String> = preferencesManager.modelFlow
    val username: StateFlow<String> = preferencesManager.usernameFlow

    private val _saveStatus = MutableStateFlow<String?>(null)
    val saveStatus: StateFlow<String?> = _saveStatus.asStateFlow()

    fun updateUsername(name: String) {
        preferencesManager.setUsername(name)
        _saveStatus.value = "Username updated."
    }

    fun updateApiKey(key: String) {
        preferencesManager.setApiKey(key)
        _saveStatus.value = "API key saved."
    }

    fun updateModel(model: String) {
        preferencesManager.setSelectedModel(model)
        _saveStatus.value = "Model updated to $model."
    }

    fun getAvatarInitials(): String {
        return preferencesManager.getAvatarInitials()
    }

    fun clearSaveStatus() {
        _saveStatus.value = null
    }
}
