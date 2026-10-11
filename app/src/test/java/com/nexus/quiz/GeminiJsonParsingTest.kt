package com.nexus.quiz

import com.google.gson.Gson
import com.nexus.quiz.data.model.FeynmanAuditResponseDto
import com.nexus.quiz.data.model.IngestionResponseDto
import com.nexus.quiz.data.model.TopicDeepMasteryResponseDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GeminiJsonParsingTest {

    private val gson = Gson()

    @Test
    fun testTopicDeepMasteryDeserialization() {
        val json = """
            {
              "topic_title": "Logarithms & Exponential Inversion",
              "core_intuition": "A logarithm is an order of magnitude counter that asks: to what power must we raise the base to produce this number?",
              "why_it_exists": "Invented to transform multi-digit multiplication into addition.",
              "everyday_applications": [
                {
                  "field": "Acoustics & Sound",
                  "title": "Decibel Scale (dB)",
                  "explanation": "Human ears perceive sound pressure logarithmically.",
                  "latex_formula": "dB = 10 \\log_{10}(P/P_0)"
                },
                {
                  "field": "Earthquake Geology",
                  "title": "Richter Scale",
                  "explanation": "Every +1 magnitude is a 10x increase in wave amplitude.",
                  "latex_formula": "M = \\log_{10}(I/S)"
                }
              ],
              "fundamental_rules": [
                {
                  "name": "Product Rule",
                  "latex_formula": "\\log_b(xy) = \\log_b x + \\log_b y",
                  "intuition": "Multiplying in linear space equals adding in log space."
                }
              ],
              "worked_examples": [
                {
                  "problem_title": "Solve 2^x = 32",
                  "problem_statement": "Find x in 2^x = 32",
                  "difficulty": "FUNDAMENTAL",
                  "steps": ["Step 1: Take log base 2: x = \\log_2(32)", "Step 2: 2^5 = 32, so x = 5"],
                  "final_answer": "x = 5",
                  "key_takeaway": "Logarithm undoes exponentiation."
                }
              ],
              "slide_specific_analysis": "The slide covers Napierian logarithms and change of base.",
              "graded_cards": [
                {
                  "difficulty": "FUNDAMENTAL",
                  "type": "cloze",
                  "prompt": "If ${'$'}b^y = x${'$'}, then in logarithmic form: [cloze]\\log_b(x) = y[/cloze]",
                  "answer": "\\log_b(x) = y",
                  "cloze_occlusion": "\\log_b(x) = y"
                },
                {
                  "difficulty": "HARD",
                  "type": "feynman",
                  "prompt": "Why is the change of base formula valid?",
                  "answer": "Let y = \\log_b a, so b^y = a. Take natural log: y \\ln b = \\ln a, hence y = \\ln a / \\ln b.",
                  "key_terms": ["natural log", "exponential form", "divide"]
                }
              ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, TopicDeepMasteryResponseDto::class.java)
        assertNotNull(response)
        assertEquals("Logarithms & Exponential Inversion", response.topicTitle)
        assertEquals(2, response.everydayApplications.size)
        assertEquals("Acoustics & Sound", response.everydayApplications[0].field)
        assertEquals(1, response.fundamentalRules.size)
        assertEquals(1, response.workedExamples.size)
        assertEquals(2, response.gradedCards.size)
        assertEquals("FUNDAMENTAL", response.gradedCards[0].difficulty)
        assertEquals("HARD", response.gradedCards[1].difficulty)
    }

    @Test
    fun testIngestionResponseDeserialization() {
        val json = """
            {
              "deck_title": "Fluid Mechanics",
              "topic_summary": "Bernoulli principle and cavitation",
              "cards": [
                {
                  "type": "cloze",
                  "prompt": "Dynamic pressure is [cloze]\\frac{1}{2}\\rho v^2[/cloze]",
                  "answer": "\\frac{1}{2}\\rho v^2",
                  "explanation": "Kinetic energy per unit volume.",
                  "latex_formula": "P_d = \\frac{1}{2}\\rho v^2"
                }
              ]
            }
        """.trimIndent()

        val response = gson.fromJson(json, IngestionResponseDto::class.java)
        assertNotNull(response)
        assertEquals("Fluid Mechanics", response.deckTitle)
        assertEquals(1, response.cards.size)
        assertEquals("cloze", response.cards[0].type)
    }

    @Test
    fun testFeynmanAuditResponseDeserialization() {
        val json = """
            {
              "score": 92,
              "terminology_matched": ["vapor pressure", "static pressure", "implosion"],
              "terminology_missing": [],
              "misconceptions_detected": [],
              "critique": "Excellent explanation of fluid cavitation.",
              "suggested_rating": "EASY"
            }
        """.trimIndent()

        val audit = gson.fromJson(json, FeynmanAuditResponseDto::class.java)
        assertNotNull(audit)
        assertEquals(92, audit.score)
        assertEquals(3, audit.terminologyMatched.size)
        assertTrue(audit.terminologyMissing.isEmpty())
        assertEquals("EASY", audit.suggestedRating)
    }

    @Test
    fun testModelDeprecationAndDefaultModel() {
        assertEquals("gemini-3.6-flash", com.nexus.quiz.data.local.PreferencesManager.DEFAULT_MODEL)
        assertTrue(com.nexus.quiz.data.local.PreferencesManager.isDeprecatedModel("gemini-2.0-flash"))
        assertTrue(com.nexus.quiz.data.local.PreferencesManager.isDeprecatedModel("gemini-1.5-flash"))
        assertTrue(com.nexus.quiz.data.local.PreferencesManager.isDeprecatedModel("gemini-1.5-pro"))
        assertTrue(com.nexus.quiz.data.local.PreferencesManager.isDeprecatedModel(""))
        assertTrue(!com.nexus.quiz.data.local.PreferencesManager.isDeprecatedModel("gemini-3.6-flash"))
        assertTrue(!com.nexus.quiz.data.local.PreferencesManager.isDeprecatedModel("gemini-3.8-flash"))
        assertTrue(!com.nexus.quiz.data.local.PreferencesManager.isDeprecatedModel("gemini-3.1-pro"))
    }
}
