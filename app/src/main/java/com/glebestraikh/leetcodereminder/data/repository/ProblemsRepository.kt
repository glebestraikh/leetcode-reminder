package com.glebestraikh.leetcodereminder.data.repository

import com.glebestraikh.leetcodereminder.data.content.ProblemsFetcher
import com.glebestraikh.leetcodereminder.data.database.dao.TransactionRunner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

class ProblemsRepository(
    private val problemsFetcher: ProblemsFetcher,
    private val problemStore: ProblemStore,
    private val contentVersionStore: ContentVersionStore,
    private val transactionRunner: TransactionRunner,
    mainDispatcher: CoroutineDispatcher,
) {
    private var refreshingJob: Deferred<Unit>? = null

    private val scope = CoroutineScope(SupervisorJob() + mainDispatcher)

    suspend fun updateProblems(force: Boolean) {
        val current = refreshingJob
        if (current?.isActive == true) {
            current.await()
            return
        }
        val deferred = scope.async {
            val version = problemsFetcher.fetchContentVersion()
            if (!force && version <= contentVersionStore.getContentVersion()) return@async

            val problems = problemsFetcher.fetchProblems()
            transactionRunner {
                problemStore.saveProblems(problems)
                problemStore.deleteProblemsExcept(problems.map { it.id })
                contentVersionStore.saveContentVersion(version)
            }
        }
        refreshingJob = deferred
        deferred.await()
    }
}
