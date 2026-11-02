package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Transaction
import com.glebestraikh.leetcodereminder.data.repository.TransactionRunner

@Dao
abstract class TransactionRunnerDao : TransactionRunner {
    @Transaction
    protected open suspend fun runInTransaction(tx: suspend () -> Unit) = tx()

    @Ignore
    override suspend fun invoke(tx: suspend () -> Unit) {
        runInTransaction(tx)
    }
}
