package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

@Immutable
data class Problem(
    val number: Int,
    val slug: String,
    val title: String,
    val difficulty: Difficulty,
    val topic: String,
    val topicOrder: Int,
    val pattern: String,
    val description: String,
    val timeComplexity: String,
    val spaceComplexity: String,
    val idea: String,
    val solution: String,
) {
    val leetCodeUrl: String
        get() = "https://leetcode.com/problems/$slug/"
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}
