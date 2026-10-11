package com.nexus.quiz.engine

import com.google.gson.Gson
import com.nexus.quiz.data.local.PreferencesManager
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.data.model.FeynmanAuditResponseDto
import com.nexus.quiz.data.model.GeminiBlob
import com.nexus.quiz.data.model.GeminiContent
import com.nexus.quiz.data.model.GeminiGenerateContentRequest
import com.nexus.quiz.data.model.GeminiGenerateContentResponse
import com.nexus.quiz.data.model.GeminiGenerationConfig
import com.nexus.quiz.data.model.GeminiPart
import com.nexus.quiz.data.model.IngestionResponseDto
import com.nexus.quiz.data.model.TopicDeepMasteryResponseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class GeminiService(
    private val apiKeyProvider: () -> String,
    private val modelProvider: () -> String
) {
    private val gson = Gson()
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun executeGenerateContent(
        apiKey: String,
        requestBody: GeminiGenerateContentRequest
    ): Result<String> {
        var model = modelProvider().trim()
        if (PreferencesManager.isDeprecatedModel(model)) {
            model = PreferencesManager.DEFAULT_MODEL
        }

        var url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
        var httpRequest = Request.Builder()
            .url(url)
            .post(gson.toJson(requestBody).toRequestBody(jsonMediaType))
            .build()

        var response = client.newCall(httpRequest).execute()
        var responseString = response.body?.string().orEmpty()

        // Fallback: If model returned 404 (model sunset or not found), automatically fallback to DEFAULT_MODEL
        if (!response.isSuccessful && response.code == 404 && model != PreferencesManager.DEFAULT_MODEL) {
            model = PreferencesManager.DEFAULT_MODEL
            url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
            httpRequest = Request.Builder()
                .url(url)
                .post(gson.toJson(requestBody).toRequestBody(jsonMediaType))
                .build()
            response = client.newCall(httpRequest).execute()
            responseString = response.body?.string().orEmpty()
        }

        if (!response.isSuccessful) {
            val friendlyError = try {
                val errResp = gson.fromJson(responseString, GeminiGenerateContentResponse::class.java)
                errResp?.error?.message ?: responseString
            } catch (_: Exception) {
                responseString
            }
            return Result.failure(Exception("Gemini API Error (${response.code}): $friendlyError"))
        }

        val geminiResponse = gson.fromJson(responseString, GeminiGenerateContentResponse::class.java)
        val jsonText = geminiResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: return Result.failure(Exception("Empty response received from Gemini."))

        val cleanedJson = jsonText.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        return Result.success(cleanedJson)
    }

    /**
     * Multimodal Concept Mastery Breakdown:
     * Deconstructs a topic (via text, uploaded screenshots, slides, or problem images) into
     * first-principles intuition, real-world everyday applications, worked step-by-step examples,
     * and graded active-recall flashcards (Fundamental -> Medium -> Hard).
     */
    suspend fun analyzeAndBreakdownTopic(
        rawNotes: String,
        imageBase64: String? = null,
        imageMimeType: String? = "image/jpeg",
        topicHint: String = ""
    ): Result<TopicDeepMasteryResponseDto> = withContext(Dispatchers.IO) {
        val apiKey = apiKeyProvider().trim()
        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                IllegalStateException("Gemini API Key is not configured. Please add your key in Settings.")
            )
        }

        val systemPrompt = """
            You are Nexus: an elite cognitive engineering professor and deep-learning tutor.
            Your mission is to guide a student to MASTERY of a complex or tricky technical topic (e.g. Logarithms, Calculus, Fluid Dynamics, GST, Thermodynamics, Complex Numbers).
            
            When given lecture notes, textbook excerpts, or a screenshot/slide of a problem or diagram:
            1. BREAK DOWN THE CORE CONCEPTS FIRST from first-principles before diving into granular slide text.
            2. PROVIDE VISCERAL INTUITION: Explain what the concept ACTUALLY IS in plain, intuitive English (e.g. for Logarithms: It is an "order of magnitude counter" or the inverse of exponentiation).
            3. EXPLAIN WHY IT EXISTS: What historical, physical, or mathematical problem did it solve?
            4. DETAIL 3-5 EVERYDAY & ENGINEERING APPLICATIONS: Explain specifically where it is used (e.g. Richter scale for earthquakes, Decibel scale in sound/acoustics, pH scale in chemistry, O(log n) binary search algorithms in CS, compound interest doubling in finance). Provide the exact LaTeX formula for each!
            5. PROVIDE FUNDAMENTAL RULES & LAWS: Format all equations in clean LaTeX ($...$ or $$...$$) with 1-sentence intuition for each rule.
            6. SOLVE 2-3 STEP-BY-STEP WORKED EXAMPLES: Ranging from Fundamental to Hard/Exam level with clear derivation steps.
            7. ANALYZE SLIDE/IMAGE MATERIAL: If an image or specific note was provided, deconstruct what is specifically shown on the slide.
            8. GENERATE GRADED ACTIVE RECALL CARDS:
               - You MUST generate between 10 to 20 comprehensive flashcards to thoroughly test all facets of the provided notes/topic (do NOT just generate 3-4 cards).
               - Distribute cards across difficulty tiers: at least 4-6 FUNDAMENTAL, 4-6 MEDIUM, and 3-6 HARD cards.
               - "FUNDAMENTAL": Core definitions, cloze rule occlusions, intuitive checks, base conversions.
               - "MEDIUM": Standard algebraic manipulations, change of base, property applications, evaluating expressions.
               - "HARD": Multi-step derivations, tricky misconception traps, real-world rate problems, edge cases.
               CRITICAL RULE: Maintain strict atomicity (1 discrete mental leap per card). Use `[cloze]...[/cloze]` for occluded variables. Format all math expressions and cloze targets in LaTeX using $...$ notation.
            
            OUTPUT STRICT JSON SCHEMA:
            {
              "topic_title": "Topic Name (e.g. Logarithms & Exponential Inversion)",
              "core_intuition": "Deep, intuitive plain-English explanation...",
              "why_it_exists": "Why this mathematical tool was invented...",
              "everyday_applications": [
                {
                  "field": "Acoustics & Sound Engineering",
                  "title": "Decibel Scale (dB)",
                  "explanation": "Human ears perceive loudness logarithmically rather than linearly...",
                  "latex_formula": "dB = 10 \\log_{10}\\left(\\frac{P}{P_0}\\right)"
                }
              ],
              "fundamental_rules": [
                {
                  "name": "Product Rule",
                  "latex_formula": "\\log_b(xy) = \\log_b x + \\log_b y",
                  "intuition": "Multiplying numbers in linear space equals adding their logarithmic magnitudes."
                }
              ],
              "worked_examples": [
                {
                  "problem_title": "Solving for Exponents in Radioactive Decay",
                  "problem_statement": "Solve for t: 100 = 50 \\cdot 2^{t/5}",
                  "difficulty": "MEDIUM",
                  "steps": [
                    "Step 1: Divide both sides by 50: 2 = 2^{t/5}",
                    "Step 2: Take log base 2 of both sides: \\log_2(2) = \\log_2(2^{t/5})",
                    "Step 3: Simplify: 1 = t/5 \\implies t = 5"
                  ],
                  "final_answer": "t = 5",
                  "key_takeaway": "Logarithms liberate variables trapped inside exponents."
                }
              ],
              "slide_specific_analysis": "Explanation of the uploaded screenshot or notes...",
              "graded_cards": [
                {
                  "difficulty": "FUNDAMENTAL",
                  "type": "cloze",
                  "prompt": "The definition of $\\log_b(a) = c$ in exponential form is [cloze]b^c = a[/cloze].",
                  "answer": "b^c = a",
                  "explanation": "Logarithms ask: what exponent on base b produces a?",
                  "latex_formula": "\\log_b(a) = c \\iff b^c = a",
                  "cloze_occlusion": "b^c = a"
                },
                {
                  "difficulty": "MEDIUM",
                  "type": "cloze",
                  "prompt": "The Change of Base rule states $\\log_b a = [cloze]\\frac{\\ln a}{\\ln b}[/cloze]$.",
                  "answer": "\\frac{\\ln a}{\\ln b}",
                  "explanation": "Allows evaluating arbitrary bases using natural or common logs.",
                  "latex_formula": "\\log_b a = \\frac{\\ln a}{\\ln b}",
                  "cloze_occlusion": "\\frac{\\ln a}{\\ln b}"
                },
                {
                  "difficulty": "HARD",
                  "type": "feynman",
                  "prompt": "Explain why an earthquake of Richter magnitude 7.0 has 1000 times more energy than magnitude 5.0.",
                  "answer": "Richter scale magnitude measures wave amplitude where each +1.0 represents a 10x increase in amplitude and approximately a 31.6x (10^1.5) increase in radiated seismic energy. A difference of 2.0 corresponds to 10^(1.5 * 2) = 10^3 = 1000 times the energy.",
                  "explanation": "Logarithmic scaling of seismic moment energy.",
                  "key_terms": ["Richter scale", "amplitude", "31.6x", "logarithmic", "seismic energy"]
                }
              ]
            }
        """.trimIndent()

        val parts = mutableListOf<GeminiPart>()
        parts.add(GeminiPart(text = "$systemPrompt\n\nTopic Hint / Scope: $topicHint\n\nUser Notes / Material:\n$rawNotes"))

        // Add image part if provided (Multimodal Vision)
        if (!imageBase64.isNullOrBlank()) {
            parts.add(
                GeminiPart(
                    inlineData = GeminiBlob(
                        mimeType = imageMimeType ?: "image/jpeg",
                        data = imageBase64
                    )
                )
            )
        }

        val requestBody = GeminiGenerateContentRequest(
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = parts
                )
            ),
            generationConfig = GeminiGenerationConfig(
                responseMimeType = "application/json",
                temperature = 0.2f
            )
        )

        try {
            val contentResult = executeGenerateContent(apiKey, requestBody)
            contentResult.mapCatching { json ->
                gson.fromJson(json, TopicDeepMasteryResponseDto::class.java)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Standard note shredder (backward-compatible)
     */
    suspend fun shredNotesIntoCards(
        rawNotes: String,
        deckTitleHint: String = "",
        targetTypes: List<CardType> = CardType.values().toList()
    ): Result<IngestionResponseDto> = withContext(Dispatchers.IO) {
        val apiKey = apiKeyProvider().trim()
        if (apiKey.isBlank()) {
            return@withContext Result.failure(
                IllegalStateException("Gemini API Key is not configured. Please add your key in Settings.")
            )
        }

        val typeInstructions = targetTypes.joinToString(", ") { "${it.name} (${it.description})" }

        val systemPrompt = """
            You are Nexus: an elite cognitive engineering and memory retention system.
            Your task is to take raw technical notes (engineering, mathematics formulas, physical sciences, GST) and SHRED them into ATOMIC FLASHCARDS.
            
            CRITICAL RULES:
            1. RULE OF ATOMICITY: One card tests exactly one discrete mental leap.
            2. QUANTITY: Generate between 10 to 25 comprehensive flashcards covering every distinct concept, definition, formula, rule, and example from the notes. Do NOT stop after only 3-4 cards.
            3. LaTeX MATHEMATICS: All formulas in LaTeX ($...$ or $$...$$).
            4. CLOZE DELETIONS: Wrap occluded terms in `[cloze]term[/cloze]`. Format math cloze values with LaTeX syntax.
            5. FEYNMAN CONCEPTS: Supply 3-5 `key_terms`.
            
            Target Card Types: $typeInstructions
            
            OUTPUT JSON SCHEMA:
            {
              "deck_title": "Concise Deck Name",
              "topic_summary": "1-sentence summary",
              "cards": [
                {
                  "type": "cloze | feynman | derivation | concept",
                  "prompt": "Question string with LaTeX",
                  "answer": "Exact atomic answer",
                  "explanation": "Concise explanation",
                  "latex_formula": "Optional LaTeX formula",
                  "cloze_occlusion": "Occluded term",
                  "derivation_steps": ["step 1"],
                  "key_terms": ["term1"],
                  "misconceptions": ["mistake 1"]
                }
              ]
            }
        """.trimIndent()

        val userPrompt = "Topic Hint: $deckTitleHint\n\nRaw Notes:\n$rawNotes"
        val fullPrompt = "$systemPrompt\n\n$userPrompt"

        val requestBody = GeminiGenerateContentRequest(
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = fullPrompt))
                )
            ),
            generationConfig = GeminiGenerationConfig(
                responseMimeType = "application/json",
                temperature = 0.2f
            )
        )

        try {
            val contentResult = executeGenerateContent(apiKey, requestBody)
            contentResult.mapCatching { json ->
                gson.fromJson(json, IngestionResponseDto::class.java)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Audits the user's typed explanation for Feynman concept recall.
     */
    suspend fun auditFeynmanExplanation(
        prompt: String,
        expectedAnswer: String,
        keyTerms: List<String>,
        userExplanation: String
    ): Result<FeynmanAuditResponseDto> = withContext(Dispatchers.IO) {
        val apiKey = apiKeyProvider().trim()
        if (apiKey.isBlank()) {
            return@withContext Result.failure(IllegalStateException("Gemini API Key is not configured."))
        }

        val auditPrompt = """
            You are a strict academic and engineering professor evaluating a student's active recall explanation.
            
            QUESTION PROMPT: $prompt
            GOLD STANDARD ANSWER: $expectedAnswer
            REQUIRED KEY TERMINOLOGY: ${keyTerms.joinToString(", ")}
            
            STUDENT'S TYPED EXPLANATION:
            "$userExplanation"
            
            EVALUATION CRITERIA:
            1. Did the student explain the underlying mechanism accurately without hand-waving?
            2. Which key terms were successfully included vs missing?
            3. Are there any misconceptions, flawed physics/math, or hallucinations?
            4. Provide a 2-sentence constructive critique.
            5. Suggest a rating: "AGAIN" (score < 40), "HARD" (40-69), "GOOD" (70-89), "EASY" (90-100).
            
            OUTPUT JSON SCHEMA:
            {
              "score": 85,
              "terminology_matched": ["vapor pressure", "static pressure"],
              "terminology_missing": ["pitting erosion"],
              "misconceptions_detected": [],
              "critique": "Solid explanation of pressure drop. Make sure to note how subsequent bubble implosion causes physical pitting.",
              "suggested_rating": "GOOD"
            }
        """.trimIndent()

        val requestBody = GeminiGenerateContentRequest(
            contents = listOf(
                GeminiContent(
                    role = "user",
                    parts = listOf(GeminiPart(text = auditPrompt))
                )
            ),
            generationConfig = GeminiGenerationConfig(
                responseMimeType = "application/json",
                temperature = 0.1f
            )
        )

        try {
            val contentResult = executeGenerateContent(apiKey, requestBody)
            contentResult.mapCatching { json ->
                gson.fromJson(json, FeynmanAuditResponseDto::class.java)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
