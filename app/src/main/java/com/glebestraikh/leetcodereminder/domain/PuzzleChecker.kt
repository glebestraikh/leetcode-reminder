package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Puzzle

object PuzzleChecker {
    /**
     * Returns the slots that are empty or hold a wrong block. An empty set means the puzzle is solved.
     *
     * Blocks are compared by text, not by id: if two lines are the same, either block fits either slot.
     */
    fun findWrongSlots(puzzle: Puzzle, placedBlockIds: List<Int?>): Set<Int> =
        puzzle.slotAnswers.indices.filterTo(mutableSetOf()) { slot ->
            val blockId = placedBlockIds.getOrNull(slot)
            blockId == null || puzzle.blocks[blockId].text != puzzle.slotAnswers[slot]
        }
}
