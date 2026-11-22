package com.glebestraikh.leetcodereminder.data.database.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glebestraikh.leetcodereminder.model.Rating

@Entity(tableName = "progress")
@Immutable
data class Progress(
    @PrimaryKey @ColumnInfo(name = "problem_number") val problemNumber: Int,
    @ColumnInfo(name = "last_rating") val lastRating: Rating,
    @ColumnInfo(name = "solved_count") val solvedCount: Int,
)
