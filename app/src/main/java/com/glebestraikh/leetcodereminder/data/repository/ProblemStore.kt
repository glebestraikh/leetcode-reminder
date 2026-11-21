package com.glebestraikh.leetcodereminder.data.repository

import com.glebestraikh.leetcodereminder.model.Problem
import kotlinx.coroutines.flow.Flow

interface ProblemStore {
    fun getProblem(problemNumber: Int): Flow<Problem?>

    fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>>
}
