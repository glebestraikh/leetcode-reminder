package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.PuzzleLine
import com.glebestraikh.leetcodereminder.model.PuzzlePart
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class SolutionSplitterTest {

    private val solution = "func f() int {\n\ta := 1\n\tif a > 0 {\n\t\treturn a\n\t}\n\treturn 0\n}"

    @Test
    fun whenIndentedWithTabs_eachTabIsOneLevel() {
        val solution = "func f() {\n\tfor {\n\t\treturn\n\t}\n}"

        assertEquals(
            listOf(
                "func f() {" to 0,
                "for {" to 1,
                "return" to 2,
                "}" to 1,
                "}" to 0,
            ),
            SolutionSplitter.splitIntoLines(solution).map { it.code to it.indent },
        )
    }

    @Test
    fun whenIndentedWithSpaces_smallestIndentIsOneLevel() {
        val solution = "func f() {\n  for {\n    return\n  }\n}"

        assertEquals(listOf(0, 1, 2, 1, 0), SolutionSplitter.splitIntoLines(solution).map { it.indent })
    }

    @Test
    fun blankLinesAreSkipped() {
        val solution = "a := 1\n\n   \nb := 2"

        assertEquals(listOf("a := 1", "b := 2"), SolutionSplitter.splitIntoLines(solution).map { it.code })
    }

    @Test
    fun skeletonIsFixedAndOtherLinesAreCutIntoSlots() {
        val puzzle = SolutionSplitter.createPuzzle(solution, Random(1))

        assertEquals(
            listOf(
                PuzzleLine(0, listOf(PuzzlePart.Fixed("func f() int {"))),
                PuzzleLine(1, listOf(PuzzlePart.Slot(0))),
                PuzzleLine(1, listOf(PuzzlePart.Slot(1))),
                PuzzleLine(2, listOf(PuzzlePart.Slot(2))),
                PuzzleLine(1, listOf(PuzzlePart.Fixed("}"))),
                PuzzleLine(1, listOf(PuzzlePart.Slot(3))),
                PuzzleLine(0, listOf(PuzzlePart.Fixed("}"))),
            ),
            puzzle.lines,
        )
        assertEquals(listOf("a := 1", "if a > 0 {", "return a", "return 0"), puzzle.slotAnswers)
    }

    @Test
    fun oneBlockForEverySlot() {
        val puzzle = SolutionSplitter.createPuzzle(solution, Random(1))

        assertEquals(puzzle.slotAnswers.sorted(), puzzle.blocks.map { it.text }.sorted())
    }

    @Test
    fun sameSeed_samePuzzle() {
        assertEquals(
            SolutionSplitter.createPuzzle(solution, Random(7)),
            SolutionSplitter.createPuzzle(solution, Random(7)),
        )
    }

    @Test
    fun splitIntoBlocks_cutsAtSemicolonsAndAfterAssignment() {
        assertEquals(
            listOf("for i := 0", "; i < len(nums)", "; i++ {"),
            SolutionSplitter.splitIntoBlocks("for i := 0; i < len(nums); i++ {"),
        )
        assertEquals(
            listOf("if idx, ok :=", "prevMap[diff]", "; ok {"),
            SolutionSplitter.splitIntoBlocks("if idx, ok := prevMap[diff]; ok {"),
        )
        assertEquals(
            listOf("return", "[]int{idx, i}"),
            SolutionSplitter.splitIntoBlocks("return []int{idx, i}"),
        )
    }

    @Test
    fun splitIntoBlocks_singleTokenIsGluedToThePreviousBlock() {
        assertEquals(listOf("prevMap[v] = i"), SolutionSplitter.splitIntoBlocks("prevMap[v] = i"))
    }
}
