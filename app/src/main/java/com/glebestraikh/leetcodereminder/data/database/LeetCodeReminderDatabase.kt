package com.glebestraikh.leetcodereminder.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glebestraikh.leetcodereminder.data.database.dao.ProgressDao
import com.glebestraikh.leetcodereminder.data.database.model.Progress

@Database(entities = [Progress::class], version = 1, exportSchema = true)
abstract class LeetCodeReminderDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
}
