package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.domain.model.PerceivedDifficulty
import com.glebestraikh.leetcodereminder.domain.model.Progress
import com.glebestraikh.leetcodereminder.domain.model.Progress.Companion.MAX_MEMORIZATION_LEVEL
import com.glebestraikh.leetcodereminder.domain.model.Recall
import java.util.concurrent.TimeUnit

/**
 * Правила индикатора запоминания — упрощённое интервальное повторение.
 *
 * - «Помню» поднимает уровень на 1, «частично» оставляет как есть,
 *   «не помню» опускает на 2.
 * - Чем выше уровень, тем дольше можно не повторять задачу (см. [reviewIntervalsDays]).
 */
object Memorization {

    /** Через сколько дней задачу стоит повторить на каждом уровне от 0 до 5. */
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

    /** Пора ли повторить задачу. Задачи, которые ни разу не решали, сюда не попадают. */
    fun isDueForReview(progress: Progress, nowMillis: Long): Boolean {
        val lastReviewedAt = progress.lastReviewedAtMillis ?: return false
        val intervalMillis = TimeUnit.DAYS.toMillis(
            reviewIntervalsDays[progress.memorizationLevel.coerceIn(0, MAX_MEMORIZATION_LEVEL)],
        )
        return nowMillis - lastReviewedAt >= intervalMillis
    }
}
