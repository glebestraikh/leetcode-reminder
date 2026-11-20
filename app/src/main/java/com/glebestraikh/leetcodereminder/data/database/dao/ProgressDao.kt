package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.glebestraikh.leetcodereminder.data.database.model.Progress
import com.glebestraikh.leetcodereminder.data.database.model.Rating
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProgressDao {
    @Query("SELECT * FROM progress WHERE problem_number = :problemNumber")
    abstract fun getProgress(problemNumber: Int): Flow<Progress?>

    @Query("SELECT * FROM progress")
    abstract fun getAllProgress(): Flow<List<Progress>>

    @Query("SELECT * FROM progress WHERE problem_number = :problemNumber")
    abstract suspend fun readProgress(problemNumber: Int): Progress?

    @Upsert
    abstract suspend fun upsert(progress: Progress)

    @Transaction
    open suspend fun saveRating(problemNumber: Int, rating: Rating) {
        val count = readProgress(problemNumber)?.solvedCount ?: 0
        upsert(Progress(problemNumber = problemNumber, lastRating = rating, solvedCount = count + 1))
    }
}
