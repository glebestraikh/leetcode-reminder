package com.glebestraikh.leetcodereminder.data.sync

import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.model.Problem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class ProblemMarkdownParserTest {

    private val topics = listOf("Two Pointers", "Arrays & Hashing")

    private val markdown = """
        |---
        |number: 1
        |title: Two Sum
        |difficulty: easy
        |topic: Arrays & Hashing
        |pattern: Hash Map
        |time: O(n)
        |space: O(n)
        |---
        |
        |## Description
        |
        |Find two numbers.
        |
        |Example 1:
        |Input: nums = [2,7], target = 9
        |
        |## Idea
        |
        |Use a map.
        |
        |## Solution
        |
        |```go
        |func twoSum() {
        |	return
        |}
        |```
        |
    """.trimMargin()

    @Test
    fun whenValidFile_allFieldsParsed() {
        assertEquals(
            Problem(
                number = 1,
                slug = "two-sum",
                title = "Two Sum",
                difficulty = Difficulty.EASY,
                topic = "Arrays & Hashing",
                topicOrder = 1,
                pattern = "Hash Map",
                description = "Find two numbers.\n\nExample 1:\nInput: nums = [2,7], target = 9",
                timeComplexity = "O(n)",
                spaceComplexity = "O(n)",
                idea = "Use a map.",
                solution = "func twoSum() {\n\treturn\n}",
            ),
            ProblemMarkdownParser.parse(topics = topics, slug = "two-sum", markdown = markdown),
        )
    }

    @Test
    fun whenWindowsLineEndings_sameResult() {
        assertEquals(
            ProblemMarkdownParser.parse(topics = topics, slug = "two-sum", markdown = markdown),
            ProblemMarkdownParser.parse(topics = topics, slug = "two-sum", markdown = markdown.replace("\n", "\r\n")),
        )
    }

    @Test
    fun whenFieldMissing_errorNamesFileAndField() {
        val error = assertThrows(IllegalArgumentException::class.java) {
            ProblemMarkdownParser.parse(topics = topics, slug = "two-sum", markdown = markdown.replace("pattern: Hash Map\n", ""))
        }
        assertTrue(error.message!!.contains("two-sum"))
        assertTrue(error.message!!.contains("pattern"))
    }

    @Test
    fun whenUnknownDifficulty_error() {
        assertThrows(IllegalArgumentException::class.java) {
            ProblemMarkdownParser.parse(topics = topics, slug = "two-sum", markdown = markdown.replace("difficulty: easy", "difficulty: easyy"))
        }
    }

    @Test
    fun whenSectionMissing_error() {
        assertThrows(IllegalArgumentException::class.java) {
            ProblemMarkdownParser.parse(topics = topics, slug = "two-sum", markdown = markdown.replace("## Idea", "## Notes"))
        }
    }

    @Test
    fun whenTopicNotInTopicsFile_error() {
        val error = assertThrows(IllegalArgumentException::class.java) {
            ProblemMarkdownParser.parse(
                topics = topics,
                slug = "two-sum",
                markdown = markdown.replace("topic: Arrays & Hashing", "topic: Graphs"),
            )
        }
        assertTrue(error.message!!.contains("Graphs"))
    }

    @Test
    fun parseTopics_skipsBlankLines() {
        assertEquals(listOf("Arrays & Hashing", "Two Pointers"), ProblemMarkdownParser.parseTopics("Arrays & Hashing\n\n Two Pointers \n"))
    }
}
