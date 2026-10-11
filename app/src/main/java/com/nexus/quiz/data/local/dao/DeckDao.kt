package com.nexus.quiz.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nexus.quiz.data.local.entity.DeckEntity
import kotlinx.coroutines.flow.Flow

data class DeckWithStats(
    val id: Long,
    val name: String,
    val description: String,
    val category: com.nexus.quiz.data.model.DeckCategory,
    val hexColor: String,
    val createdAt: Long,
    val updatedAt: Long,
    val totalCards: Int,
    val dueCards: Int,
    val masteredCards: Int
)

@Dao
interface DeckDao {
    @Query("SELECT * FROM decks ORDER BY updatedAt DESC")
    fun getAllDecks(): Flow<List<DeckEntity>>

    @Query("SELECT * FROM decks WHERE id = :deckId LIMIT 1")
    suspend fun getDeckById(deckId: Long): DeckEntity?

    @Query("SELECT * FROM decks WHERE id = :deckId LIMIT 1")
    fun getDeckByIdFlow(deckId: Long): Flow<DeckEntity?>

    @Query("""
        SELECT 
            d.id,
            d.name,
            d.description,
            d.category,
            d.hexColor,
            d.createdAt,
            d.updatedAt,
            COUNT(c.id) as totalCards,
            SUM(CASE WHEN c.dueDate <= :currentTimestamp THEN 1 ELSE 0 END) as dueCards,
            SUM(CASE WHEN c.interval >= 21 THEN 1 ELSE 0 END) as masteredCards
        FROM decks d
        LEFT JOIN cards c ON d.id = c.deckId
        GROUP BY d.id
        ORDER BY d.updatedAt DESC
    """)
    fun getDecksWithStats(currentTimestamp: Long): Flow<List<DeckWithStats>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeck(deck: DeckEntity): Long

    @Update
    suspend fun updateDeck(deck: DeckEntity)

    @Delete
    suspend fun deleteDeck(deck: DeckEntity)

    @Query("DELETE FROM decks WHERE id = :deckId")
    suspend fun deleteDeckById(deckId: Long)
}
