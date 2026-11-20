package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

/**
 * A line of the solution: the code without leading whitespace and its nesting level.
 */
@Immutable
data class CodeLine(
    val code: String,
    val indent: Int = 0,
)
