package com.glebestraikh.leetcodereminder.ui.problem

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebestraikh.leetcodereminder.Graph
import com.glebestraikh.leetcodereminder.data.problems.ProblemRepository
import com.glebestraikh.leetcodereminder.data.progress.ProgressRepository
import com.glebestraikh.leetcodereminder.domain.PuzzleChecker
import com.glebestraikh.leetcodereminder.domain.SolutionSplitter
import com.glebestraikh.leetcodereminder.domain.hints
import com.glebestraikh.leetcodereminder.model.Hint
import com.glebestraikh.leetcodereminder.model.HintType
import com.glebestraikh.leetcodereminder.model.Problem
import com.glebestraikh.leetcodereminder.model.ProgressInfo
import com.glebestraikh.leetcodereminder.model.Puzzle
import com.glebestraikh.leetcodereminder.model.PuzzleBlock
import com.glebestraikh.leetcodereminder.model.Rating
import kotlin.random.Random
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json

@Immutable
sealed interface ProblemUiState {
    data object Loading : ProblemUiState

    data object NotFound : ProblemUiState

    data object Error : ProblemUiState

    data class Ready(
        val problem: Problem,
        val progress: ProgressInfo,
        val hints: List<Hint>,
        val revealedHints: Set<HintType>,
        val puzzle: Puzzle,
        val slots: List<PuzzleBlock?>,
        val availableBlocks: List<PuzzleBlock>,
        val targetSlot: Int?,
        val wrongSlots: Set<Int>,
        val mistakes: Int,
        val isSolved: Boolean,
        val savedRating: Rating?,
        val isSavingRating: Boolean = false,
        val ratingSaveFailed: Boolean = false,
    ) : ProblemUiState {
        val filledSlotCount: Int get() = slots.count { it != null }

        val canCheck: Boolean get() = filledSlotCount == slots.size && !isSolved
    }
}

class ProblemDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val problemNumber: Int,
    problemRepository: ProblemRepository = Graph.problemRepository,
    private val progressRepository: ProgressRepository = Graph.progressRepository,
    random: Random = Random.Default,
) : ViewModel() {

    private val session = MutableStateFlow(
        savedStateHandle.get<String>(KEY_SESSION)?.let { Json.decodeFromString<Session>(it) }
            ?: Session(seed = random.nextLong()),
    )

    private val problemWithPuzzle = problemRepository.getProblem(problemNumber).map { problem ->
        if (problem == null) return@map null
        val puzzle = SolutionSplitter.createPuzzle(problem.solution, Random(session.value.seed))
        if (session.value.slots.size != puzzle.slotAnswers.size) {
            updateSession { Session(seed = it.seed, slots = List(puzzle.slotAnswers.size) { null }) }
        }
        problem to puzzle
    }

    val state: StateFlow<ProblemUiState> = combine(
        problemWithPuzzle,
        progressRepository.getProgress(problemNumber),
        session,
    ) { loaded, progress, session ->
        if (loaded == null) return@combine ProblemUiState.NotFound
        val (problem, puzzle) = loaded
        if (session.slots.size != puzzle.slotAnswers.size) return@combine ProblemUiState.Loading
        val hints = problem.hints()
        ProblemUiState.Ready(
            problem = problem,
            progress = progress ?: ProgressInfo(problemNumber = problemNumber),
            hints = hints,
            revealedHints = if (session.isSolved) hints.map { it.type }.toSet() else session.revealedHints,
            puzzle = puzzle,
            slots = session.slots.map { index -> index?.let { puzzle.blocks[it] } },
            availableBlocks = puzzle.blocks.filterNot { it.index in session.slots },
            targetSlot = session.targetSlot,
            wrongSlots = session.wrongSlots,
            mistakes = session.mistakes,
            isSolved = session.isSolved,
            savedRating = session.savedRating,
            isSavingRating = session.isSavingRating,
            ratingSaveFailed = session.ratingSaveFailed,
        )
    }.catch { emit(ProblemUiState.Error) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProblemUiState.Loading,
    )

    fun onHintClick(type: HintType) = updateSession { it.copy(revealedHints = it.revealedHints + type) }

    fun onAvailableBlockClick(block: PuzzleBlock) = updateSession { current ->
        val target = current.targetSlot
        if (current.isSolved || target == null) {
            current
        } else {
            current.copy(
                slots = current.slots.replaced(target, block.index),
                selectedSlot = null,
                wrongSlots = emptySet(),
            )
        }
    }

    fun onSlotClick(slot: Int) = updateSession { current ->
        when {
            current.isSolved -> current
            current.slots[slot] != null -> current.copy(
                slots = current.slots.replaced(slot, null),
                selectedSlot = slot,
                wrongSlots = emptySet(),
            )
            else -> current.copy(selectedSlot = slot)
        }
    }

    fun onReset() = updateSession { current ->
        if (current.isSolved) {
            current
        } else {
            current.copy(
                slots = current.slots.map { null },
                selectedSlot = null,
                wrongSlots = emptySet()
            )
        }
    }

    fun onCheck() {
        val puzzle = (state.value as? ProblemUiState.Ready)?.puzzle ?: return
        updateSession { current ->
            if (current.isSolved) return@updateSession current
            val wrongSlots = PuzzleChecker.findWrongSlots(puzzle, current.slots)
            if (wrongSlots.isEmpty()) {
                current.copy(isSolved = true, wrongSlots = emptySet())
            } else {
                current.copy(mistakes = current.mistakes + 1, wrongSlots = wrongSlots)
            }
        }
    }

    fun onRatingSelected(rating: Rating) {
        val current = session.value
        if (!current.isSolved || current.savedRating != null || current.isSavingRating) return
        updateSession { it.copy(isSavingRating = true, ratingSaveFailed = false) }
        viewModelScope.launch {
            try {
                progressRepository.saveRating(problemNumber, rating)
                updateSession { it.copy(savedRating = rating) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                updateSession { it.copy(ratingSaveFailed = true) }
            } finally {
                updateSession { it.copy(isSavingRating = false) }
            }
        }
    }

    private fun updateSession(transform: (Session) -> Session) {
        val updated = transform(session.value)
        session.value = updated
        savedStateHandle[KEY_SESSION] = Json.encodeToString(updated)
    }

    @Serializable
    private data class Session(
        val seed: Long,
        val slots: List<Int?> = emptyList(),
        val selectedSlot: Int? = null,
        val wrongSlots: Set<Int> = emptySet(),
        val mistakes: Int = 0,
        val revealedHints: Set<HintType> = emptySet(),
        val isSolved: Boolean = false,
        val savedRating: Rating? = null,
        @Transient val isSavingRating: Boolean = false,
        @Transient val ratingSaveFailed: Boolean = false,
    ) {
        val targetSlot: Int?
            get() = selectedSlot?.takeIf { it in slots.indices && slots[it] == null }
                ?: slots.indexOfFirst { it == null }.takeIf { it >= 0 }
    }

    private companion object {
        const val KEY_SESSION = "problem_session"
    }
}

private fun <T> List<T>.replaced(index: Int, value: T): List<T> =
    toMutableList().also { it[index] = value }
