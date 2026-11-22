package com.glebestraikh.leetcodereminder.ui.problem

import androidx.lifecycle.SavedStateHandle
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProblemRepository
import com.glebestraikh.leetcodereminder.data.testing.repository.TestProgressRepository
import com.glebestraikh.leetcodereminder.domain.testProblem
import com.glebestraikh.leetcodereminder.model.HintType
import com.glebestraikh.leetcodereminder.model.Rating
import com.glebestraikh.leetcodereminder.testing.MainDispatcherRule
import kotlin.random.Random
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProblemDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val problem = testProblem(number = 1, solution = "func f() int {\n\ta := 1\n\tb := 2\n\treturn a + b\n}")
    private val problemRepository = TestProblemRepository().apply { setProblems(listOf(problem)) }
    private val progressRepository = TestProgressRepository()

    private fun createViewModel(savedStateHandle: SavedStateHandle = SavedStateHandle()) = ProblemDetailsViewModel(
        savedStateHandle = savedStateHandle,
        problemNumber = problem.number,
        problemRepository = problemRepository,
        progressRepository = progressRepository,
        random = Random(seed = 42),
    )

    /** state is built with stateIn(WhileSubscribed): it does not update without a subscriber. */
    private fun TestScope.subscribe(viewModel: ProblemDetailsViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.state.collect() }
    }

    private val ProblemDetailsViewModel.readyState: ProblemUiState.Ready
        get() = state.value as ProblemUiState.Ready

    /** The correct text for every slot, in order. */
    private val ProblemDetailsViewModel.answers: List<String>
        get() = readyState.puzzle.slotAnswers

    private fun ProblemDetailsViewModel.place(text: String) =
        onAvailableBlockClick(readyState.availableBlocks.first { it.text == text })

    private fun ProblemDetailsViewModel.solve() {
        answers.forEach { place(it) }
        onCheck()
    }

    @Test
    fun whenOpened_allSlotsEmptyAndOnlyNeededBlocks() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)

        val state = viewModel.readyState
        assertTrue(state.slots.all { it == null })
        assertEquals(viewModel.answers.sorted(), state.availableBlocks.map { it.text }.sorted())
        assertEquals(0, state.targetSlot)
        assertFalse(state.canCheck)
    }

    @Test
    fun whenWrongOrder_wrongSlotsMarked_andTakingABlockBackClearsMarks() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)

        (listOf(viewModel.answers[1], viewModel.answers[0]) + viewModel.answers.drop(2)).forEach { viewModel.place(it) }
        viewModel.onCheck()

        assertEquals(setOf(0, 1), viewModel.readyState.wrongSlots)
        assertEquals(1, viewModel.readyState.mistakes)
        assertFalse(viewModel.readyState.isSolved)

        viewModel.onSlotClick(0)
        assertNull(viewModel.readyState.slots[0])
        assertEquals(0, viewModel.readyState.targetSlot)
        assertEquals(emptySet<Int>(), viewModel.readyState.wrongSlots)
    }

    @Test
    fun whenEmptySlotTapped_nextBlockGoesThere() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)
        val last = viewModel.answers.lastIndex

        viewModel.onSlotClick(last)
        assertEquals(last, viewModel.readyState.targetSlot)

        viewModel.place(viewModel.answers[last])
        assertEquals(viewModel.answers[last], viewModel.readyState.slots[last]?.text)
        assertEquals(0, viewModel.readyState.targetSlot)
    }

    @Test
    fun anyHintCanBeOpenedAtAnyTime() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)

        viewModel.onHintClick(HintType.SOLUTION)

        assertEquals(setOf(HintType.SOLUTION), viewModel.readyState.revealedHints)
    }

    @Test
    fun whenSolved_allHintsOpenAndNothingSavedUntilRated() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)

        viewModel.solve()

        assertTrue(viewModel.readyState.isSolved)
        assertEquals(HintType.entries.toSet(), viewModel.readyState.revealedHints)
        assertNull(progressRepository.getProgress(1).first())
    }

    @Test
    fun ratingSavesTheSolveOnce() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)
        viewModel.solve()

        viewModel.onRatingSelected(Rating.NEED_TO_REPEAT)
        viewModel.onRatingSelected(Rating.REMEMBER_WELL)

        assertEquals(Rating.NEED_TO_REPEAT, viewModel.readyState.savedRating)
        val progress = progressRepository.getProgress(1).first()
        assertEquals(1, progress?.solvedCount)
        assertEquals(Rating.NEED_TO_REPEAT, progress?.lastRating)
    }

    @Test
    fun secondSolveIncreasesCountAndSavesLatestRating() = runTest {
        val firstAttempt = createViewModel()
        subscribe(firstAttempt)
        firstAttempt.solve()
        firstAttempt.onRatingSelected(Rating.FORGOT)

        val secondAttempt = createViewModel()
        subscribe(secondAttempt)
        secondAttempt.solve()
        secondAttempt.onRatingSelected(Rating.REMEMBER_WELL)

        val progress = progressRepository.getProgress(1).first()
        assertEquals(2, progress?.solvedCount)
        assertEquals(Rating.REMEMBER_WELL, progress?.lastRating)
    }

    @Test
    fun failedRatingCanBeRetried() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)
        viewModel.solve()
        progressRepository.saveFailure = IllegalStateException("Write failed")

        viewModel.onRatingSelected(Rating.NEED_TO_REPEAT)

        assertNull(viewModel.readyState.savedRating)
        assertTrue(viewModel.readyState.ratingSaveFailed)
        assertFalse(viewModel.readyState.isSavingRating)
        assertNull(progressRepository.getProgress(1).first())

        progressRepository.saveFailure = null
        viewModel.onRatingSelected(Rating.NEED_TO_REPEAT)

        assertEquals(Rating.NEED_TO_REPEAT, viewModel.readyState.savedRating)
        assertFalse(viewModel.readyState.ratingSaveFailed)
        assertEquals(1, progressRepository.getProgress(1).first()?.solvedCount)
    }

    @Test
    fun ratingIsIgnoredBeforeTheSolve() = runTest {
        val viewModel = createViewModel()
        subscribe(viewModel)

        viewModel.onRatingSelected(Rating.REMEMBER_WELL)

        assertNull(progressRepository.getProgress(1).first())
    }

    @Test
    fun whenRecreatedWithTheSameState_attemptRestored() = runTest {
        val savedStateHandle = SavedStateHandle()
        val first = createViewModel(savedStateHandle)
        subscribe(first)
        first.onHintClick(HintType.IDEA)
        first.place(first.answers[0])
        val order = first.readyState.availableBlocks.map { it.index }

        // A new ViewModel with the same SavedStateHandle, as after a rotation or process death.
        val restored = createViewModel(savedStateHandle)
        subscribe(restored)

        assertEquals(first.answers[0], restored.readyState.slots[0]?.text)
        assertEquals(order, restored.readyState.availableBlocks.map { it.index })
        assertEquals(setOf(HintType.IDEA), restored.readyState.revealedHints)
    }

    @Test
    fun whenOpenedAgainAfterBack_attemptStartsFromScratch() = runTest {
        val first = createViewModel()
        subscribe(first)
        first.onHintClick(HintType.IDEA)
        first.place(first.answers[0])

        // Back removes the screen with its state, so the problem opens with a new SavedStateHandle.
        val reopened = createViewModel()
        subscribe(reopened)

        assertTrue(reopened.readyState.slots.all { it == null })
        assertEquals(emptySet<HintType>(), reopened.readyState.revealedHints)
        assertEquals(0, reopened.readyState.mistakes)
    }
}
