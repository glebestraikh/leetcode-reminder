package com.glebestraikh.leetcodereminder.data.sync

import android.content.res.AssetManager
import com.glebestraikh.leetcodereminder.data.repository.ProblemStore
import com.glebestraikh.leetcodereminder.model.Problem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AssetProblemStore(
    private val assets: AssetManager,
    applicationScope: CoroutineScope,
    ioDispatcher: CoroutineDispatcher,
) : ProblemStore {
    // Share one parse across all screens for the lifetime of the process.
    private val catalog = applicationScope.async(ioDispatcher) { readProblems() }

    override fun getProblem(problemNumber: Int): Flow<Problem?> = flow {
        emit(catalog.await().firstOrNull { it.number == problemNumber })
    }

    override fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>> = flow {
        emit(catalog.await())
    }

    private fun readProblems(): List<Problem> {
        val topics = ProblemMarkdownParser.parseTopics(readAsset("$PROBLEMS_DIR/$TOPICS_FILE"))
        val problems = assets.list(PROBLEMS_DIR).orEmpty()
            .filter { it.endsWith(MARKDOWN_EXTENSION) }
            .map { fileName ->
                ProblemMarkdownParser.parse(
                    slug = fileName.removeSuffix(MARKDOWN_EXTENSION),
                    markdown = readAsset("$PROBLEMS_DIR/$fileName"),
                    topics = topics,
                )
            }

        val duplicates = problems.groupBy { it.number }.filterValues { it.size > 1 }
        check(duplicates.isEmpty()) {
            "Duplicate problem numbers: " +
                duplicates.entries.joinToString { (number, sameNumber) -> "$number in ${sameNumber.map { it.slug }}" }
        }
        return problems.sortedWith(compareBy({ it.topicOrder }, { it.difficulty }, { it.number }))
    }

    private fun readAsset(path: String): String = assets.open(path).bufferedReader().use { it.readText() }

    private companion object {
        const val PROBLEMS_DIR = "problems"
        const val MARKDOWN_EXTENSION = ".md"
        const val TOPICS_FILE = "topics.txt"
    }
}
