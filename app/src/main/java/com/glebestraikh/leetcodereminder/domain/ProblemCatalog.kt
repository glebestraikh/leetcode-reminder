package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.data.problems.ProblemRepository
import com.glebestraikh.leetcodereminder.data.progress.ProgressRepository
import com.glebestraikh.leetcodereminder.model.ProblemTopic
import com.glebestraikh.leetcodereminder.model.ProblemWithProgress
import com.glebestraikh.leetcodereminder.model.ProgressInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProblemCatalog(
    private val problemRepository: ProblemRepository,
    private val progressRepository: ProgressRepository,
) {
    fun observeTopics(): Flow<List<ProblemTopic>> =
        combine(
            problemRepository.getProblemsSortedByTopicAndDifficulty(),
            progressRepository.getAllProgress(),
        ) { problems, progress ->
            val progressByNumber = progress.associateBy { it.problemNumber }
            problems.map { problem ->
                    ProblemWithProgress(
                        problem = problem,
                        progress = progressByNumber[problem.number] ?: ProgressInfo(problemNumber = problem.number),
                    )
                }
                .groupBy { it.problem.topic }
                .map { (topic, problemsWithProgress) ->
                    ProblemTopic(name = topic, problems = problemsWithProgress) }
        }
}
