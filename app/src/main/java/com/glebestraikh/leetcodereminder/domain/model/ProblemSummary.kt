package com.glebestraikh.leetcodereminder.domain.model

data class ProblemSummary(
    val id: String,
    val number: Int,
    val title: String,
    val difficulty: Difficulty,
    val pattern: String,
    val progress: Progress,
)
