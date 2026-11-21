package com.glebestraikh.leetcodereminder

import android.content.Context
import androidx.room.Room
import com.glebestraikh.leetcodereminder.data.database.LeetCodeReminderDatabase
import com.glebestraikh.leetcodereminder.data.repository.LocalProgressStore
import com.glebestraikh.leetcodereminder.data.repository.ProblemStore
import com.glebestraikh.leetcodereminder.data.repository.ProgressStore
import com.glebestraikh.leetcodereminder.data.sync.AssetProblemStore
import com.glebestraikh.leetcodereminder.domain.ProblemTopicsUseCase
import com.glebestraikh.leetcodereminder.domain.SaveRatingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * A very simple global singleton dependency graph.
 *
 * For a real app, you would use something like Hilt/Dagger instead.
 */
object Graph {
    lateinit var database: LeetCodeReminderDatabase
        private set

    lateinit var problemStore: ProblemStore
        private set

    val progressStore: ProgressStore by lazy {
        LocalProgressStore(progressDao = database.progressDao())
    }

    val problemTopicsUseCase by lazy {
        ProblemTopicsUseCase(problemStore = problemStore, progressStore = progressStore)
    }

    val saveRatingUseCase by lazy {
        SaveRatingUseCase(progressStore = progressStore)
    }

    fun provide(context: Context, applicationScope: CoroutineScope) {
        database = Room.databaseBuilder(context, LeetCodeReminderDatabase::class.java, "leetcode_reminder.db")
            .build()
        problemStore = AssetProblemStore(context.assets, applicationScope, Dispatchers.IO)
    }
}
