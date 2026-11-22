package com.glebestraikh.leetcodereminder.data.problems

import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.model.Problem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AssetProblemRepositoryTest {

    private val topics = listOf("Two Pointers", "Arrays & Hashing")

    private val json = """
        {
          "number": 1,
          "title": "Two Sum",
          "difficulty": "EASY",
          "topic": "Arrays & Hashing",
          "pattern": "Hash Map",
          "timeComplexity": "O(n)",
          "spaceComplexity": "O(n)",
          "description": "Find two numbers.\n\nExample 1:\nInput: nums = [2,7], target = 9",
          "idea": "Use a map.",
          "solution": "func twoSum() {\n\tfmt.Println(\"hello\")\n}"
        }
    """.trimIndent()

    @Test
    fun validFilePreservesTextAndUsesTopicOrder() = runTest {
        assertEquals(
            Problem(
                number = 1,
                slug = "two-sum",
                title = "Two Sum",
                difficulty = Difficulty.EASY,
                topic = "Arrays & Hashing",
                topicOrder = 1,
                pattern = "Hash Map",
                description = "Find two numbers.\n\nExample 1:\nInput: nums = [2,7], target = 9",
                timeComplexity = "O(n)",
                spaceComplexity = "O(n)",
                idea = "Use a map.",
                solution = "func twoSum() {\n\tfmt.Println(\"hello\")\n}",
            ),
            repository().getProblem(1).first(),
        )
    }

    @Test
    fun missingRequiredFieldReportsFileName() = runTest {
        val repository = repository(json = json.replace("  \"pattern\": \"Hash Map\",\n", ""))
        val error = runCatching { repository.getProblemsSortedByTopicAndDifficulty().first() }.exceptionOrNull()

        assertTrue(error is IllegalArgumentException)
        assertTrue(error?.message.orEmpty().contains("two-sum.json"))
    }

    @Test
    fun unknownDifficultyIsRejected() = runTest {
        val repository = repository(json = json.replace("EASY", "UNKNOWN"))
        val error = runCatching { repository.getProblemsSortedByTopicAndDifficulty().first() }.exceptionOrNull()

        assertTrue(error is IllegalArgumentException)
    }

    @Test
    fun topicOutsideTopicsFileIsRejected() = runTest {
        val repository = repository(topics = listOf("Graphs"))
        val error = runCatching { repository.getProblemsSortedByTopicAndDifficulty().first() }.exceptionOrNull()

        assertTrue(error is IllegalArgumentException)
        assertTrue(error?.message.orEmpty().contains("Arrays & Hashing"))
    }

    private fun TestScope.repository(
        json: String = this@AssetProblemRepositoryTest.json,
        topics: List<String> = this@AssetProblemRepositoryTest.topics,
    ): ProblemRepository {
        val files = mapOf("topics.txt" to topics.joinToString("\n"), "two-sum.json" to json)
        return AssetProblemRepository(
            listAssets = { directory ->
                assertEquals("problems", directory)
                files.keys.toTypedArray()
            },
            readAsset = { path -> files.getValue(path.removePrefix("problems/")) },
            applicationScope = CoroutineScope(backgroundScope.coroutineContext + SupervisorJob(backgroundScope.coroutineContext.job)),
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )
    }
}
