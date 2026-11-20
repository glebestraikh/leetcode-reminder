package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProblemsDao {
    @Query("SELECT * FROM problems WHERE number = :number")
    abstract fun getProblem(number: Int): Flow<Problem?>

    @Query(
        """
        SELECT * FROM problems
        ORDER BY
            topic_order,
            CASE difficulty WHEN 'EASY' THEN 0 WHEN 'MEDIUM' THEN 1 ELSE 2 END,
            number
        """,
    )
    abstract fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>>

    @Query("SELECT number FROM problems")
    abstract suspend fun getProblemNumbers(): List<Int>

    @Upsert
    abstract suspend fun upsertAll(problems: Collection<Problem>)

    @Query("DELETE FROM problems WHERE number IN (:numbers)")
    abstract suspend fun deleteProblems(numbers: Collection<Int>)
}
