package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

/**
 * A solution cut into blocks for the Solve screen.
 *
 * The code is shown line by line. Some parts of a line are fixed (the skeleton: function
 * signatures and closing braces), the rest are empty slots. The user fills the slots with [blocks].
 */
@Immutable
data class Puzzle(
    val lines: List<PuzzleLine>,
    /** The correct text for each slot, by slot index. */
    val slotAnswers: List<String>,
    /** All blocks the user can pick from, in shuffled order: exactly one for every slot. */
    val blocks: List<PuzzleBlock>,
)

@Immutable
data class PuzzleLine(
    val indent: Int,
    val parts: List<PuzzlePart>,
)

@Immutable
sealed interface PuzzlePart {
    /** Code that is already in place and cannot be moved. */
    data class Fixed(val text: String) : PuzzlePart

    /** An empty place for a block. [index] is the slot number in [Puzzle.slotAnswers]. */
    data class Slot(val index: Int) : PuzzlePart
}

/**
 * A block the user can move. [id] is its index in [Puzzle.blocks].
 * Two blocks can have the same text: either of them fits a slot with that text.
 */
@Immutable
data class PuzzleBlock(
    val id: Int,
    val text: String,
)
