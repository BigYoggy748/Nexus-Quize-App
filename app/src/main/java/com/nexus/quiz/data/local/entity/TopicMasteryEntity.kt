package com.nexus.quiz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_topics")
data class TopicMasteryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topicTitle: String,
    val topicHint: String = "",
    val rawNotes: String = "",
    val jsonContent: String, // Complete serialized TopicDeepMasteryResponseDto
    val cardCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
