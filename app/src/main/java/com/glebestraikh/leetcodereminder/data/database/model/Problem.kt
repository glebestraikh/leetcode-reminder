package com.glebestraikh.leetcodereminder.data.database.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "problems")
@Immutable
data class Problem(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "number") val number: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "difficulty") val difficulty: Difficulty,
    @ColumnInfo(name = "pattern") val pattern: String,
    @ColumnInfo(name = "statement") val statement: String,
    @ColumnInfo(name = "examples") val examples: List<Example>,
    @ColumnInfo(name = "constraints") val constraints: List<String>,
    @ColumnInfo(name = "time_complexity") val timeComplexity: String,
    @ColumnInfo(name = "space_complexity") val spaceComplexity: String,
    @ColumnInfo(name = "idea") val idea: String,
    @ColumnInfo(name = "solution") val solution: List<CodeLine>,
)

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}

@Serializable
@Immutable
data class Example(
    val input: String,
    val output: String,
    val explanation: String? = null,
)

@Serializable
@Immutable
data class CodeLine(
    val code: String,
    val indent: Int = 0,
)
