package com.glebestraikh.leetcodereminder

import android.content.Context
import androidx.room.Room
import com.glebestraikh.leetcodereminder.data.database.LeetCodeReminderDatabase
import com.glebestraikh.leetcodereminder.data.problems.AssetProblemRepository
import com.glebestraikh.leetcodereminder.data.problems.ProblemRepository
import com.glebestraikh.leetcodereminder.data.progress.ProgressRepository
import com.glebestraikh.leetcodereminder.data.progress.RoomProgressRepository
import com.glebestraikh.leetcodereminder.domain.ProblemCatalog
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

    lateinit var problemRepository: ProblemRepository
        private set

    val progressRepository: ProgressRepository by lazy {
        RoomProgressRepository(progressDao = database.progressDao())
    }

    val problemCatalog by lazy {
        ProblemCatalog(problemRepository = problemRepository, progressRepository = progressRepository)
    }

    val saveRatingUseCase by lazy {
        SaveRatingUseCase(progressRepository = progressRepository)
    }

    fun provide(context: Context, applicationScope: CoroutineScope) {
        database = Room.databaseBuilder(context, LeetCodeReminderDatabase::class.java, "leetcode_reminder.db")
            .build()
        val assets = context.assets
        problemRepository = AssetProblemRepository(
            listAssets = assets::list,
            readAsset = { path -> assets.open(path).bufferedReader().use { it.readText() } },
            applicationScope = applicationScope,
            ioDispatcher = Dispatchers.IO,
        )
    }
}
