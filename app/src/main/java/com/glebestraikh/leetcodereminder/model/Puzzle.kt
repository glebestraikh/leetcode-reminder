package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

@Immutable
data class Puzzle(
    val lines: List<PuzzleLine>,
    val slotAnswers: List<String>,
    val blocks: List<PuzzleBlock>,
)

@Immutable
data class PuzzleLine(
    val indent: Int,
    val parts: List<PuzzlePart>,
)

@Immutable
sealed interface PuzzlePart {
    data class Fixed(val text: String) : PuzzlePart

    data class Slot(val index: Int) : PuzzlePart
}

@Immutable
data class PuzzleBlock(
    val index: Int,
    val text: String,
)
