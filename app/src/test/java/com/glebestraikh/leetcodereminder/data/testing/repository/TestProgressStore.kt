package com.glebestraikh.leetcodereminder.data.testing.repository

import com.glebestraikh.leetcodereminder.data.database.model.Progress
import com.glebestraikh.leetcodereminder.data.repository.ProgressStore
import com.glebestraikh.leetcodereminder.data.database.model.Rating
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestProgressStore : ProgressStore {
    private val progressFlow = MutableStateFlow<List<Progress>>(listOf())

    override fun getProgress(problemNumber: Int): Flow<Progress?> = progressFlow.map { progress ->
        progress.find { it.problemNumber == problemNumber }
    }

    override fun getAllProgress(): Flow<List<Progress>> = progressFlow

    var saveFailure: Exception? = null

    override suspend fun saveRating(problemNumber: Int, rating: Rating) {
        saveFailure?.let { throw it }
        progressFlow.update { current ->
            val count = current.find { it.problemNumber == problemNumber }?.solvedCount ?: 0
            val progress = Progress(problemNumber = problemNumber, lastRating = rating, solvedCount = count + 1)
            current.filterNot { it.problemNumber == progress.problemNumber } + progress
        }
    }

    fun setProgress(progress: List<Progress>) {
        progressFlow.value = progress
    }
}
