package com.nexus.quiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nexus.quiz.data.local.PreferencesManager
import com.nexus.quiz.data.repository.NexusRepository
import com.nexus.quiz.engine.GeminiService

class ViewModelFactory(
    private val repository: NexusRepository,
    private val preferencesManager: PreferencesManager,
    private val geminiService: GeminiService,
    private val extraId: Long = -1L
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(IngestionViewModel::class.java) -> {
                IngestionViewModel(repository, geminiService) as T
            }
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> {
                ReviewViewModel(repository, geminiService) as T
            }
            modelClass.isAssignableFrom(DeckDetailViewModel::class.java) -> {
                DeckDetailViewModel(repository, extraId) as T
            }
            modelClass.isAssignableFrom(AnalyticsViewModel::class.java) -> {
                AnalyticsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(preferencesManager, repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
