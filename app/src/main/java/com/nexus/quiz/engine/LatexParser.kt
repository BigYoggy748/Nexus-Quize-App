package com.nexus.quiz.engine

object LatexParser {
    private val CLOZE_REGEX = Regex("\\[cloze\\](.*?)\\[/cloze\\]", RegexOption.DOT_MATCHES_ALL)

    /**
     * Ensures math expressions (with exponents ^, fractions, logs, greek letters, etc.)
     * are properly enclosed in LaTeX inline math delimiters so KaTeX can render them.
     */
    fun ensureMathDelimiters(content: String): String {
        val trimmed = content.trim()
        if (trimmed.startsWith("$") || trimmed.startsWith("\\(") || trimmed.startsWith("\\[")) {
            return trimmed
        }
        val hasMathCharacters = trimmed.any { it in "^_\\=+-*/<>{}~" } ||
            trimmed.contains("log") || trimmed.contains("ln") || trimmed.contains("pi") ||
            trimmed.contains("sqrt") || trimmed.contains("frac")
        return if (hasMathCharacters) "\\($trimmed\\)" else trimmed
    }

    /**
     * Formats raw prompt text with KaTeX delimiters and cloze occlusions.
     * If isRevealed is false, occludes the cloze content. If true, reveals it with green styling.
     */
    fun formatForWebView(
        text: String,
        latexFormula: String? = null,
        isRevealed: Boolean = false
    ): String {
        var processed = text

        // Replace cloze tags
        processed = CLOZE_REGEX.replace(processed) { matchResult ->
            val hiddenContent = matchResult.groupValues[1]
            if (isRevealed) {
                val formattedMath = ensureMathDelimiters(hiddenContent)
                "<span class='cloze-revealed'>$formattedMath</span>"
            } else {
                "<span class='cloze-occluded'>[ ... ]</span>"
            }
        }

        // If there is a separate latex formula provided
        val formulaHtml = if (!latexFormula.isNullOrBlank()) {
            val cleanText = text.replace("\\s+".toRegex(), "").replace("$", "")
            val cleanFormula = latexFormula.replace("\\s+".toRegex(), "").replace("$", "")
            // Avoid duplicate rendering if the formula is already embedded inside the prompt
            if (cleanText.contains(cleanFormula) || cleanFormula.contains(cleanText)) {
                ""
            } else {
                var formulaProcessed = latexFormula
                formulaProcessed = CLOZE_REGEX.replace(formulaProcessed) { matchResult ->
                    val hiddenContent = matchResult.groupValues[1]
                    if (isRevealed) {
                        "\\mathbf{$hiddenContent}"
                    } else {
                        "\\underline{\\quad ? \\quad}"
                    }
                }
                val trimmed = formulaProcessed.trim().removePrefix("$$").removeSuffix("$$").removePrefix("$").removeSuffix("$")
                "<div class='math-display'>\$\$$trimmed\$\$</div>"
            }
        } else ""

        // Wrap newlines into <p> or <br>
        val paragraphs = processed.split("\n\n").joinToString("") { paragraph ->
            "<p>${paragraph.replace("\n", "<br>")}</p>"
        }

        return "$paragraphs$formulaHtml"
    }

    /**
     * Extract the raw occluded text inside the first [cloze]...[/cloze] tag
     */
    fun extractClozeAnswer(text: String): String? {
        val match = CLOZE_REGEX.find(text)
        return match?.groupValues?.get(1)?.trim()
    }
}
