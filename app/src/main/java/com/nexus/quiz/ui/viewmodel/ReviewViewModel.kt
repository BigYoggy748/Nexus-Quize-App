package com.nexus.quiz.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.data.model.FeynmanAuditResponseDto
import com.nexus.quiz.data.model.SrsRating
import com.nexus.quiz.data.repository.NexusRepository
import com.nexus.quiz.engine.GeminiService
import com.nexus.quiz.engine.SrsScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ReviewUiState {
    object Loading : ReviewUiState()
    object Empty : ReviewUiState()
    data class Active(
        val currentCard: CardEntity,
        val cardIndex: Int,
        val totalCards: Int,
        val isAnswerRevealed: Boolean,
        val feynmanUserText: String = "",
        val feynmanAuditLoading: Boolean = false,
        val feynmanAuditResult: FeynmanAuditResponseDto? = null
    ) : ReviewUiState()
    data class Complete(
        val reviewedCount: Int,
        val againCount: Int,
        val hardCount: Int,
        val goodCount: Int,
        val easyCount: Int
    ) : ReviewUiState()
}

class ReviewViewModel(
    private val repository: NexusRepository,
    private val geminiService: GeminiService
) : ViewModel() {

    private val gson = Gson()

    private val _uiState = MutableStateFlow<ReviewUiState>(ReviewUiState.Loading)
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    private var queue: MutableList<CardEntity> = mutableListOf()
    private var currentIndex = 0
    private var cardStartTimeMs = System.currentTimeMillis()

    // Session Stats
    private var totalReviewed = 0
    private var countAgain = 0
    private var countHard = 0
    private var countGood = 0
    private var countEasy = 0

    fun startSession(deckId: Long? = null) {
        viewModelScope.launch {
            _uiState.value = ReviewUiState.Loading
            totalReviewed = 0
            countAgain = 0
            countHard = 0
            countGood = 0
            countEasy = 0
            currentIndex = 0

            val dueCards = if (deckId != null && deckId > 0) {
                repository.getDueCardsForDeckBatch(deckId, limit = 100)
            } else {
                repository.getDueCardsBatch(limit = 100)
            }

            if (dueCards.isEmpty()) {
                _uiState.value = ReviewUiState.Empty
            } else {
                queue = dueCards.toMutableList()
                cardStartTimeMs = System.currentTimeMillis()
                emitActiveState(isRevealed = false)
            }
        }
    }

    private fun emitActiveState(
        isRevealed: Boolean,
        feynmanText: String = "",
        auditLoading: Boolean = false,
        auditResult: FeynmanAuditResponseDto? = null
    ) {
        if (currentIndex in queue.indices) {
            _uiState.value = ReviewUiState.Active(
                currentCard = queue[currentIndex],
                cardIndex = currentIndex,
                totalCards = queue.size,
                isAnswerRevealed = isRevealed,
                feynmanUserText = feynmanText,
                feynmanAuditLoading = auditLoading,
                feynmanAuditResult = auditResult
            )
        } else {
            _uiState.value = ReviewUiState.Complete(
                reviewedCount = totalReviewed,
                againCount = countAgain,
                hardCount = countHard,
                goodCount = countGood,
                easyCount = countEasy
            )
        }
    }

    fun revealAnswer() {
        val state = _uiState.value as? ReviewUiState.Active ?: return
        emitActiveState(
            isRevealed = true,
            feynmanText = state.feynmanUserText,
            auditLoading = state.feynmanAuditLoading,
            auditResult = state.feynmanAuditResult
        )
    }

    fun updateFeynmanText(text: String) {
        val state = _uiState.value as? ReviewUiState.Active ?: return
        _uiState.value = state.copy(feynmanUserText = text)
    }

    fun auditFeynmanExplanation() {
        val state = _uiState.value as? ReviewUiState.Active ?: return
        if (state.feynmanUserText.isBlank()) return

        val card = state.currentCard
        val keyTerms: List<String> = try {
            if (!card.keyTermsJson.isNullOrBlank()) {
                val listType = object : TypeToken<List<String>>() {}.type
                gson.fromJson(card.keyTermsJson, listType)
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }

        emitActiveState(
            isRevealed = true,
            feynmanText = state.feynmanUserText,
            auditLoading = true,
            auditResult = null
        )

        viewModelScope.launch {
            val result = geminiService.auditFeynmanExplanation(
                prompt = card.prompt,
                expectedAnswer = card.answer,
                keyTerms = keyTerms,
                userExplanation = state.feynmanUserText
            )

            result.onSuccess { auditDto ->
                emitActiveState(
                    isRevealed = true,
                    feynmanText = state.feynmanUserText,
                    auditLoading = false,
                    auditResult = auditDto
                )
            }.onFailure {
                emitActiveState(
                    isRevealed = true,
                    feynmanText = state.feynmanUserText,
                    auditLoading = false,
                    auditResult = null
                )
            }
        }
    }

    fun rateCard(rating: SrsRating) {
        val state = _uiState.value as? ReviewUiState.Active ?: return
        val currentCard = state.currentCard
        val timeSpent = System.currentTimeMillis() - cardStartTimeMs

        when (rating) {
            SrsRating.AGAIN -> countAgain++
            SrsRating.HARD -> countHard++
            SrsRating.GOOD -> countGood++
            SrsRating.EASY -> countEasy++
        }
        totalReviewed++

        viewModelScope.launch {
            val srsResult = SrsScheduler.scheduleCard(
                card = currentCard,
                rating = rating,
                timeSpentMs = timeSpent
            )

            repository.updateCard(srsResult.updatedCard)
            repository.logReview(srsResult.reviewLog)

            // If card failed (Again), requeue at the end of the current session
            if (rating == SrsRating.AGAIN) {
                queue.add(srsResult.updatedCard)
            }

            currentIndex++
            cardStartTimeMs = System.currentTimeMillis()
            emitActiveState(isRevealed = false)
        }
    }
}
