package com.glebestraikh.leetcodereminder.domain.model

data class Hint(
    val type: HintType,
    val text: String,
)

enum class HintType {
    COMPLEXITY,
    PATTERN,
    IDEA,
    KEY_LINE,
    SOLUTION,
}
