package com.glebestraikh.leetcodereminder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glebestraikh.leetcodereminder.data.database.dao.ContentInfoDao
import com.glebestraikh.leetcodereminder.data.database.dao.ProblemsDao
import com.glebestraikh.leetcodereminder.data.database.dao.ProgressDao
import com.glebestraikh.leetcodereminder.data.database.model.ContentInfo
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import com.glebestraikh.leetcodereminder.data.database.model.Progress

@Database(
    entities = [
        Problem::class,
        Progress::class,
        ContentInfo::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class LeetCodeReminderDatabase : RoomDatabase() {
    abstract fun problemsDao(): ProblemsDao

    abstract fun progressDao(): ProgressDao

    abstract fun contentInfoDao(): ContentInfoDao
}
