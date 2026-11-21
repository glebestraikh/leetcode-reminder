package com.glebestraikh.leetcodereminder.ui.problem

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebestraikh.leetcodereminder.Graph
import com.glebestraikh.leetcodereminder.data.database.model.Rating
import com.glebestraikh.leetcodereminder.data.repository.ProblemStore
import com.glebestraikh.leetcodereminder.data.repository.ProgressStore
import com.glebestraikh.leetcodereminder.domain.PuzzleChecker
import com.glebestraikh.leetcodereminder.domain.SaveRatingUseCase
import com.glebestraikh.leetcodereminder.domain.SolutionSplitter
import com.glebestraikh.leetcodereminder.domain.hints
import com.glebestraikh.leetcodereminder.model.Hint
import com.glebestraikh.leetcodereminder.model.HintType
import com.glebestraikh.leetcodereminder.model.ProblemInfo
import com.glebestraikh.leetcodereminder.model.ProgressInfo
import com.glebestraikh.leetcodereminder.model.Puzzle
import com.glebestraikh.leetcodereminder.model.PuzzleBlock
import com.glebestraikh.leetcodereminder.model.asExternalModel
import kotlin.random.Random
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
        val problem: ProblemInfo,
        val progress: ProgressInfo,
        val hints: List<Hint>,
        /** Hints the user opened. Any hint can be opened at any time. After a solve all are open. */
        val revealedHints: Set<HintType>,
        val puzzle: Puzzle,
        /** The block in each slot, null for an empty slot. */
        val slots: List<PuzzleBlock?>,
        /** Blocks that are not placed yet, in shuffled order. */
        val availableBlocks: List<PuzzleBlock>,
        /** The slot the next tapped block goes to. Null when all slots are filled. */
        val targetSlot: Int?,
        /** Slots marked wrong by the last check. Cleared on the next move. */
        val wrongSlots: Set<Int>,
        /** How many times the check found a mistake in this attempt. */
        val mistakes: Int,
        val isSolved: Boolean,
        /** The rating the user chose after the solve. Null until it is chosen and saved. */
        val savedRating: Rating?,
        val isSavingRating: Boolean = false,
        val ratingSaveFailed: Boolean = false,
    ) : ProblemUiState {
        val filledSlotCount: Int get() = slots.count { it != null }

        /** Checking makes sense only when all slots are filled. */
        val canCheck: Boolean get() = filledSlotCount == slots.size && !isSolved
    }
}

/**
 * ViewModel that handles the business logic and screen state of the Problem details screen:
 * the problem, its hints and building the solution from blocks.
 *
 * The attempt ([Session]) lives only while the screen is in the back stack. It is kept in
 * [SavedStateHandle], so it survives a screen rotation and the system killing the process.
 * Leaving the screen with Back removes it from the stack together with this ViewModel,
 * so coming back starts a new attempt from scratch.
 *
 * Nothing is written to the database until the user rates a solved problem.
 */
class ProblemDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val problemNumber: Int,
    problemStore: ProblemStore = Graph.problemStore,
    progressStore: ProgressStore = Graph.progressStore,
    private val saveRatingUseCase: SaveRatingUseCase = Graph.saveRatingUseCase,
    random: Random = Random.Default,
) : ViewModel() {

    private val session = MutableStateFlow(
        savedStateHandle.get<String>(KEY_SESSION)?.let { Json.decodeFromString<Session>(it) }
            ?: Session(seed = random.nextLong()),
    )

    private val problemFlow: Flow<ProblemInfo?> = problemStore.getProblem(problemNumber).map { it?.asExternalModel() }

    /** The blocks are shuffled with the seed from the session, so they keep their order after a restore. */
    private val puzzleFlow: Flow<Puzzle?> = problemFlow.map { problem ->
        problem?.let { SolutionSplitter.createPuzzle(it.solution, Random(session.value.seed)) }
    }

    /** The current puzzle, for the actions. Null until it is built. */
    private var puzzle: Puzzle? = null

    val state: StateFlow<ProblemUiState> = combine(
        problemFlow,
        puzzleFlow,
        progressStore.getProgress(problemNumber),
        session,
    ) { problem, puzzle, progress, session ->
        when {
            problem == null || puzzle == null -> ProblemUiState.NotFound
            // The session is not created yet or is left over from an old version of the problem.
            session.slots.size != puzzle.slotAnswers.size -> ProblemUiState.Loading
            else -> {
                val hints = problem.hints()
                ProblemUiState.Ready(
                    problem = problem,
                    progress = progress?.asExternalModel() ?: ProgressInfo(problemNumber = problemNumber),
                    hints = hints,
                    revealedHints = if (session.isSolved) hints.map { it.type }.toSet() else session.revealedHints,
                    puzzle = puzzle,
                    slots = session.slots.map { id -> id?.let { puzzle.blocks[it] } },
                    availableBlocks = puzzle.blocks.filterNot { it.id in session.slots },
                    targetSlot = session.targetSlot,
                    wrongSlots = session.wrongSlots,
                    mistakes = session.mistakes,
                    isSolved = session.isSolved,
                    savedRating = session.savedRating,
                    isSavingRating = session.isSavingRating,
                    ratingSaveFailed = session.ratingSaveFailed,
                )
            }
        }
    }.catch { emit(ProblemUiState.Error) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProblemUiState.Loading,
    )

    init {
        viewModelScope.launch {
            val puzzle = puzzleFlow.catch { emit(null) }.first() ?: return@launch
            this@ProblemDetailsViewModel.puzzle = puzzle
            // On the first opening all slots are empty. After a restore the slots are already
            // in the session. If the problem changed after a content update, start from scratch.
            if (session.value.slots.size != puzzle.slotAnswers.size) {
                updateSession { Session(seed = it.seed, slots = List(puzzle.slotAnswers.size) { null }) }
            }
        }
    }

    fun onHintClick(type: HintType) = updateSession { it.copy(revealedHints = it.revealedHints + type) }

    /** Puts the block into the target slot: the selected one or the first empty one. */
    fun onAvailableBlockClick(block: PuzzleBlock) = updateSession { current ->
        val target = current.targetSlot
        if (current.isSolved || target == null) {
            current
        } else {
            current.copy(slots = current.slots.replaced(target, block.id), selectedSlot = null, wrongSlots = emptySet())
        }
    }

    /**
     * A filled slot gives its block back and becomes the target. An empty slot just becomes
     * the target, so the next block goes exactly there.
     */
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

    /** Takes all blocks back. Mistakes and opened hints stay until the user leaves the screen. */
    fun onReset() = updateSession { current ->
        if (current.isSolved) {
            current
        } else {
            current.copy(slots = current.slots.map { null }, selectedSlot = null, wrongSlots = emptySet())
        }
    }

    fun onCheck() {
        val puzzle = puzzle ?: return
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

    /** Saves the solve with the rating. A solve is rated once: after that the choice is locked. */
    fun onRatingSelected(rating: Rating) {
        val current = session.value
        if (!current.isSolved || current.savedRating != null || current.isSavingRating) return
        updateSession { it.copy(isSavingRating = true, ratingSaveFailed = false) }
        viewModelScope.launch {
            try {
                saveRatingUseCase(problemNumber = problemNumber, rating = rating)
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

    /**
     * Everything that has to be restored after process death. It is stored in
     * [SavedStateHandle] as a single JSON string: a Bundle cannot store
     * arbitrary classes, but it can store a string.
     */
    @Serializable
    private data class Session(
        /** The blocks are shuffled with this seed. */
        val seed: Long,
        /** Block id in each slot, null for an empty slot. */
        val slots: List<Int?> = emptyList(),
        /** The slot the user tapped to fill next. */
        val selectedSlot: Int? = null,
        val wrongSlots: Set<Int> = emptySet(),
        val mistakes: Int = 0,
        val revealedHints: Set<HintType> = emptySet(),
        val isSolved: Boolean = false,
        val savedRating: Rating? = null,
        @Transient val isSavingRating: Boolean = false,
        @Transient val ratingSaveFailed: Boolean = false,
    ) {
        /** The selected slot if it is still empty, otherwise the first empty slot. */
        val targetSlot: Int?
            get() = selectedSlot?.takeIf { it in slots.indices && slots[it] == null }
                ?: slots.indexOfFirst { it == null }.takeIf { it >= 0 }
    }

    private companion object {
        const val KEY_SESSION = "problem_session"
    }
}

/** A copy of the list with the element at [index] replaced. */
private fun <T> List<T>.replaced(index: Int, value: T): List<T> =
    toMutableList().also { it[index] = value }
