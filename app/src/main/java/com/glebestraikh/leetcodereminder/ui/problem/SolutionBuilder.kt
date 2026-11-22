package com.glebestraikh.leetcodereminder.ui.problem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glebestraikh.leetcodereminder.R
import com.glebestraikh.leetcodereminder.model.PuzzleBlock
import com.glebestraikh.leetcodereminder.model.PuzzleLine
import com.glebestraikh.leetcodereminder.model.PuzzlePart
import com.glebestraikh.leetcodereminder.model.Rating
import com.glebestraikh.leetcodereminder.ui.shared.CodeText
import com.glebestraikh.leetcodereminder.ui.shared.labelRes
import com.glebestraikh.leetcodereminder.ui.theme.LeetCodeReminderTheme

@Composable
fun SolutionBuilder(
    uiState: ProblemUiState.Ready,
    onAvailableBlockClick: (PuzzleBlock) -> Unit,
    onSlotClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = stringResource(R.string.solve_title),
            style = MaterialTheme.typography.titleMedium,
        )
        LinearProgressIndicator(
            progress = { uiState.filledSlotCount.toFloat() / uiState.slots.size.coerceAtLeast(1) },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)),
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.puzzle.lines.forEach { line ->
                CodeLineRow(line = line, uiState = uiState, onSlotClick = onSlotClick)
            }
        }
        if (uiState.availableBlocks.isNotEmpty()) {
            Text(text = stringResource(R.string.solve_section_blocks),
                style = MaterialTheme.typography.titleSmall,
            )
            BlockBank(blocks = uiState.availableBlocks,
                onBlockClick = onAvailableBlockClick,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CodeLineRow(
    line: PuzzleLine,
    uiState: ProblemUiState.Ready,
    onSlotClick: (Int) -> Unit,
) {
    FlowRow(
        modifier = Modifier.padding(start = (line.indent * 16).dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        line.parts.forEach { part ->
            when (part) {
                is PuzzlePart.Fixed -> CodeText(
                    text = part.text,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
                is PuzzlePart.Slot -> {
                    val block = uiState.slots[part.index]
                    if (block == null) {
                        EmptySlot(
                            isTarget = part.index == uiState.targetSlot,
                            onClick = { onSlotClick(part.index) },
                        )
                    } else {
                        ChunkyBlock(
                            text = block.text,
                            style = when {
                                uiState.isSolved -> BlockStyle.Correct
                                part.index in uiState.wrongSlots -> BlockStyle.Wrong
                                else -> BlockStyle.Normal
                            },
                            onClick = { onSlotClick(part.index) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySlot(
    isTarget: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(12.dp)
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = 40.dp)
            .clip(shape)
            .background(if (isTarget) colors.primary.copy(alpha = 0.08f) else Color.Transparent)
            .border(width = 2.dp, color = if (isTarget) colors.primary else colors.outlineVariant, shape = shape)
            .clickable(onClick = onClick),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BlockBank(
    blocks: List<PuzzleBlock>,
    onBlockClick: (PuzzleBlock) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        blocks.forEach { block ->
            ChunkyBlock(
                text = block.text,
                style = BlockStyle.Normal,
                onClick = { onBlockClick(block) },
            )
        }
    }
}

private enum class BlockStyle { Normal, Wrong, Correct }

@Composable
private fun ChunkyBlock(
    text: String,
    style: BlockStyle,
    onClick: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val (container, content, edge) = when (style) {
        BlockStyle.Normal -> Triple(colors.surface, colors.onSurface, colors.outlineVariant)
        BlockStyle.Wrong -> Triple(colors.errorContainer, colors.onErrorContainer, colors.error)
        BlockStyle.Correct -> Triple(colors.primaryContainer, colors.onPrimaryContainer, colors.primary)
    }
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(edge)
            .padding(bottom = 3.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(container)
                .border(width = 2.dp, color = edge, shape = shape)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            CodeText(text = text, color = content)
        }
    }
}

@Composable
fun SolveBottomPanel(
    uiState: ProblemUiState.Ready,
    onCheck: () -> Unit,
    onReset: () -> Unit,
    onRatingSelected: (Rating) -> Unit,
    onContinue: () -> Unit,
) {
    val colors = MaterialTheme.colorScheme
    val panelColor = when {
        uiState.isSolved -> colors.primaryContainer
        uiState.wrongSlots.isNotEmpty() -> colors.errorContainer
        else -> colors.surface
    }
    Surface(color = panelColor, shadowElevation = 3.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (uiState.isSolved) {
                Text(
                    text = stringResource(R.string.solve_status_correct),
                    style = MaterialTheme.typography.titleLarge,
                    color = colors.onPrimaryContainer,
                )
                Text(
                    text = stringResource(R.string.rating_question),
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onPrimaryContainer,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Rating.entries.forEach { rating ->
                        FilterChip(
                            selected = rating == uiState.savedRating,
                            onClick = { onRatingSelected(rating) },
                            enabled = !uiState.isSavingRating && (uiState.savedRating == null || rating == uiState.savedRating),
                            label = { Text(text = stringResource(rating.labelRes)) },
                        )
                    }
                }
                if (uiState.ratingSaveFailed) {
                    Text(
                        text = stringResource(R.string.rating_save_failed),
                        color = colors.error,
                    )
                }
                Button(
                    onClick = onContinue,
                    enabled = uiState.savedRating != null,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = stringResource(R.string.solve_action_continue))
                }
            } else {
                if (uiState.wrongSlots.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.solve_status_wrong, uiState.wrongSlots.size),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colors.onErrorContainer,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onReset,
                        enabled = uiState.filledSlotCount > 0,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(R.string.solve_action_reset))
                    }
                    Button(
                        onClick = onCheck,
                        enabled = uiState.canCheck,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = stringResource(R.string.solve_action_check))
                    }
                }
            }
        }
    }
}

@Preview(name = "Puzzle blocks and slots")
@Composable
private fun PuzzlePiecesPreview() {
    LeetCodeReminderTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    EmptySlot(isTarget = false, onClick = {})
                    EmptySlot(isTarget = true, onClick = {})
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BlockStyle.entries.forEach { style ->
                        ChunkyBlock(text = "return", style = style, onClick = {})
                    }
                }
            }
        }
    }
}
