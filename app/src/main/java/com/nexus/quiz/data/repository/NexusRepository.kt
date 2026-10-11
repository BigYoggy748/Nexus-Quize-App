package com.nexus.quiz.data.repository

import com.nexus.quiz.data.local.NexusDatabase
import com.nexus.quiz.data.local.dao.DeckWithStats
import com.nexus.quiz.data.local.dao.RetentionOverview
import com.nexus.quiz.data.local.dao.ReviewAccuracyStats
import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.local.entity.DeckEntity
import com.nexus.quiz.data.local.entity.ReviewLogEntity
import com.nexus.quiz.data.local.entity.TopicMasteryEntity
import kotlinx.coroutines.flow.Flow

class NexusRepository(private val database: NexusDatabase) {
    private val deckDao = database.deckDao()
    private val cardDao = database.cardDao()
    private val reviewLogDao = database.reviewLogDao()
    private val topicMasteryDao = database.topicMasteryDao()

    // Saved Topics / Explanations Library
    fun getAllSavedTopics(): Flow<List<TopicMasteryEntity>> = topicMasteryDao.getAllSavedTopics()

    suspend fun getSavedTopicById(id: Long): TopicMasteryEntity? = topicMasteryDao.getTopicById(id)

    suspend fun getSavedTopicByTitle(title: String): TopicMasteryEntity? = topicMasteryDao.getTopicByTitle(title)

    suspend fun saveTopicMastery(topic: TopicMasteryEntity): Long = topicMasteryDao.insertTopic(topic)

    suspend fun deleteSavedTopic(topic: TopicMasteryEntity) = topicMasteryDao.deleteTopic(topic)

    suspend fun deleteSavedTopicById(id: Long) = topicMasteryDao.deleteTopicById(id)

    // Decks
    fun getDecksWithStats(): Flow<List<DeckWithStats>> {
        return deckDao.getDecksWithStats(System.currentTimeMillis())
    }

    fun getAllDecks(): Flow<List<DeckEntity>> = deckDao.getAllDecks()

    suspend fun getDeckById(deckId: Long): DeckEntity? = deckDao.getDeckById(deckId)

    fun getDeckByIdFlow(deckId: Long): Flow<DeckEntity?> = deckDao.getDeckByIdFlow(deckId)

    suspend fun insertDeck(deck: DeckEntity): Long = deckDao.insertDeck(deck)

    suspend fun updateDeck(deck: DeckEntity) = deckDao.updateDeck(deck)

    suspend fun deleteDeckById(deckId: Long) = deckDao.deleteDeckById(deckId)

    // Cards
    fun getCardsForDeck(deckId: Long): Flow<List<CardEntity>> = cardDao.getCardsForDeck(deckId)

    fun getAllDueCards(): Flow<List<CardEntity>> {
        return cardDao.getAllDueCards(System.currentTimeMillis())
    }

    fun getDueCardsForDeck(deckId: Long): Flow<List<CardEntity>> {
        return cardDao.getDueCardsForDeck(deckId, System.currentTimeMillis())
    }

    suspend fun getDueCardsBatch(limit: Int = 50): List<CardEntity> {
        return cardDao.getDueCardsBatch(System.currentTimeMillis(), limit)
    }

    suspend fun getDueCardsForDeckBatch(deckId: Long, limit: Int = 50): List<CardEntity> {
        return cardDao.getDueCardsForDeckBatch(deckId, System.currentTimeMillis(), limit)
    }

    fun countDueCards(): Flow<Int> = cardDao.countDueCards(System.currentTimeMillis())

    fun getRetentionOverview(): Flow<RetentionOverview> {
        return cardDao.getRetentionOverview(System.currentTimeMillis())
    }

    suspend fun getCardById(cardId: Long): CardEntity? = cardDao.getCardById(cardId)

    suspend fun insertCard(card: CardEntity): Long = cardDao.insertCard(card)

    suspend fun insertCards(cards: List<CardEntity>): List<Long> = cardDao.insertCards(cards)

    suspend fun updateCard(card: CardEntity) = cardDao.updateCard(card)

    suspend fun deleteCardById(cardId: Long) = cardDao.deleteCardById(cardId)

    suspend fun getAllCards(): List<CardEntity> = cardDao.getAllCardsList()

    // Review Logs
    suspend fun logReview(log: ReviewLogEntity): Long = reviewLogDao.insertLog(log)

    fun getLogsForCard(cardId: Long): Flow<List<ReviewLogEntity>> = reviewLogDao.getLogsForCard(cardId)

    fun getRecentLogs(limit: Int = 100): Flow<List<ReviewLogEntity>> = reviewLogDao.getRecentLogs(limit)

    fun getAccuracyStats(): Flow<ReviewAccuracyStats> = reviewLogDao.getAccuracyStats()

    fun countReviewsToday(): Flow<Int> {
        val startOfDay = getStartOfDayTimestamp()
        return reviewLogDao.countReviewsSince(startOfDay)
    }

    private fun getStartOfDayTimestamp(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
