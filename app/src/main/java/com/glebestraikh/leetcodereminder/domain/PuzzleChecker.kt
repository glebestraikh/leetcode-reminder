package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Puzzle

object PuzzleChecker {
    /**
     * Returns the slots that are empty or hold a wrong block. An empty set means the puzzle is solved.
     *
     * Blocks with the same text fit the same slots.
     */
    fun findWrongSlots(puzzle: Puzzle, placedBlockIndices: List<Int?>): Set<Int> =
        puzzle.slotAnswers.indices.filterTo(mutableSetOf()) { slot ->
            val blockIndex = placedBlockIndices.getOrNull(slot)
            blockIndex == null || puzzle.blocks[blockIndex].text != puzzle.slotAnswers[slot]
        }
}
