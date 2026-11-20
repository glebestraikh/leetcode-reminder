package com.glebestraikh.leetcodereminder.data.database.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "problems")
@Immutable
data class Problem(
    @PrimaryKey @ColumnInfo(name = "number") val number: Int,
    @ColumnInfo(name = "slug") val slug: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "difficulty") val difficulty: Difficulty,
    @ColumnInfo(name = "topic") val topic: String,
    @ColumnInfo(name = "topic_order") val topicOrder: Int,
    @ColumnInfo(name = "pattern") val pattern: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "time_complexity") val timeComplexity: String,
    @ColumnInfo(name = "space_complexity") val spaceComplexity: String,
    @ColumnInfo(name = "idea") val idea: String,
    @ColumnInfo(name = "solution") val solution: String,
)

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}
