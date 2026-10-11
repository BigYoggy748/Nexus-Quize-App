package com.nexus.quiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.local.entity.DeckEntity
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.data.repository.NexusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeckDetailViewModel(
    private val repository: NexusRepository,
    private val deckId: Long
) : ViewModel() {

    val deck: StateFlow<DeckEntity?> = repository.getDeckByIdFlow(deckId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val cards: StateFlow<List<CardEntity>> = repository.getCardsForDeck(deckId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCard(
        type: CardType,
        prompt: String,
        answer: String,
        explanation: String = "",
        latexFormula: String? = null,
        clozeOcclusion: String? = null
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repository.insertCard(
                CardEntity(
                    deckId = deckId,
                    type = type,
                    prompt = prompt.trim(),
                    answer = answer.trim(),
                    explanation = explanation.trim(),
                    latexFormula = latexFormula?.trim(),
                    clozeOcclusion = clozeOcclusion?.trim(),
                    dueDate = now
                )
            )
        }
    }

    fun deleteCard(cardId: Long) {
        viewModelScope.launch {
            repository.deleteCardById(cardId)
        }
    }
}
