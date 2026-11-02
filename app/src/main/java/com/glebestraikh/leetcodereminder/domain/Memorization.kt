package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.domain.model.PerceivedDifficulty
import com.glebestraikh.leetcodereminder.domain.model.Progress
import com.glebestraikh.leetcodereminder.domain.model.Progress.Companion.MAX_MEMORIZATION_LEVEL
import com.glebestraikh.leetcodereminder.domain.model.Recall
import java.util.concurrent.TimeUnit

object Memorization {
    private val reviewIntervalsDays = listOf(0L, 1L, 3L, 7L, 14L, 30L)

    fun review(
        progress: Progress,
        recall: Recall,
        perceivedDifficulty: PerceivedDifficulty,
        nowMillis: Long,
    ): Progress {
        val levelChange = when (recall) {
            Recall.REMEMBERED -> 1
            Recall.PARTIALLY -> 0
            Recall.FORGOT -> -2
        }
        return progress.copy(
            repetitions = progress.repetitions + 1,
            memorizationLevel = (progress.memorizationLevel + levelChange)
                .coerceIn(0, MAX_MEMORIZATION_LEVEL),
            perceivedDifficulty = perceivedDifficulty,
            lastReviewedAtMillis = nowMillis,
        )
    }

    fun isDueForReview(progress: Progress, nowMillis: Long): Boolean {
        val lastReviewedAt = progress.lastReviewedAtMillis ?: return false
        val intervalMillis = TimeUnit.DAYS.toMillis(
            reviewIntervalsDays[progress.memorizationLevel.coerceIn(0, MAX_MEMORIZATION_LEVEL)],
        )
        return nowMillis - lastReviewedAt >= intervalMillis
    }
}
