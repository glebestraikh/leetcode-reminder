package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Puzzle

object PuzzleChecker {
    fun findWrongSlots(puzzle: Puzzle, placedBlockIndices: List<Int?>): Set<Int> =
        puzzle.slotAnswers.indices.filterTo(mutableSetOf()) { slot ->
            val blockIndex = placedBlockIndices.getOrNull(slot)
            blockIndex == null || puzzle.blocks[blockIndex].text != puzzle.slotAnswers[slot]
        }
}
