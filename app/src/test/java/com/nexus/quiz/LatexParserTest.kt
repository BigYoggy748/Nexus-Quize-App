package com.nexus.quiz

import com.nexus.quiz.engine.LatexParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LatexParserTest {

    @Test
    fun testExtractClozeAnswer() {
        val prompt = "The equation is P + [cloze]\\frac{1}{2}\\rho v^2[/cloze] + \\rho gz = C"
        val answer = LatexParser.extractClozeAnswer(prompt)
        assertEquals("\\frac{1}{2}\\rho v^2", answer)
    }

    @Test
    fun testOccludedHtmlFormatting() {
        val prompt = "Ideal gas: P V = [cloze]n R T[/cloze]"
        val html = LatexParser.formatForWebView(prompt, isRevealed = false)

        assertTrue(html.contains("cloze-occluded"))
        assertTrue(html.contains("[ ... ]"))
    }

    @Test
    fun testRevealedHtmlFormatting() {
        val prompt = "Ideal gas: P V = [cloze]n R T[/cloze]"
        val html = LatexParser.formatForWebView(prompt, isRevealed = true)

        assertTrue(html.contains("cloze-revealed"))
        assertTrue(html.contains("n R T"))
    }
}
