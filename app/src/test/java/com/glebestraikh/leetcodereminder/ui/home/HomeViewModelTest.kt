package com.glebestraikh.leetcodereminder.ui.home

import com.glebestraikh.leetcodereminder.data.repository.ProblemStore
import com.glebestraikh.leetcodereminder.model.Problem
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProblemStore
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProgressStore
import com.glebestraikh.leetcodereminder.domain.ProblemTopicsUseCase
import com.glebestraikh.leetcodereminder.testing.MainDispatcherRule
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
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
        val store = object : ProblemStore by TestProblemStore() {
            override fun getProblemsSortedByTopicAndDifficulty() = flow { emit(catalog.await()) }
        }
        val viewModel = HomeViewModel(ProblemTopicsUseCase(store, TestProgressStore()))
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }

        assertTrue(viewModel.state.value.isLoading)

        catalog.completeExceptionally(IllegalStateException("Invalid catalog"))

        assertFalse(viewModel.state.value.isLoading)
        assertNotNull(viewModel.state.value.errorMessage)
    }
}
