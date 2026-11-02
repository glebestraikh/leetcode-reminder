package com.glebestraikh.leetcodereminder.data.repository

import com.glebestraikh.leetcodereminder.data.database.dao.ProblemsDao
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import com.glebestraikh.leetcodereminder.data.database.model.ProblemToProgress
import kotlinx.coroutines.flow.Flow

interface ProblemStore {
    fun getProblem(problemId: String): Flow<Problem?>

    fun getProblemToProgress(problemId: String): Flow<ProblemToProgress?>

    fun getProblemsSortedByNumber(): Flow<List<ProblemToProgress>>

    suspend fun saveProblems(problems: Collection<Problem>)

    suspend fun deleteProblemsExcept(problemIds: List<String>)
}

class LocalProblemStore(private val problemsDao: ProblemsDao) : ProblemStore {
    override fun getProblem(problemId: String): Flow<Problem?> {
        return problemsDao.getProblem(problemId)
    }

    override fun getProblemToProgress(problemId: String): Flow<ProblemToProgress?> =
        problemsDao.getProblemToProgress(problemId)

    override fun getProblemsSortedByNumber(): Flow<List<ProblemToProgress>> {
        return problemsDao.getProblemsSortedByNumber()
    }

    override suspend fun saveProblems(problems: Collection<Problem>) = problemsDao.upsertAll(problems)

    override suspend fun deleteProblemsExcept(problemIds: List<String>) {
        problemsDao.deleteAllExcept(problemIds)
    }
}
