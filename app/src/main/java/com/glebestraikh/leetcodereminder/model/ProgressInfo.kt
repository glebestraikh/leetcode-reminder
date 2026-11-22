package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProgressInfo(
    val problemNumber: Int,
    val lastRating: Rating? = null,
    val solvedCount: Int = 0,
) {
    val isSolved: Boolean get() = solvedCount > 0
}

enum class Rating {
    FORGOT,
    NEED_TO_REPEAT,
    REMEMBER_WELL,
}
