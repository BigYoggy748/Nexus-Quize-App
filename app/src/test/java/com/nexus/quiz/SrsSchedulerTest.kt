package com.nexus.quiz

import com.nexus.quiz.data.local.entity.CardEntity
import com.nexus.quiz.data.model.CardType
import com.nexus.quiz.data.model.SrsRating
import com.nexus.quiz.engine.SrsScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SrsSchedulerTest {

    private fun createSampleCard(
        interval: Int = 0,
        repetitions: Int = 0,
        easeFactor: Float = 2.5f,
        lapses: Int = 0
    ): CardEntity {
        return CardEntity(
            id = 1L,
            deckId = 10L,
            type = CardType.CLOZE_FORMULA,
            prompt = "Bernoulli equation dynamic pressure term:",
            answer = "\\frac{1}{2}\\rho v^2",
            interval = interval,
            repetitions = repetitions,
            easeFactor = easeFactor,
            lapses = lapses
        )
    }

    @Test
    fun testFirstReviewGoodRating() {
        val card = createSampleCard()
        val result = SrsScheduler.scheduleCard(card, SrsRating.GOOD)

        assertEquals(1, result.updatedCard.interval)
        assertEquals(1, result.updatedCard.repetitions)
        assertEquals(0, result.updatedCard.lapses)
        assertTrue(result.updatedCard.easeFactor >= 2.5f)
    }

    @Test
    fun testSecondReviewGoodRating() {
        val card = createSampleCard(interval = 1, repetitions = 1)
        val result = SrsScheduler.scheduleCard(card, SrsRating.GOOD)

        assertEquals(6, result.updatedCard.interval)
        assertEquals(2, result.updatedCard.repetitions)
    }

    @Test
    fun testThirdReviewGoodRatingScalesByEaseFactor() {
        val card = createSampleCard(interval = 6, repetitions = 2, easeFactor = 2.5f)
        val result = SrsScheduler.scheduleCard(card, SrsRating.GOOD)

        assertEquals(15, result.updatedCard.interval) // 6 * 2.5 = 15
        assertEquals(3, result.updatedCard.repetitions)
    }

    @Test
    fun testAgainRatingResetsRepetitionsAndIncrementsLapses() {
        val card = createSampleCard(interval = 15, repetitions = 3, easeFactor = 2.5f, lapses = 0)
        val result = SrsScheduler.scheduleCard(card, SrsRating.AGAIN)

        assertEquals(1, result.updatedCard.interval)
        assertEquals(0, result.updatedCard.repetitions)
        assertEquals(1, result.updatedCard.lapses)
        assertEquals(2.30f, result.updatedCard.easeFactor, 0.01f)
    }

    @Test
    fun testEasyRatingAcceleratesInterval() {
        val card = createSampleCard(interval = 0, repetitions = 0)
        val result = SrsScheduler.scheduleCard(card, SrsRating.EASY)

        assertEquals(3, result.updatedCard.interval)
        assertEquals(1, result.updatedCard.repetitions)
        assertEquals(2.65f, result.updatedCard.easeFactor, 0.01f)
    }

    @Test
    fun testEaseFactorNeverDropsBelowMinimum() {
        var card = createSampleCard(easeFactor = 1.35f)
        val result = SrsScheduler.scheduleCard(card, SrsRating.AGAIN)

        assertEquals(SrsScheduler.MINIMUM_EASE_FACTOR, result.updatedCard.easeFactor)
    }
}
