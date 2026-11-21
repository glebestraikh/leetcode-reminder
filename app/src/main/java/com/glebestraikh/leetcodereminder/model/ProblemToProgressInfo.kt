package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable
import com.glebestraikh.leetcodereminder.data.database.model.Progress

@Immutable
data class ProblemToProgressInfo(
    val problem: ProblemInfo,
    val progress: ProgressInfo,
)

/**
 * If the problem has never been solved, there is no progress, so an empty one is returned.
 */
fun Problem.asProblemToProgressInfo(progress: Progress?): ProblemToProgressInfo = ProblemToProgressInfo(
    problem = asExternalModel(),
    progress = progress?.asExternalModel() ?: ProgressInfo(problemNumber = number),
)
