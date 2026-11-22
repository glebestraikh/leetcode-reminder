package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.data.testing.repository.TestProblemRepository
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProgressRepository
import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.model.ProgressInfo
import com.glebestraikh.leetcodereminder.model.Rating
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProblemCatalogTest {

    private val problemRepository = TestProblemRepository()
    private val progressRepository = TestProgressRepository()

    val catalog = ProblemCatalog(problemRepository = problemRepository, progressRepository = progressRepository)

    @Before
    fun setUp() {
        problemRepository.setProblems(
            listOf(
                testProblem(number = 3, difficulty = Difficulty.MEDIUM, topic = "Sliding Window", topicOrder = 2),
                testProblem(number = 125, difficulty = Difficulty.EASY, topic = "Two Pointers", topicOrder = 1),
                testProblem(number = 11, difficulty = Difficulty.MEDIUM, topic = "Two Pointers", topicOrder = 1),
                testProblem(number = 392, difficulty = Difficulty.EASY, topic = "Two Pointers", topicOrder = 1),
            ),
        )
    }

    @Test
    fun topicsAreOrderedFromEasyToHard() = runTest {
        val topics = catalog.observeTopics().first()

        assertEquals(listOf("Two Pointers", "Sliding Window"), topics.map { it.name })
    }

    @Test
    fun problemsGoFromEasyToHard() = runTest {
        val problems = catalog.observeTopics().first().first().problems

        assertEquals(listOf(125, 392, 11), problems.map { it.problem.number })
    }

    @Test
    fun solvedProblemsAreMarked() = runTest {
        progressRepository.setProgress(listOf(ProgressInfo(problemNumber = 11, lastRating = Rating.REMEMBER_WELL, solvedCount = 1)))

        val problems = catalog.observeTopics().first().first().problems

        assertTrue(problems.first { it.problem.number == 11 }.progress.isSolved)
        assertFalse(problems.first { it.problem.number == 125 }.progress.isSolved)
    }
}
