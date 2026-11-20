package com.glebestraikh.leetcodereminder.data.database.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(
            entity = Problem::class,
            parentColumns = ["number"],
            childColumns = ["problem_number"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
@Immutable
data class Progress(
    @PrimaryKey @ColumnInfo(name = "problem_number") val problemNumber: Int,
    @ColumnInfo(name = "last_rating") val lastRating: Rating,
    @ColumnInfo(name = "solved_count") val solvedCount: Int,
)

enum class Rating {
    FORGOT,
    NEED_TO_REPEAT,
    REMEMBER_WELL,
}
