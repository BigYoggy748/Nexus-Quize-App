package com.nexus.quiz.data.model

enum class SrsRating(val score: Int, val label: String, val description: String) {
    AGAIN(
        score = 1,
        label = "Again",
        description = "Complete blackout / incorrect. Review today."
    ),
    HARD(
        score = 2,
        label = "Hard",
        description = "Recalled with significant hesitation / partial error."
    ),
    GOOD(
        score = 3,
        label = "Good",
        description = "Correct recall with slight mental effort."
    ),
    EASY(
        score = 4,
        label = "Easy",
        description = "Instantaneous, effortless recall. Advance interval."
    )
}
