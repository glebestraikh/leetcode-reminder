package com.glebestraikh.leetcodereminder.data.progress

import com.glebestraikh.leetcodereminder.data.database.dao.ProgressDao
import com.glebestraikh.leetcodereminder.data.database.model.Progress
import com.glebestraikh.leetcodereminder.model.ProgressInfo
import com.glebestraikh.leetcodereminder.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProgressRepository {
    fun getProgress(problemNumber: Int): Flow<ProgressInfo?>

    fun getAllProgress(): Flow<List<ProgressInfo>>

    suspend fun saveRating(problemNumber: Int, rating: Rating)
}

class RoomProgressRepository(private val progressDao: ProgressDao) : ProgressRepository {
    override fun getProgress(problemNumber: Int): Flow<ProgressInfo?> =
        progressDao.getProgress(problemNumber).map { progress -> progress?.toProgressInfo() }

    override fun getAllProgress(): Flow<List<ProgressInfo>> =
        progressDao.getAllProgress().map { rows -> rows.map { it.toProgressInfo() } }

    override suspend fun saveRating(problemNumber: Int, rating: Rating) {
        progressDao.saveRating(problemNumber, rating)
    }
}

private fun Progress.toProgressInfo(): ProgressInfo = ProgressInfo(
    problemNumber = problemNumber,
    lastRating = lastRating,
    solvedCount = solvedCount,
)
