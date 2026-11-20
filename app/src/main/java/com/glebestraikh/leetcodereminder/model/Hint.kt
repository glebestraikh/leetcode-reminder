package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

@Immutable
data class Hint(
    val type: HintType,
    val text: String,
)

enum class HintType {
    COMPLEXITY,
    IDEA,
    SOLUTION,
}
