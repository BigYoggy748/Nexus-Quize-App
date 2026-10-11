package com.nexus.quiz.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nexus.quiz.data.local.entity.CardEntity
import kotlinx.coroutines.flow.Flow

data class RetentionOverview(
    val totalCards: Int,
    val dueCards: Int,
    val learningCards: Int,
    val youngCards: Int,
    val matureCards: Int
)

@Dao
interface CardDao {
    @Query("SELECT * FROM cards WHERE deckId = :deckId ORDER BY createdAt DESC")
    fun getCardsForDeck(deckId: Long): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :cardId LIMIT 1")
    suspend fun getCardById(cardId: Long): CardEntity?

    @Query("SELECT * FROM cards WHERE dueDate <= :currentTimestamp ORDER BY dueDate ASC")
    fun getAllDueCards(currentTimestamp: Long): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE deckId = :deckId AND dueDate <= :currentTimestamp ORDER BY dueDate ASC")
    fun getDueCardsForDeck(deckId: Long, currentTimestamp: Long): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE dueDate <= :currentTimestamp ORDER BY dueDate ASC LIMIT :limit")
    suspend fun getDueCardsBatch(currentTimestamp: Long, limit: Int = 50): List<CardEntity>

    @Query("SELECT * FROM cards WHERE deckId = :deckId AND dueDate <= :currentTimestamp ORDER BY dueDate ASC LIMIT :limit")
    suspend fun getDueCardsForDeckBatch(deckId: Long, currentTimestamp: Long, limit: Int = 50): List<CardEntity>

    @Query("SELECT COUNT(*) FROM cards WHERE dueDate <= :currentTimestamp")
    fun countDueCards(currentTimestamp: Long): Flow<Int>

    @Query("""
        SELECT 
            COUNT(*) as totalCards,
            SUM(CASE WHEN dueDate <= :currentTimestamp THEN 1 ELSE 0 END) as dueCards,
            SUM(CASE WHEN `interval` = 0 THEN 1 ELSE 0 END) as learningCards,
            SUM(CASE WHEN `interval` > 0 AND `interval` < 21 THEN 1 ELSE 0 END) as youngCards,
            SUM(CASE WHEN `interval` >= 21 THEN 1 ELSE 0 END) as matureCards
        FROM cards
    """)
    fun getRetentionOverview(currentTimestamp: Long): Flow<RetentionOverview>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: CardEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>): List<Long>

    @Update
    suspend fun updateCard(card: CardEntity)

    @Delete
    suspend fun deleteCard(card: CardEntity)

    @Query("DELETE FROM cards WHERE id = :cardId")
    suspend fun deleteCardById(cardId: Long)

    @Query("SELECT * FROM cards ORDER BY createdAt DESC")
    suspend fun getAllCardsList(): List<CardEntity>
}
