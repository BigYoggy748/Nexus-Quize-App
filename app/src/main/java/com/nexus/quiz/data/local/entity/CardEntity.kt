package com.nexus.quiz.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nexus.quiz.data.model.CardType

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("deckId"),
        Index("dueDate"),
        Index("type")
    ]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val deckId: Long,
    val type: CardType = CardType.ATOMIC_FACT,
    val prompt: String,
    val answer: String,
    val explanation: String = "",
    val latexFormula: String? = null,
    val clozeOcclusion: String? = null,
    val derivationStepsJson: String? = null, // JSON array of steps
    val keyTermsJson: String? = null,        // JSON array of key terms for Feynman
    val misconceptionsJson: String? = null,  // JSON array of misconceptions
    val difficulty: String = "MEDIUM",       // "FUNDAMENTAL" | "MEDIUM" | "HARD"

    // Spaced Repetition (SM-2) State
    val interval: Int = 0,               // in days (0 means learning/due now)
    val repetitions: Int = 0,            // successful consecutive recall count
    val easeFactor: Float = 2.5f,        // default SM-2 ease factor
    val lapses: Int = 0,                 // number of times card was failed (Again)
    val dueDate: Long = System.currentTimeMillis(), // epoch timestamp in ms when due
    val lastReviewedDate: Long? = null,  // epoch timestamp in ms
    val createdAt: Long = System.currentTimeMillis()
)
