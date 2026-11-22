package com.glebestraikh.leetcodereminder.data.problems

import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.model.Problem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

interface ProblemRepository {
    fun getProblem(problemNumber: Int): Flow<Problem?>

    fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>>
}

class AssetProblemRepository(
    private val listAssets: (String) -> Array<String>?,
    private val readAsset: (String) -> String,
    applicationScope: CoroutineScope,
    ioDispatcher: CoroutineDispatcher,
) : ProblemRepository {
    private val catalog = applicationScope.async(ioDispatcher) { readProblems() }

    override fun getProblem(problemNumber: Int): Flow<Problem?> = flow {
        emit(catalog.await().firstOrNull { it.number == problemNumber })
    }

    override fun getProblemsSortedByTopicAndDifficulty(): Flow<List<Problem>> = flow {
        emit(catalog.await())
    }

    private fun readProblems(): List<Problem> {
        val topics = readAsset("$PROBLEMS_DIR/$TOPICS_FILE").lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        val problems = listAssets(PROBLEMS_DIR).orEmpty()
            .filter { it.endsWith(JSON_EXTENSION) }
            .map { fileName ->
                decodeProblem(
                    slug = fileName.removeSuffix(JSON_EXTENSION),
                    json = readAsset("$PROBLEMS_DIR/$fileName"),
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

    private companion object {
        const val PROBLEMS_DIR = "problems"
        const val JSON_EXTENSION = ".json"
        const val TOPICS_FILE = "topics.txt"
    }
}

@Serializable
private class ProblemJson(
    val number: Int,
    val title: String,
    val difficulty: Difficulty,
    val topic: String,
    val pattern: String,
    val timeComplexity: String,
    val spaceComplexity: String,
    val description: String,
    val idea: String,
    val solution: String,
)

private fun decodeProblem(slug: String, json: String, topics: List<String>): Problem {
    val file = try {
        Json.decodeFromString<ProblemJson>(json)
    } catch (e: SerializationException) {
        throw IllegalArgumentException("$slug.json: invalid problem data", e)
    }
    val topicOrder = topics.indexOf(file.topic)
    require(topicOrder >= 0) { "$slug.json: topic '${file.topic}' is not listed in topics.txt" }

    return Problem(
        number = file.number,
        slug = slug,
        title = file.title,
        difficulty = file.difficulty,
        topic = file.topic,
        topicOrder = topicOrder,
        pattern = file.pattern,
        description = file.description,
        timeComplexity = file.timeComplexity,
        spaceComplexity = file.spaceComplexity,
        idea = file.idea,
        solution = file.solution,
    )
}
