package com.glebestraikh.leetcodereminder.data.testing.repository

import com.glebestraikh.leetcodereminder.data.progress.ProgressRepository
import com.glebestraikh.leetcodereminder.model.ProgressInfo
import com.glebestraikh.leetcodereminder.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestProgressRepository : ProgressRepository {
    private val progressFlow = MutableStateFlow<List<ProgressInfo>>(listOf())

    override fun getProgress(problemNumber: Int): Flow<ProgressInfo?> = progressFlow.map { progress ->
        progress.find { it.problemNumber == problemNumber }
    }

    override fun getAllProgress(): Flow<List<ProgressInfo>> = progressFlow

    var saveFailure: Exception? = null

    override suspend fun saveRating(problemNumber: Int, rating: Rating) {
        saveFailure?.let { throw it }
        progressFlow.update { current ->
            val count = current.find { it.problemNumber == problemNumber }?.solvedCount ?: 0
            val progress = ProgressInfo(problemNumber = problemNumber, lastRating = rating, solvedCount = count + 1)
            current.filterNot { it.problemNumber == progress.problemNumber } + progress
        }
    }

    fun setProgress(progress: List<ProgressInfo>) {
        progressFlow.value = progress
    }
}
