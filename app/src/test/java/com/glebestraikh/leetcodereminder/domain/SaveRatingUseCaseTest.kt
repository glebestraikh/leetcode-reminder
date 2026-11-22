package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.data.testing.repository.TestProgressRepository
import com.glebestraikh.leetcodereminder.model.ProgressInfo
import com.glebestraikh.leetcodereminder.model.Rating
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveRatingUseCaseTest {

    private val progressRepository = TestProgressRepository()

    val useCase = SaveRatingUseCase(progressRepository = progressRepository)

    @Test
    fun whenFirstSolve_progressIsSaved() = runTest {
        useCase(problemNumber = 1, rating = Rating.NEED_TO_REPEAT)

        assertEquals(
            ProgressInfo(problemNumber = 1, lastRating = Rating.NEED_TO_REPEAT, solvedCount = 1),
            progressRepository.getProgress(1).first(),
        )
    }

    @Test
    fun whenSolvedAgain_countGrowsAndLastRatingReplaced() = runTest {
        useCase(problemNumber = 1, rating = Rating.FORGOT)
        useCase(problemNumber = 1, rating = Rating.REMEMBER_WELL)

        val progress = progressRepository.getProgress(1).first()
        assertEquals(2, progress?.solvedCount)
        assertEquals(Rating.REMEMBER_WELL, progress?.lastRating)
    }
}
