package com.nexus.quiz.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nexus.quiz.data.model.SrsRating

@Entity(
    tableName = "review_logs",
    foreignKeys = [
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("cardId"),
        Index("reviewTimestamp")
    ]
)
data class ReviewLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: Long,
    val rating: SrsRating,
    val reviewTimestamp: Long = System.currentTimeMillis(),
    val intervalBefore: Int,
    val intervalAfter: Int,
    val easeFactorBefore: Float,
    val easeFactorAfter: Float,
    val timeSpentMs: Long = 0
)
