package com.nexus.quiz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nexus.quiz.data.model.DeckCategory

@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String = "",
    val category: DeckCategory = DeckCategory.ENGINEERING,
    val hexColor: String = "#38BDF8",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
