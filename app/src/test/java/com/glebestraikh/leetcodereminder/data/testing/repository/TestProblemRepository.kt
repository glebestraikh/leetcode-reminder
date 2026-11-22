package com.glebestraikh.leetcodereminder.data.testing.repository

import com.glebestraikh.leetcodereminder.data.problems.ProblemRepository
import com.glebestraikh.leetcodereminder.model.Problem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class TestProblemRepository : ProblemRepository {
    private val problemsFlow = MutableStateFlow<List<Problem>>(listOf())

    override fun getProblem(problemNumber: Int): Flow<Problem?> = problemsFlow.map { problems ->
        problems.find { it.number == problemNumber }
    }

    /** The same order as the SQL query: topic, then difficulty, then number. */
    override fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>> = problemsFlow.map { problems ->
        problems.sortedWith(compareBy({ it.topicOrder }, { it.difficulty }, { it.number }))
    }

    fun setProblems(problems: List<Problem>) {
        problemsFlow.value = problems
    }
}
