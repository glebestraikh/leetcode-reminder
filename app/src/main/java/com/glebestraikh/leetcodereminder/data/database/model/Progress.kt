package com.glebestraikh.leetcodereminder.data.database.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

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

enum class PerceivedDifficulty {
    EASY,
    MEDIUM,
    HARD,
}

@Immutable
data class ProblemToProgress(
    @Embedded
    val problem: Problem,
    @Relation(parentColumn = "id", entityColumn = "problem_id")
    val progress: Progress?,
)
