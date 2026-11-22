package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.data.progress.ProgressRepository
import com.glebestraikh.leetcodereminder.model.Rating

/** Saves the rating and increments the solved count. */
class SaveRatingUseCase(private val progressRepository: ProgressRepository) {
    suspend operator fun invoke(problemNumber: Int, rating: Rating) {
        progressRepository.saveRating(problemNumber, rating)
    }
}
