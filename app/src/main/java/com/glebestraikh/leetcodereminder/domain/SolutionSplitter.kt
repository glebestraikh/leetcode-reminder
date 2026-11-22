package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Puzzle
import com.glebestraikh.leetcodereminder.model.PuzzleBlock
import com.glebestraikh.leetcodereminder.model.PuzzleLine
import com.glebestraikh.leetcodereminder.model.PuzzlePart
import kotlin.random.Random

object SolutionSplitter {
    private val assignmentOperators = setOf(":=", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<=", ">>=", "&^=")
    private val comparisonOperators = setOf("<", ">", "<=", ">=", "==", "!=")
    private val openBrackets = setOf("(", "[", "{")
    private val closeBrackets = setOf(")", "]", "}")

    private const val MAX_TOKENS_PER_BLOCK = 8

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

    fun splitIntoBlocks(line: String): List<String> {
        val tokens = CodeTokenizer.tokenize(line)
        if (tokens.size <= 2) return listOf(line)

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
            if (end - start > MAX_TOKENS_PER_BLOCK) {
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
            line.substring(tokens[start].start, tokens[end - 1].end)
        }
    }

    private val CodeLine.isSkeleton: Boolean
        get() = indent == 0 || code == "} else {" || code.all { it in "}])" }
}

internal data class CodeLine(
    val code: String,
    val indent: Int = 0,
)
