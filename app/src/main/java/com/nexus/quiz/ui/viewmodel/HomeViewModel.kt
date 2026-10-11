package com.nexus.quiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.quiz.data.local.dao.DeckWithStats
import com.nexus.quiz.data.local.dao.RetentionOverview
import com.nexus.quiz.data.local.entity.DeckEntity
import com.nexus.quiz.data.model.DeckCategory
import com.nexus.quiz.data.repository.NexusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: NexusRepository) : ViewModel() {

    val decks: StateFlow<List<DeckWithStats>> = repository.getDecksWithStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val dueCardsCount: StateFlow<Int> = repository.countDueCards()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val retentionOverview: StateFlow<RetentionOverview> = repository.getRetentionOverview()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RetentionOverview(0, 0, 0, 0, 0)
        )

    val reviewsTodayCount: StateFlow<Int> = repository.countReviewsToday()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun createDeck(
        name: String,
        description: String,
        category: DeckCategory,
        hexColor: String
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.insertDeck(
                DeckEntity(
                    name = name.trim(),
                    description = description.trim(),
                    category = category,
                    hexColor = hexColor,
                    createdAt = now,
                    updatedAt = now
                )
            )
        }
    }

    fun deleteDeck(deckId: Long) {
        viewModelScope.launch {
            repository.deleteDeckById(deckId)
        }
    }
}
