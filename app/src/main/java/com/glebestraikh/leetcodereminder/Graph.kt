package com.glebestraikh.leetcodereminder

import android.content.Context
import androidx.room.Room
import com.glebestraikh.leetcodereminder.data.database.LeetCodeReminderDatabase
import com.glebestraikh.leetcodereminder.data.problems.AssetProblemRepository
import com.glebestraikh.leetcodereminder.data.problems.ProblemRepository
import com.glebestraikh.leetcodereminder.data.progress.ProgressRepository
import com.glebestraikh.leetcodereminder.data.progress.RoomProgressRepository
import com.glebestraikh.leetcodereminder.domain.ProblemCatalog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

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

    fun provide(context: Context, applicationScope: CoroutineScope) {
        database = Room.databaseBuilder(
            context,
            LeetCodeReminderDatabase::class.java,
            "leetcode_reminder.db",
        ).build()

        val assets = context.assets
        problemRepository = AssetProblemRepository(
            listAssets = assets::list,
            readAsset = { path ->
                assets.open(path).bufferedReader().use { it.readText() } },
            applicationScope = applicationScope,
            ioDispatcher = Dispatchers.IO,
        )
    }
}
