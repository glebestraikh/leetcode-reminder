package com.glebestraikh.leetcodereminder.data.repository

import com.glebestraikh.leetcodereminder.data.database.dao.ProblemsDao
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import kotlinx.coroutines.flow.Flow

interface ProblemStore {
    fun getProblem(problemNumber: Int): Flow<Problem?>

    fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>>
}

class LocalProblemStore(private val problemsDao: ProblemsDao) : ProblemStore {
    override fun getProblem(problemNumber: Int): Flow<Problem?> {
        return problemsDao.getProblem(problemNumber)
    }

    override fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>> {
        return problemsDao.getProblemsSortedByTopicAndDifficulty()
    }
}
