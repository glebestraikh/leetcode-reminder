package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProblemToProgressInfo(
    val problem: ProblemInfo,
    val progress: ProgressInfo,
)

/**
 * If the problem has never been solved, there is no progress, so an empty one is returned.
 */
fun Problem.asProblemToProgressInfo(progress: ProgressInfo?): ProblemToProgressInfo = ProblemToProgressInfo(
    problem = asExternalModel(),
    progress = progress ?: ProgressInfo(problemNumber = number),
)
