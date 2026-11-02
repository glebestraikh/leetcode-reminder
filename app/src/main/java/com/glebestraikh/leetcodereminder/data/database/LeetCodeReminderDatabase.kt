package com.glebestraikh.leetcodereminder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.glebestraikh.leetcodereminder.data.database.dao.ContentVersionDao
import com.glebestraikh.leetcodereminder.data.database.dao.ProblemsDao
import com.glebestraikh.leetcodereminder.data.database.dao.ProgressDao
import com.glebestraikh.leetcodereminder.data.database.dao.TransactionRunnerDao
import com.glebestraikh.leetcodereminder.data.database.model.ContentVersion
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import com.glebestraikh.leetcodereminder.data.database.model.Progress

@Database(
    entities = [
        Problem::class,
        Progress::class,
        ContentVersion::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(ListTypeConverters::class)
abstract class LeetCodeReminderDatabase : RoomDatabase() {
    abstract fun problemsDao(): ProblemsDao
    abstract fun progressDao(): ProgressDao
    abstract fun contentVersionDao(): ContentVersionDao
    abstract fun transactionRunnerDao(): TransactionRunnerDao
}
