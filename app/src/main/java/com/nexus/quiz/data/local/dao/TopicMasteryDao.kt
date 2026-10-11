package com.nexus.quiz.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nexus.quiz.data.local.entity.TopicMasteryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicMasteryDao {
    @Query("SELECT * FROM saved_topics ORDER BY createdAt DESC")
    fun getAllSavedTopics(): Flow<List<TopicMasteryEntity>>

    @Query("SELECT * FROM saved_topics WHERE id = :id")
    suspend fun getTopicById(id: Long): TopicMasteryEntity?

    @Query("SELECT * FROM saved_topics WHERE topicTitle = :title LIMIT 1")
    suspend fun getTopicByTitle(title: String): TopicMasteryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopic(topic: TopicMasteryEntity): Long

    @Delete
    suspend fun deleteTopic(topic: TopicMasteryEntity)

    @Query("DELETE FROM saved_topics WHERE id = :id")
    suspend fun deleteTopicById(id: Long)
}
