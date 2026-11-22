package com.glebestraikh.leetcodereminder.ui.problem

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.glebestraikh.leetcodereminder.R
import com.glebestraikh.leetcodereminder.domain.SolutionSplitter
import com.glebestraikh.leetcodereminder.domain.hints
import com.glebestraikh.leetcodereminder.model.Hint
import com.glebestraikh.leetcodereminder.model.HintType
import com.glebestraikh.leetcodereminder.model.PuzzleBlock
import com.glebestraikh.leetcodereminder.model.Rating
import com.glebestraikh.leetcodereminder.ui.preview.PreviewProblems
import com.glebestraikh.leetcodereminder.ui.preview.PreviewProgress
import com.glebestraikh.leetcodereminder.ui.shared.AppTopAppBar
import com.glebestraikh.leetcodereminder.ui.shared.CodeText
import com.glebestraikh.leetcodereminder.ui.shared.DifficultyLabel
import com.glebestraikh.leetcodereminder.ui.shared.Loading
import com.glebestraikh.leetcodereminder.ui.shared.labelRes
import com.glebestraikh.leetcodereminder.ui.theme.LeetCodeReminderTheme
import kotlin.random.Random

@Composable
fun ProblemDetailsScreen(
    viewModel: ProblemDetailsViewModel,
    navigateBackToHome: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProblemDetailsScreen(
        uiState = state,
        onHintClick = viewModel::onHintClick,
        onAvailableBlockClick = viewModel::onAvailableBlockClick,
        onSlotClick = viewModel::onSlotClick,
        onCheck = viewModel::onCheck,
        onReset = viewModel::onReset,
        onRatingSelected = viewModel::onRatingSelected,
        navigateBackToHome = navigateBackToHome,
        navigateBack = navigateBack,
        modifier = modifier,
    )
}

@Composable
private fun ProblemDetailsScreen(
    uiState: ProblemUiState,
    onHintClick: (HintType) -> Unit,
    onAvailableBlockClick: (PuzzleBlock) -> Unit,
    onSlotClick: (Int) -> Unit,
    onCheck: () -> Unit,
    onReset: () -> Unit,
    onRatingSelected: (Rating) -> Unit,
    navigateBackToHome: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = (uiState as? ProblemUiState.Ready)?.problem
        ?.let { "${it.number}. ${it.title}" }
        ?: stringResource(R.string.problem_title)
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = title,
                onBack = navigateBack,
                actions = {
                    if (uiState is ProblemUiState.Ready && uiState.mistakes > 0) {
                        Text(
                            text = stringResource(R.string.solve_mistakes, uiState.mistakes),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(end = 16.dp),
                        )
                    }
                },
            )
        },
        bottomBar = {
            if (uiState is ProblemUiState.Ready) {
                SolveBottomPanel(
                    uiState = uiState,
                    onCheck = onCheck,
                    onReset = onReset,
                    onRatingSelected = onRatingSelected,
                    onContinue = navigateBackToHome,
                )
            }
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            when (uiState) {
                ProblemUiState.Loading -> Loading(modifier = Modifier.fillMaxSize())
                ProblemUiState.NotFound -> Text(
                    text = stringResource(R.string.problem_not_found),
                    modifier = Modifier.align(Alignment.Center),
                )
                ProblemUiState.Error -> Text(
                    text = stringResource(R.string.an_error_has_occurred),
                    modifier = Modifier.align(Alignment.Center),
                )
                is ProblemUiState.Ready -> ProblemDetailsContent(
                    uiState = uiState,
                    onHintClick = onHintClick,
                    onAvailableBlockClick = onAvailableBlockClick,
                    onSlotClick = onSlotClick,
                )
            }
        }
    }
}

@Composable
private fun ProblemDetailsContent(
    uiState: ProblemUiState.Ready,
    onHintClick: (HintType) -> Unit,
    onAvailableBlockClick: (PuzzleBlock) -> Unit,
    onSlotClick: (Int) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProblemHeader(uiState = uiState)
        Text(text = uiState.problem.description, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = stringResource(R.string.problem_pattern, uiState.problem.pattern),
            style = MaterialTheme.typography.titleSmall,
        )
        OutlinedButton(
            onClick = { uriHandler.openUri(uiState.problem.leetCodeUrl) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = stringResource(R.string.problem_action_open_leetcode))
        }

        Text(text = stringResource(R.string.problem_hints_title),
            style = MaterialTheme.typography.titleMedium,
            )
        uiState.hints.forEach { hint ->
            HintCard(
                hint = hint,
                isRevealed = hint.type in uiState.revealedHints,
                onReveal = { onHintClick(hint.type) },
            )
        }

        HorizontalDivider()
        SolutionBuilder(
            uiState = uiState,
            onAvailableBlockClick = onAvailableBlockClick,
            onSlotClick = onSlotClick,
        )
    }
}

