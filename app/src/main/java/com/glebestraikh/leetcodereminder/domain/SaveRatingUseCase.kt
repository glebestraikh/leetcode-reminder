package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.data.database.model.Rating
import com.glebestraikh.leetcodereminder.data.repository.ProgressStore

/** Saves the rating and increments the solved count. */
class SaveRatingUseCase(private val progressStore: ProgressStore) {
    suspend operator fun invoke(problemNumber: Int, rating: Rating) {
        progressStore.saveRating(problemNumber, rating)
    }
}
