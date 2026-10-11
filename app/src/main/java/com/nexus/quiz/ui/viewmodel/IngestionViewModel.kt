package com.nexus.quiz.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.local.entity.DeckEntity
import com.nexus.quiz.data.local.entity.TopicMasteryEntity
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.data.model.DeckCategory
import com.nexus.quiz.data.model.GradedCardDto
import com.nexus.quiz.data.model.TopicDeepMasteryResponseDto
import com.nexus.quiz.data.repository.NexusRepository
import com.nexus.quiz.engine.GeminiService
import com.nexus.quiz.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class IngestionUiState {
    object Idle : IngestionUiState()
    object Loading : IngestionUiState()
    data class DeepMasterySuccess(val breakdown: TopicDeepMasteryResponseDto) : IngestionUiState()
    data class Error(val message: String) : IngestionUiState()
    object Saved : IngestionUiState()
}

class IngestionViewModel(
    private val repository: NexusRepository,
    private val geminiService: GeminiService
) : ViewModel() {

    private val gson = Gson()

    val decks: StateFlow<List<DeckEntity>> = repository.getAllDecks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedTopics: StateFlow<List<TopicMasteryEntity>> = repository.getAllSavedTopics()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow<IngestionUiState>(IngestionUiState.Idle)
    val uiState: StateFlow<IngestionUiState> = _uiState.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    private var selectedImageBase64: String? = null
    private var selectedImageMimeType: String? = null

    private val _deepMastery = MutableStateFlow<TopicDeepMasteryResponseDto?>(null)
    val deepMastery: StateFlow<TopicDeepMasteryResponseDto?> = _deepMastery.asStateFlow()

    private val _gradedCards = MutableStateFlow<List<GradedCardDto>>(emptyList())
    val gradedCards: StateFlow<List<GradedCardDto>> = _gradedCards.asStateFlow()

    private val _difficultyFilter = MutableStateFlow("ALL") // "ALL" | "FUNDAMENTAL" | "MEDIUM" | "HARD"
    val difficultyFilter: StateFlow<String> = _difficultyFilter.asStateFlow()

    fun setSelectedImage(context: Context, uri: Uri?) {
        _selectedImageUri.value = uri
        if (uri != null) {
            val result = ImageUtils.uriToBase64(context, uri)
            selectedImageBase64 = result?.first
            selectedImageMimeType = result?.second
        } else {
            selectedImageBase64 = null
            selectedImageMimeType = null
        }
    }

    fun clearSelectedImage() {
        _selectedImageUri.value = null
        selectedImageBase64 = null
        selectedImageMimeType = null
    }

    fun setDifficultyFilter(filter: String) {
        _difficultyFilter.value = filter
    }

    fun removeGradedCard(index: Int) {
        val current = _gradedCards.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _gradedCards.value = current
        }
    }

    fun analyzeAndBreakdown(rawNotes: String, topicHint: String) {
        if (rawNotes.isBlank() && selectedImageBase64.isNullOrBlank()) {
            _uiState.value = IngestionUiState.Error("Please provide notes, topic name, or upload a screenshot/slide.")
            return
        }

        _uiState.value = IngestionUiState.Loading
        viewModelScope.launch {
            val result = geminiService.analyzeAndBreakdownTopic(
                rawNotes = rawNotes,
                imageBase64 = selectedImageBase64,
                imageMimeType = selectedImageMimeType ?: "image/jpeg",
                topicHint = topicHint
            )

            result.onSuccess { response ->
                _deepMastery.value = response
                _gradedCards.value = response.gradedCards
                _uiState.value = IngestionUiState.DeepMasterySuccess(response)

                // Persist topic breakdown to Room so user can revisit explanations anytime
                val json = gson.toJson(response)
                val entity = TopicMasteryEntity(
                    topicTitle = response.topicTitle.ifBlank { topicHint.ifBlank { "Deconstructed Topic" } },
                    topicHint = topicHint,
                    rawNotes = rawNotes,
                    jsonContent = json,
                    cardCount = response.gradedCards.size
                )
                repository.saveTopicMastery(entity)
            }.onFailure { error ->
                _uiState.value = IngestionUiState.Error(error.localizedMessage ?: "Failed to process topic.")
            }
        }
    }

    fun loadSavedTopic(saved: TopicMasteryEntity) {
        try {
            val response = gson.fromJson(saved.jsonContent, TopicDeepMasteryResponseDto::class.java)
            if (response != null) {
                _deepMastery.value = response
                _gradedCards.value = response.gradedCards
                _uiState.value = IngestionUiState.DeepMasterySuccess(response)
            }
        } catch (e: Exception) {
            _uiState.value = IngestionUiState.Error("Failed to load saved topic: ${e.localizedMessage}")
        }
    }

    fun deleteSavedTopic(topicId: Long) {
        viewModelScope.launch {
            repository.deleteSavedTopicById(topicId)
        }
    }

    fun saveGradedCardsToDeck(
        targetDeckId: Long,
        newDeckName: String? = null,
        selectedDifficulty: String = "ALL"
    ) {
        val allCards = _gradedCards.value
        val filtered = if (selectedDifficulty == "ALL") {
            allCards
        } else {
            allCards.filter { it.difficulty.equals(selectedDifficulty, ignoreCase = true) }
        }

        if (filtered.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = IngestionUiState.Loading

            val finalDeckId = if (targetDeckId == -1L && !newDeckName.isNullOrBlank()) {
                val now = System.currentTimeMillis()
                repository.insertDeck(
                    DeckEntity(
                        name = newDeckName.trim(),
                        description = "Deconstructed: ${_deepMastery.value?.topicTitle ?: "Deep Study"}",
                        category = DeckCategory.MATHEMATICS,
                        hexColor = "#818CF8",
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                targetDeckId
            }

            val now = System.currentTimeMillis()
            val entities = filtered.map { dto ->
                val cardType = try {
                    when (dto.type.lowercase()) {
                        "cloze" -> CardType.CLOZE_FORMULA
                        "feynman" -> CardType.FEYNMAN_CONCEPT
                        "derivation" -> CardType.DERIVATION_STEP
                        else -> CardType.ATOMIC_FACT
                    }
                } catch (e: Exception) {
                    CardType.ATOMIC_FACT
                }

                CardEntity(
                    deckId = finalDeckId,
                    type = cardType,
                    prompt = dto.prompt,
                    answer = dto.answer,
                    explanation = dto.explanation ?: "",
                    latexFormula = dto.latexFormula,
                    clozeOcclusion = dto.clozeOcclusion,
                    derivationStepsJson = dto.derivationSteps?.let { gson.toJson(it) },
                    keyTermsJson = dto.keyTerms?.let { gson.toJson(it) },
                    misconceptionsJson = dto.misconceptions?.let { gson.toJson(it) },
                    difficulty = dto.difficulty,
                    dueDate = now
                )
            }

            repository.insertCards(entities)
            _uiState.value = IngestionUiState.Saved
        }
    }

    fun resetState() {
        _uiState.value = IngestionUiState.Idle
        _deepMastery.value = null
        _gradedCards.value = emptyList()
        clearSelectedImage()
    }
}
