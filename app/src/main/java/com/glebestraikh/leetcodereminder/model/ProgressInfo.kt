package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

/**
 * External data layer representation of the user's progress on a problem.
 */
@Immutable
data class ProgressInfo(
    val problemNumber: Int = 0,
    /** How well the user said they remember the problem after the last solve, null if never solved. */
    val lastRating: Rating? = null,
    /** How many times the problem was solved. */
    val solvedCount: Int = 0,
) {
    val isSolved: Boolean get() = solvedCount > 0
}
