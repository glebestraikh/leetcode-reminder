package com.glebestraikh.leetcodereminder.data.sync

import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.model.Problem

object ProblemMarkdownParser {
    fun parse(slug: String, markdown: String, topics: List<String>): Problem {
        val lines = markdown.replace("\r\n", "\n").lines()
        require(lines.firstOrNull() == HEADER_DELIMITER) { "$slug: the file must start with $HEADER_DELIMITER" }
        val headerEnd = requireNotNull((1..lines.lastIndex).firstOrNull { lines[it] == HEADER_DELIMITER }) {
            "$slug: the header must end with $HEADER_DELIMITER"
        }

        val header = parseHeader(slug, lines.subList(1, headerEnd))
        val sections = parseSections(lines.subList(headerEnd + 1, lines.size))

        fun field(key: String): String = requireNotNull(header[key]) { "$slug: missing header field '$key'" }
        fun section(name: String): String = requireNotNull(sections[name]) { "$slug: missing section '## $name'" }

        val topic = field("topic")
        val topicOrder = topics.indexOf(topic)
        require(topicOrder != -1) { "$slug: topic '$topic' is not listed in topics.txt" }

        return Problem(
            number = requireNotNull(field("number").toIntOrNull()) { "$slug: number must be an integer" },
            slug = slug,
            title = field("title"),
            difficulty = requireNotNull(Difficulty.entries.find { it.name == field("difficulty").uppercase() }) {
                "$slug: difficulty must be one of ${Difficulty.entries}"
            },
            topic = topic,
            topicOrder = topicOrder,
            pattern = field("pattern"),
            description = section("Description"),
            timeComplexity = field("time"),
            spaceComplexity = field("space"),
            idea = section("Idea"),
            solution = section("Solution").removeCodeFence(),
        )
    }

    fun parseTopics(text: String): List<String> =
        text.lines().map { it.trim() }.filter { it.isNotEmpty() }

    private fun parseHeader(slug: String, lines: List<String>): Map<String, String> =
        lines.filter { it.isNotBlank() }.associate { line ->
            val key = line.substringBefore(":", missingDelimiterValue = "").trim()
            require(key.isNotEmpty()) { "$slug: invalid header line '$line'" }
            key to line.substringAfter(":").trim()
        }

    private fun parseSections(lines: List<String>): Map<String, String> {
        val sections = mutableMapOf<String, String>()
        var name: String? = null
        val content = mutableListOf<String>()

        fun saveSection() {
            name?.let { sections[it] = content.trimBlankLines().joinToString("\n") }
            content.clear()
        }

        for (line in lines) {
            if (line.startsWith(SECTION_PREFIX)) {
                saveSection()
                name = line.removePrefix(SECTION_PREFIX).trim()
            } else {
                content += line
            }
        }
        saveSection()
        return sections
    }

    private fun String.removeCodeFence(): String {
        val lines = lines()
        if (lines.size < 2 || !lines.first().startsWith(CODE_FENCE) || lines.last() != CODE_FENCE) return this
        return lines.subList(1, lines.size - 1).joinToString("\n")
    }

    private fun List<String>.trimBlankLines(): List<String> =
        dropWhile { it.isBlank() }.dropLastWhile { it.isBlank() }

    private const val HEADER_DELIMITER = "---"
    private const val SECTION_PREFIX = "## "
    private const val CODE_FENCE = "```"
}
