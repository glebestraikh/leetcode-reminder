package com.glebestraikh.leetcodereminder.data.content

import android.content.res.AssetManager
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private const val ProblemsAssetFile = "problems.json"

private const val ProblemsVersionAssetFile = "problems_version.txt"

class ProblemsFetcher(
    private val assetManager: AssetManager,
    private val ioDispatcher: CoroutineDispatcher,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun fetchContentVersion(): Long {
        return withContext(ioDispatcher) {
            assetManager.open(ProblemsVersionAssetFile).bufferedReader().use { reader ->
                reader.readText().trim().toLong()
            }
        }
    }

    suspend fun fetchProblems(): List<Problem> {
        return withContext(ioDispatcher) {
            assetManager.open(ProblemsAssetFile).bufferedReader().use { reader ->
                json.decodeFromString<ProblemsJson>(reader.readText()).toProblems()
            }
        }
    }
}
