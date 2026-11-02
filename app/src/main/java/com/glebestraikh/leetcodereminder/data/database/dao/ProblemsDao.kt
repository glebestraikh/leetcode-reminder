package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import com.glebestraikh.leetcodereminder.data.database.model.ProblemToProgress
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProblemsDao : BaseDao<Problem> {
    @Query("SELECT * FROM problems WHERE id = :id")
    abstract fun problemWithId(id: String): Flow<Problem?>

    @Transaction
    @Query("SELECT * FROM problems WHERE id = :id")
    abstract fun problemAndProgress(id: String): Flow<ProblemToProgress?>

    @Transaction
    @Query("SELECT * FROM problems ORDER BY number")
    abstract fun problemsSortedByNumber(): Flow<List<ProblemToProgress>>

    @Query("DELETE FROM problems WHERE id NOT IN (:ids)")
    abstract suspend fun deleteAllExcept(ids: List<String>)
}
