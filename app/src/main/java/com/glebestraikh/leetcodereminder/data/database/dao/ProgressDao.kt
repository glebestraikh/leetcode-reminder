package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.glebestraikh.leetcodereminder.data.database.model.Progress
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProgressDao : BaseDao<Progress> {
    @Query("SELECT * FROM progress WHERE problem_id = :problemId")
    abstract fun getProgress(problemId: String): Flow<Progress?>
}
