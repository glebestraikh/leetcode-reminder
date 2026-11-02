package com.glebestraikh.leetcodereminder.data.database.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

/**
 * Таблица `progress`: прогресс пользователя по задаче. Хранится отдельно от контента задачи:
 * импорт нового контента перезаписывает `problems`, но не трогает эту таблицу.
 * Если задача удалена из problems.json, её прогресс удаляется каскадом.
 */
@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = Problem::class,
            parentColumns = ["id"],
            childColumns = ["problem_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("problem_id", unique = true),
    ],
)
@Immutable
data class Progress(
    @PrimaryKey @ColumnInfo(name = "problem_id") val problemId: String,
    @ColumnInfo(name = "repetitions") val repetitions: Int = 0,
    @ColumnInfo(name = "memorization_level") val memorizationLevel: Int = 0,
    @ColumnInfo(name = "perceived_difficulty") val perceivedDifficulty: PerceivedDifficulty? = null,
    @ColumnInfo(name = "last_reviewed_at_millis") val lastReviewedAtMillis: Long? = null,
)

/**
 * Насколько сложной задача показалась пользователю.
 */
enum class PerceivedDifficulty {
    EASY,
    MEDIUM,
    HARD,
}

/**
 * Не таблица, а результат запроса: задача вместе с прогрессом по ней.
 * Room сам подгружает [progress] по `problem_id`. [progress] равен null,
 * пока задачу ни разу не оценивали.
 */
@Immutable
data class ProblemToProgress(
    @Embedded val problem: Problem,
    @Relation(parentColumn = "id", entityColumn = "problem_id")
    val progress: Progress?,
)
