package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.data.problems.ProblemRepository
import com.glebestraikh.leetcodereminder.data.progress.ProgressRepository
import com.glebestraikh.leetcodereminder.model.ProblemTopic
import com.glebestraikh.leetcodereminder.model.asProblemToProgressInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * A use case which returns the problems for the home screen split into topics.
 *
 * The problem repository already returns problems sorted by topic, then from easy to hard, so here they are
 * only grouped. groupBy keeps the order in which the keys first appear, so the topics stay in order.
 */
class ProblemTopicsUseCase(
    private val problemRepository: ProblemRepository,
    private val progressRepository: ProgressRepository,
) {
    operator fun invoke(): Flow<List<ProblemTopic>> =
        combine(
            problemRepository.getProblemsSortedByTopicAndDifficulty(),
            progressRepository.getAllProgress(),
        ) { problems, progress ->
            // A problem that was never solved has no row in progress, so the display model uses empty progress.
            val progressByNumber = progress.associateBy { it.problemNumber }
            problems
                .map { it.asProblemToProgressInfo(progressByNumber[it.number]) }
                .groupBy { it.problem.topic }
                .map { (topic, problemsToProgress) -> ProblemTopic(name = topic, problems = problemsToProgress) }
        }
}
