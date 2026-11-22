package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Puzzle
import com.glebestraikh.leetcodereminder.model.PuzzleBlock
import com.glebestraikh.leetcodereminder.model.PuzzleLine
import com.glebestraikh.leetcodereminder.model.PuzzlePart
import kotlin.random.Random

/**
 * Cuts the solution text into a [Puzzle] for the Solve screen. It runs every time the screen
 * opens: it is fast, and the database keeps only the solution text.
 */
object SolutionSplitter {
    private val assignmentOperators = setOf(
        ":=", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<=", ">>=", "&^=",
    )
    private val comparisonOperators = setOf("<", ">", "<=", ">=", "==", "!=")
    private val openBrackets = setOf("(", "[", "{")
    private val closeBrackets = setOf(")", "]", "}")

    /** A block longer than this is cut once more, at commas and comparisons. */
    private const val MaxTokensPerBlock = 8

    /**
     * Builds the puzzle. [random] decides the order of the blocks, so the same seed always
     * gives the same puzzle.
     */
    fun createPuzzle(solution: String, random: Random): Puzzle {
        val slotAnswers = mutableListOf<String>()
        val lines = splitIntoLines(solution)
            .filterNot { it.code.startsWith("//") }
            .map { line ->
                if (line.isSkeleton) {
                    PuzzleLine(indent = line.indent, parts = listOf(PuzzlePart.Fixed(line.code)))
                } else {
                    val blocks = splitIntoBlocks(line.code)
                    PuzzleLine(
                        indent = line.indent,
                        parts = blocks.map { block ->
                            slotAnswers += block
                            PuzzlePart.Slot(index = slotAnswers.lastIndex)
                        },
                    )
                }
            }

        val blocks = slotAnswers
            .shuffled(random)
            .mapIndexed { index, text -> PuzzleBlock(index = index, text = text) }

        return Puzzle(lines = lines, slotAnswers = slotAnswers, blocks = blocks)
    }

    /**
     * One [CodeLine] per line. Blank lines are skipped. The nesting level is counted from the
     * leading whitespace: a tab is one level, and for spaces the step is the smallest space indent
     * in the solution, so code indented with 2 or 4 spaces works too.
     */
    internal fun splitIntoLines(solution: String): List<CodeLine> {
        val lines = solution.lines().filter { it.isNotBlank() }
        val indents = lines.map { line -> line.takeWhile { it == ' ' || it == '\t' } }
        val spacesPerLevel = indents
            .map { indent -> indent.count { it == ' ' } }
            .filter { it > 0 }
            .minOrNull() ?: 1

        return lines.zip(indents) { line, indent ->
            CodeLine(
                code = line.trim(),
                indent = indent.count { it == '\t' } + indent.count { it == ' ' } / spacesPerLevel,
            )
        }
    }

    /**
     * Cuts one line into blocks of a few tokens, for example
     * `for i := 0; i < len(nums); i++ {` into `for i := 0`, `; i < len(nums)` and `; i++ {`.
     *
     * Cuts happen only outside brackets: before `;`, `&&` and `||`, after an assignment and after
     * `return`. Long blocks are cut again after commas and before comparisons. A block of a single
     * token is too small to be interesting, so it is glued to the block before it.
     */
    fun splitIntoBlocks(code: String): List<String> {
        val tokens = CodeTokenizer.tokenize(code)
        if (tokens.size <= 2) return listOf(code)

        // How deep inside brackets each token is. 0 means outside any brackets.
        val depths = IntArray(tokens.size)
        var depth = 0
        tokens.forEachIndexed { index, token ->
            if (token.text in closeBrackets) depth = maxOf(0, depth - 1)
            depths[index] = depth
            if (token.text in openBrackets) depth++
        }

        fun isMainCut(index: Int): Boolean {
            val text = tokens[index].text
            val previous = tokens[index - 1].text
            return text == ";" || text == "&&" || text == "||" ||
                previous in assignmentOperators || previous == "return"
        }

        fun isExtraCut(index: Int): Boolean =
            tokens[index - 1].text == "," || tokens[index].text in comparisonOperators

        val mainCuts = (1 until tokens.size).filter { depths[it] == 0 && isMainCut(it) }
        val cuts = mutableListOf<Int>()
        (listOf(0) + mainCuts + tokens.size).zipWithNext { start, end ->
            cuts += start
            if (end - start > MaxTokensPerBlock) {
                cuts += (start + 1 until end).filter { depths[it] == 0 && isExtraCut(it) }
            }
        }

        val ranges = (cuts + tokens.size).zipWithNext()
        val merged = mutableListOf<Pair<Int, Int>>()
        for (range in ranges) {
            val isSingleToken = range.second - range.first == 1
            if (isSingleToken && merged.isNotEmpty()) {
                merged[merged.lastIndex] = merged.last().first to range.second
            } else {
                merged += range
            }
        }

        return merged.map { (start, end) ->
            code.substring(tokens[start].start, tokens[end - 1].end)
        }
    }

    /**
     * The skeleton stays in place and is not a block: top-level lines (function signatures,
     * type declarations and their closing braces) and lines of closing brackets only.
     */
    private val CodeLine.isSkeleton: Boolean
        get() = indent == 0 || code == "} else {" || code.all { it in "}])" }
}

internal class CodeLine(
    val code: String,
    val indent: Int = 0,
)
