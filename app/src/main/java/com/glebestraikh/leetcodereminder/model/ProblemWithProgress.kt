package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProblemWithProgress(
    val problem: Problem,
    val progress: ProgressInfo,
)
