package com.glebestraikh.leetcodereminder.data.repository

import com.glebestraikh.leetcodereminder.data.database.dao.ProgressDao
import com.glebestraikh.leetcodereminder.data.database.model.Progress
import com.glebestraikh.leetcodereminder.data.database.model.Rating
import kotlinx.coroutines.flow.Flow

interface ProgressStore {
    fun getProgress(problemNumber: Int): Flow<Progress?>

    fun getAllProgress(): Flow<List<Progress>>

    suspend fun saveRating(problemNumber: Int, rating: Rating)
}

class LocalProgressStore(private val progressDao: ProgressDao) : ProgressStore {
    override fun getProgress(problemNumber: Int): Flow<Progress?> {
        return progressDao.getProgress(problemNumber)
    }

    override fun getAllProgress(): Flow<List<Progress>> {
        return progressDao.getAllProgress()
    }

    override suspend fun saveRating(problemNumber: Int, rating: Rating) {
        progressDao.saveRating(problemNumber, rating)
    }
}
