package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

/**
 * The problems of one topic, a tab on the home screen. Problems go from easy to hard.
 */
@Immutable
data class ProblemTopic(
    val name: String,
    val problems: List<ProblemToProgressInfo>,
)
