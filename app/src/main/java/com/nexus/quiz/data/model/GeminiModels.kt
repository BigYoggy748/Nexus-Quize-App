package com.nexus.quiz.data.model

import com.google.gson.annotations.SerializedName

/**
 * Deep concept breakdown & mastery response from Gemini
 */
data class TopicDeepMasteryResponseDto(
    @SerializedName("topic_title")
    val topicTitle: String = "",

    @SerializedName("core_intuition")
    val coreIntuition: String = "",

    @SerializedName("why_it_exists")
    val whyItExists: String = "",

    @SerializedName("everyday_applications")
    val everydayApplications: List<RealWorldApplicationDto> = emptyList(),

    @SerializedName("fundamental_rules")
    val fundamentalRules: List<FundamentalRuleDto> = emptyList(),

    @SerializedName("worked_examples")
    val workedExamples: List<WorkedExampleDto> = emptyList(),

    @SerializedName("slide_specific_analysis")
    val slideSpecificAnalysis: String? = null,

    @SerializedName("graded_cards")
    val gradedCards: List<GradedCardDto> = emptyList()
)

data class RealWorldApplicationDto(
    @SerializedName("field")
    val field: String = "", // e.g. "Acoustics & Sound", "Earthquake Geology", "Chemistry", "Computer Science", "Finance"

    @SerializedName("title")
    val title: String = "", // e.g. "Decibel Scale (dB)"

    @SerializedName("explanation")
    val explanation: String = "",

    @SerializedName("latex_formula")
    val latexFormula: String? = null
)

data class FundamentalRuleDto(
    @SerializedName("name")
    val name: String = "", // e.g. "Product Rule", "Change of Base"

    @SerializedName("latex_formula")
    val latexFormula: String = "",

    @SerializedName("intuition")
    val intuition: String = ""
)

data class WorkedExampleDto(
    @SerializedName("problem_title")
    val problemTitle: String = "",

    @SerializedName("problem_statement")
    val problemStatement: String = "",

    @SerializedName("difficulty")
    val difficulty: String = "MEDIUM", // "FUNDAMENTAL" | "MEDIUM" | "HARD"

    @SerializedName("steps")
    val steps: List<String> = emptyList(),

    @SerializedName("final_answer")
    val finalAnswer: String = "",

    @SerializedName("key_takeaway")
    val keyTakeaway: String = ""
)

data class GradedCardDto(
    @SerializedName("difficulty")
    val difficulty: String = "MEDIUM", // "FUNDAMENTAL" | "MEDIUM" | "HARD"

    @SerializedName("type")
    val type: String = "cloze", // "cloze" | "feynman" | "derivation" | "concept"

    @SerializedName("prompt")
    val prompt: String = "",

    @SerializedName("answer")
    val answer: String = "",

    @SerializedName("explanation")
    val explanation: String? = null,

    @SerializedName("latex_formula")
    val latexFormula: String? = null,

    @SerializedName("cloze_occlusion")
    val clozeOcclusion: String? = null,

    @SerializedName("derivation_steps")
    val derivationSteps: List<String>? = null,

    @SerializedName("key_terms")
    val keyTerms: List<String>? = null,

    @SerializedName("misconceptions")
    val misconceptions: List<String>? = null
)

// Legacy / Standard Ingestion DTO
data class IngestionResponseDto(
    @SerializedName("deck_title")
    val deckTitle: String? = null,

    @SerializedName("topic_summary")
    val topicSummary: String? = null,

    @SerializedName("cards")
    val cards: List<AtomicCardDto> = emptyList()
)

data class AtomicCardDto(
    @SerializedName("type")
    val type: String, // "cloze" | "feynman" | "derivation" | "concept"

    @SerializedName("prompt")
    val prompt: String,

    @SerializedName("answer")
    val answer: String,

    @SerializedName("explanation")
    val explanation: String? = null,

    @SerializedName("latex_formula")
    val latexFormula: String? = null,

    @SerializedName("cloze_occlusion")
    val clozeOcclusion: String? = null,

    @SerializedName("derivation_steps")
    val derivationSteps: List<String>? = null,

    @SerializedName("key_terms")
    val keyTerms: List<String>? = null,

    @SerializedName("misconceptions")
    val misconceptions: List<String>? = null
)

data class FeynmanAuditResponseDto(
    @SerializedName("score")
    val score: Int = 0,

    @SerializedName("terminology_matched")
    val terminologyMatched: List<String> = emptyList(),

    @SerializedName("terminology_missing")
    val terminologyMissing: List<String> = emptyList(),

    @SerializedName("misconceptions_detected")
    val misconceptionsDetected: List<String> = emptyList(),

    @SerializedName("critique")
    val critique: String = "",

    @SerializedName("suggested_rating")
    val suggestedRating: String = "GOOD" // AGAIN | HARD | GOOD | EASY
)

// Gemini Multimodal API Request/Response DTOs
data class GeminiGenerateContentRequest(
    @SerializedName("contents")
    val contents: List<GeminiContent>,

    @SerializedName("generationConfig")
    val generationConfig: GeminiGenerationConfig? = null
)

data class GeminiContent(
    @SerializedName("role")
    val role: String = "user",

    @SerializedName("parts")
    val parts: List<GeminiPart>
)

data class GeminiPart(
    @SerializedName("text")
    val text: String? = null,

    @SerializedName("inline_data")
    val inlineData: GeminiBlob? = null
)

data class GeminiBlob(
    @SerializedName("mime_type")
    val mimeType: String,

    @SerializedName("data")
    val data: String // Base64 encoded string
)

data class GeminiGenerationConfig(
    @SerializedName("response_mime_type")
    val responseMimeType: String? = "application/json",

    @SerializedName("temperature")
    val temperature: Float = 0.2f
)

data class GeminiGenerateContentResponse(
    @SerializedName("candidates")
    val candidates: List<GeminiCandidate>? = null,

    @SerializedName("error")
    val error: GeminiError? = null
)

data class GeminiCandidate(
    @SerializedName("content")
    val content: GeminiContent? = null,

    @SerializedName("finishReason")
    val finishReason: String? = null
)

data class GeminiError(
    @SerializedName("code")
    val code: Int? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("status")
    val status: String? = null
)
