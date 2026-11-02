package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.domain.model.Hint
import com.glebestraikh.leetcodereminder.domain.model.HintType
import com.glebestraikh.leetcodereminder.domain.model.Problem

/**
 * Подсказки задачи от самой общей к самой конкретной:
 * сложность для собеседования → паттерн → идея → ключевая строка → решение целиком.
 *
 * Тексты собираются из полей задачи, поэтому в problems.json подсказки
 * отдельно не хранятся.
 */
fun Problem.hints(): List<Hint> = listOf(
    Hint(HintType.COMPLEXITY, "Время: $timeComplexity, память: $spaceComplexity"),
    Hint(HintType.PATTERN, pattern),
    Hint(HintType.IDEA, idea),
    Hint(HintType.KEY_LINE, keyLine),
    Hint(HintType.SOLUTION, solution.joinToString(separator = "\n") { "    ".repeat(it.indent) + it.code }),
)
