package com.nexus.quiz.engine

import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.local.entity.ReviewLogEntity
import com.nexus.quiz.data.model.SrsRating
import kotlin.math.max
import kotlin.math.roundToInt

data class SrsCalculationResult(
    val updatedCard: CardEntity,
    val reviewLog: ReviewLogEntity,
    val nextIntervalDays: Int
)

object SrsScheduler {
    const val DEFAULT_EASE_FACTOR = 2.5f
    const val MINIMUM_EASE_FACTOR = 1.3f
    private const val ONE_DAY_MS = 24 * 60 * 60 * 1000L

    /**
     * Calculates updated card state and review log according to the SM-2 algorithm.
     */
    fun scheduleCard(
        card: CardEntity,
        rating: SrsRating,
        timeSpentMs: Long = 0,
        now: Long = System.currentTimeMillis()
    ): SrsCalculationResult {
        val intervalBefore = card.interval
        val easeFactorBefore = card.easeFactor
        val repsBefore = card.repetitions
        val lapsesBefore = card.lapses

        val newInterval: Int
        val newReps: Int
        val newEaseFactor: Float
        val newLapses: Int

        when (rating) {
            SrsRating.AGAIN -> {
                // Card failed - reset repetition chain and reduce ease factor
                newReps = 0
                newInterval = 1
                newLapses = lapsesBefore + 1
                newEaseFactor = max(MINIMUM_EASE_FACTOR, easeFactorBefore - 0.20f)
            }
            SrsRating.HARD -> {
                // Strained recall - small interval increment, slight ease factor penalty
                newReps = if (repsBefore == 0) 1 else repsBefore
                newInterval = if (intervalBefore <= 1) {
                    2
                } else {
                    max(1, (intervalBefore * 1.2f).roundToInt())
                }
                newLapses = lapsesBefore
                newEaseFactor = max(MINIMUM_EASE_FACTOR, easeFactorBefore - 0.15f)
            }
            SrsRating.GOOD -> {
                // Standard recall - standard SM-2 expansion
                newReps = repsBefore + 1
                newInterval = when (newReps) {
                    1 -> 1
                    2 -> 6
                    else -> max(1, (intervalBefore * easeFactorBefore).roundToInt())
                }
                newLapses = lapsesBefore
                // Standard SM-2 formula for q=4 (equivalent to Good)
                val q = 4
                val efDelta = 0.1f - (5 - q) * (0.08f + (5 - q) * 0.02f)
                newEaseFactor = max(MINIMUM_EASE_FACTOR, easeFactorBefore + efDelta)
            }
            SrsRating.EASY -> {
                // Effortless recall - accelerated interval and bonus ease factor
                newReps = repsBefore + 1
                newInterval = when (newReps) {
                    1 -> 3
                    2 -> 8
                    else -> max(1, (intervalBefore * easeFactorBefore * 1.35f).roundToInt())
                }
                newLapses = lapsesBefore
                newEaseFactor = easeFactorBefore + 0.15f
            }
        }

        val nextDueDate = now + (newInterval * ONE_DAY_MS)

        val updatedCard = card.copy(
            interval = newInterval,
            repetitions = newReps,
            easeFactor = newEaseFactor,
            lapses = newLapses,
            dueDate = nextDueDate,
            lastReviewedDate = now
        )

        val log = ReviewLogEntity(
            cardId = card.id,
            rating = rating,
            reviewTimestamp = now,
            intervalBefore = intervalBefore,
            intervalAfter = newInterval,
            easeFactorBefore = easeFactorBefore,
            easeFactorAfter = newEaseFactor,
            timeSpentMs = timeSpentMs
        )

        return SrsCalculationResult(
            updatedCard = updatedCard,
            reviewLog = log,
            nextIntervalDays = newInterval
        )
    }

    /**
     * Preview helper to show interval string on buttons (e.g., "1d", "6d", "14d")
     */
    fun previewIntervalString(card: CardEntity, rating: SrsRating): String {
        val result = scheduleCard(card, rating)
        return when (val days = result.nextIntervalDays) {
            0 -> "< 1d"
            1 -> "1d"
            else -> "${days}d"
        }
    }
}
