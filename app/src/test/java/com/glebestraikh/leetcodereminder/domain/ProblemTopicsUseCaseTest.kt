package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.data.database.model.Progress
import com.glebestraikh.leetcodereminder.data.database.model.Rating
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProblemStore
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProgressStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ProblemTopicsUseCaseTest {

    private val problemStore = TestProblemStore()
    private val progressStore = TestProgressStore()

    val useCase = ProblemTopicsUseCase(problemStore = problemStore, progressStore = progressStore)

    @Before
    fun setUp() {
        problemStore.setProblems(
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
        val topics = useCase().first()

        assertEquals(listOf("Two Pointers", "Sliding Window"), topics.map { it.name })
    }

    @Test
    fun problemsGoFromEasyToHard() = runTest {
        val problems = useCase().first().first().problems

        assertEquals(listOf(125, 392, 11), problems.map { it.problem.number })
    }

    @Test
    fun solvedProblemsAreMarked() = runTest {
        progressStore.setProgress(listOf(Progress(problemNumber = 11, lastRating = Rating.REMEMBER_WELL, solvedCount = 1)))

        val problems = useCase().first().first().problems

        assertTrue(problems.first { it.problem.number == 11 }.progress.isSolved)
        assertFalse(problems.first { it.problem.number == 125 }.progress.isSolved)
    }
}
