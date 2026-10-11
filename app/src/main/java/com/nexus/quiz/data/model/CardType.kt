package com.nexus.quiz.data.model

enum class CardType(val displayName: String, val description: String) {
    CLOZE_FORMULA(
        displayName = "Cloze Formula",
        description = "Occludes formula variables & technical constants"
    ),
    FEYNMAN_CONCEPT(
        displayName = "Feynman Concept",
        description = "Type your explanation; AI audits terminology & misconceptions"
    ),
    DERIVATION_STEP(
        displayName = "Derivation Step",
        description = "Sequential mathematical or logical proof step-by-step"
    ),
    ATOMIC_FACT(
        displayName = "Atomic Fact",
        description = "Direct discrete principle, definition, or GST fact"
    )
}
