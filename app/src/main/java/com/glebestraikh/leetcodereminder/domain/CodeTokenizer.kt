package com.glebestraikh.leetcodereminder.domain

object CodeTokenizer {
    data class Token(val text: String, val start: Int) {
        val end: Int get() = start + text.length
    }

    private val operators = listOf(
        "<<=", ">>=", "&^=", "...",
        ":=", "==", "!=", "<=", ">=", "&&", "||", "++", "--",
        "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "<<", ">>", "<-", "&^",
    )

    fun tokenize(line: String): List<Token> {
        val tokens = mutableListOf<Token>()
        var index = 0
        while (index < line.length) {
            val char = line[index]
            when {
                char.isWhitespace() -> index++
                line.startsWith("//", index) -> break
                char.isLetterOrDigit() || char == '_' -> {
                    val start = index
                    while (index < line.length && (line[index].isLetterOrDigit() || line[index] == '_')) index++
                    tokens += Token(line.substring(start, index), start)
                }
                char == '"' || char == '\'' || char == '`' -> {
                    val start = index
                    index++
                    while (index < line.length && line[index] != char) {
                        if (line[index] == '\\' && char != '`') index++
                        index++
                    }
                    index = minOf(index + 1, line.length)
                    tokens += Token(line.substring(start, index), start)
                }
                else -> {
                    val operator = operators.firstOrNull { line.startsWith(it, index) } ?: char.toString()
                    tokens += Token(operator, index)
                    index += operator.length
                }
            }
        }
        return tokens
    }
}
