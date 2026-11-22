package com.glebestraikh.leetcodereminder.ui.home

import com.glebestraikh.leetcodereminder.data.problems.ProblemRepository
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProblemRepository
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProgressRepository
import com.glebestraikh.leetcodereminder.domain.ProblemCatalog
import com.glebestraikh.leetcodereminder.model.Problem
import com.glebestraikh.leetcodereminder.testing.MainDispatcherRule
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun catalogFailureIsShownAfterLoading() = runTest {
        val catalog = CompletableDeferred<List<Problem>>()
        val repository = object : ProblemRepository by TestProblemRepository() {
            override fun getProblemsSortedByTopicAndDifficulty() = flow { emit(catalog.await()) }
        }
        val viewModel = HomeViewModel(ProblemCatalog(repository, TestProgressRepository()))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }

        assertTrue(viewModel.state.value.isLoading)

        catalog.completeExceptionally(IllegalStateException("Invalid catalog"))

        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.errorMessage)
    }
}
