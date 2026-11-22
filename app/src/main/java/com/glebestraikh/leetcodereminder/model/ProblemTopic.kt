package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProblemTopic(
    val name: String,
    val problems: List<ProblemWithProgress>,
)
