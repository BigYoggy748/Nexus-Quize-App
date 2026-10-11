package com.nexus.quiz.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nexus.quiz.data.local.entity.ReviewLogEntity
import kotlinx.coroutines.flow.Flow

data class DailyReviewCount(
    val dayTimestamp: Long,
    val count: Int
)

data class ReviewAccuracyStats(
    val totalReviews: Int,
    val againCount: Int,
    val hardCount: Int,
    val goodCount: Int,
    val easyCount: Int
)

@Dao
interface ReviewLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ReviewLogEntity): Long

    @Query("SELECT * FROM review_logs WHERE cardId = :cardId ORDER BY reviewTimestamp DESC")
    fun getLogsForCard(cardId: Long): Flow<List<ReviewLogEntity>>

    @Query("SELECT * FROM review_logs ORDER BY reviewTimestamp DESC LIMIT :limit")
    fun getRecentLogs(limit: Int = 100): Flow<List<ReviewLogEntity>>

    @Query("""
        SELECT 
            COUNT(*) as totalReviews,
            SUM(CASE WHEN rating = 'AGAIN' THEN 1 ELSE 0 END) as againCount,
            SUM(CASE WHEN rating = 'HARD' THEN 1 ELSE 0 END) as hardCount,
            SUM(CASE WHEN rating = 'GOOD' THEN 1 ELSE 0 END) as goodCount,
            SUM(CASE WHEN rating = 'EASY' THEN 1 ELSE 0 END) as easyCount
        FROM review_logs
    """)
    fun getAccuracyStats(): Flow<ReviewAccuracyStats>

    @Query("""
        SELECT COUNT(*) FROM review_logs 
        WHERE reviewTimestamp >= :sinceTimestamp
    """)
    fun countReviewsSince(sinceTimestamp: Long): Flow<Int>
}
