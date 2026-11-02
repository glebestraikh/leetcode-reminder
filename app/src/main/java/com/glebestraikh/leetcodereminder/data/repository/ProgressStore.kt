package com.glebestraikh.leetcodereminder.data.repository

import com.glebestraikh.leetcodereminder.data.database.dao.ProgressDao
import com.glebestraikh.leetcodereminder.data.database.model.Progress
import kotlinx.coroutines.flow.Flow

interface ProgressStore {
    fun getProgress(problemId: String): Flow<Progress?>

    suspend fun saveProgress(progress: Progress)
}

class LocalProgressStore(private val progressDao: ProgressDao) : ProgressStore {
    override fun getProgress(problemId: String): Flow<Progress?> {
        return progressDao.getProgress(problemId)
    }

    override suspend fun saveProgress(progress: Progress) {
        progressDao.upsert(progress)
    }
}
