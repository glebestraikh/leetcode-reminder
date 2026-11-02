package com.glebestraikh.leetcodereminder.domain.model

data class Progress(
    val problemId: String,
    val repetitions: Int = 0,
    val memorizationLevel: Int = 0,
    val perceivedDifficulty: PerceivedDifficulty? = null,
    val lastReviewedAtMillis: Long? = null,
) {
    companion object {
        const val MAX_MEMORIZATION_LEVEL = 5
    }
}

enum class PerceivedDifficulty {
    EASY,
    MEDIUM,
    HARD,
}

enum class Recall {
    REMEMBERED,
    PARTIALLY,
    FORGOT,
}
