package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Puzzle
import com.glebestraikh.leetcodereminder.model.PuzzleBlock
import org.junit.Assert.assertEquals
import org.junit.Test

class PuzzleCheckerTest {

    // Slots 0 and 2 expect the same text.
    private val puzzle = Puzzle(
        lines = emptyList(),
        slotAnswers = listOf("a", "b", "a"),
        blocks = listOf(PuzzleBlock(0, "a"), PuzzleBlock(1, "b"), PuzzleBlock(2, "a")),
    )

    @Test
    fun whenAllCorrect_noWrongSlots() {
        assertEquals(emptySet<Int>(), PuzzleChecker.findWrongSlots(puzzle, listOf(0, 1, 2)))
    }

    @Test
    fun whenIdenticalBlocksSwapped_stillCorrect() {
        assertEquals(emptySet<Int>(), PuzzleChecker.findWrongSlots(puzzle, listOf(2, 1, 0)))
    }

    @Test
    fun whenWrongOrder_wrongSlotsReturned() {
        assertEquals(setOf(0, 1), PuzzleChecker.findWrongSlots(puzzle, listOf(1, 0, 2)))
    }

    @Test
    fun whenSlotEmpty_itIsWrong() {
        assertEquals(setOf(1), PuzzleChecker.findWrongSlots(puzzle, listOf(0, null, 2)))
    }
}