@Composable
private fun ProblemHeader(uiState: ProblemUiState.Ready) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DifficultyLabel(difficulty = uiState.problem.difficulty)
        val progress = uiState.progress
        if (progress.isSolved) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = pluralStringResource(R.plurals.problem_solved_count, progress.solvedCount, progress.solvedCount) +
                    (progress.lastRating?.let { " · " + stringResource(it.labelRes) } ?: ""),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HintCard(
    hint: Hint,
    isRevealed: Boolean,
    onReveal: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isRevealed, onClick = onReveal)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = stringResource(hint.type.titleRes),
                style = MaterialTheme.typography.titleSmall,
            )
            when {
                isRevealed && hint.type == HintType.SOLUTION -> CodeText(text = hint.text)
                isRevealed -> Text(text = hint.text, style = MaterialTheme.typography.bodyMedium)
                else -> Text(
                    text = stringResource(R.string.hint_reveal),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@get:StringRes
private val HintType.titleRes: Int
    get() = when (this) {
        HintType.COMPLEXITY -> R.string.hint_complexity
        HintType.IDEA -> R.string.hint_idea
        HintType.SOLUTION -> R.string.hint_solution
    }

private fun previewAttempt(): ProblemUiState.Ready {
    val problem = PreviewProblems.first()
    val puzzle = SolutionSplitter.createPuzzle(problem.solution, Random(1))
    val firstBlock = puzzle.blocks.first { it.text == puzzle.slotAnswers.first() }
    return ProblemUiState.Ready(
        problem = problem,
        progress = PreviewProgress.first(),
        hints = problem.hints(),
        revealedHints = setOf(HintType.IDEA),
        puzzle = puzzle,
        slots = puzzle.slotAnswers.indices.map { if (it == 0) firstBlock else null },
        availableBlocks = puzzle.blocks.filterNot { it == firstBlock },
        targetSlot = 1,
        wrongSlots = emptySet(),
        mistakes = 0,
        isSolved = false,
        savedRating = null,
    )
}

@Preview(name = "Attempt in progress")
@Composable
private fun ProblemDetailsScreenPreview() = PreviewDetails(previewAttempt())

@Composable
private fun PreviewDetails(state: ProblemUiState) {
    LeetCodeReminderTheme {
        ProblemDetailsScreen(
            uiState = state,
            onHintClick = {},
            onAvailableBlockClick = {},
            onSlotClick = {},
            onCheck = {},
            onReset = {},
            onRatingSelected = {},
            navigateBackToHome = {},
            navigateBack = {},
        )
    }
}

@Preview(name = "Hidden and revealed hints")
@Composable
private fun HintCardsPreview() {
    LeetCodeReminderTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PreviewProblems.first().hints().forEach { hint ->
                HintCard(hint = hint, isRevealed = hint.type == HintType.IDEA, onReveal = {})
            }
        }
    }
}

@Preview(name = "Problem · Loading")
@Composable
private fun ProblemLoadingPreview() = PreviewDetails(ProblemUiState.Loading)

@Preview(name = "Problem · NotFound")
@Composable
private fun ProblemNotFoundPreview() = PreviewDetails(ProblemUiState.NotFound)

@Preview(name = "Problem · Error")
@Composable
private fun ProblemErrorPreview() = PreviewDetails(ProblemUiState.Error)

@Composable
private fun PreviewPanel(state: ProblemUiState.Ready) {
    LeetCodeReminderTheme {
        SolveBottomPanel(
            uiState = state,
            onCheck = {},
            onReset = {},
            onRatingSelected = {},
            onContinue = {},
        )
    }
}

private fun filledAttempt(): ProblemUiState.Ready {
    val attempt = previewAttempt()
    val remaining = attempt.puzzle.blocks.toMutableList()
    val slots = attempt.puzzle.slotAnswers.map { answer ->
        remaining.removeAt(remaining.indexOfFirst { it.text == answer })
    }
    return attempt.copy(slots = slots, availableBlocks = emptyList(), targetSlot = null)
}

@Preview(name = "Check enabled")
@Composable
private fun CheckEnabledPreview() = PreviewPanel(filledAttempt())

@Preview(name = "Incorrect blocks")
@Composable
private fun IncorrectBlocksPreview() {
    val attempt = filledAttempt()
    val slots = attempt.slots.toMutableList()
    val other = slots.indexOfFirst { it?.text != slots.first()?.text }
    java.util.Collections.swap(slots, 0, other)
    PreviewPanel(attempt.copy(slots = slots, mistakes = 1, wrongSlots = setOf(0, other)))
}

@Preview(name = "Choose rating")
@Composable
private fun ChooseRatingPreview() = PreviewPanel(filledAttempt().copy(isSolved = true))

@Preview(name = "Saving rating")
@Composable
private fun SavingRatingPreview() =
    PreviewPanel(filledAttempt().copy(isSolved = true, isSavingRating = true))

@Preview(name = "Rating save failed")
@Composable
private fun RatingSaveFailedPreview() =
    PreviewPanel(filledAttempt().copy(isSolved = true, ratingSaveFailed = true))

@Preview(name = "Continue after rating")
@Composable
private fun SavedRatingPreview() =
    PreviewPanel(filledAttempt().copy(isSolved = true, savedRating = Rating.REMEMBER_WELL))
