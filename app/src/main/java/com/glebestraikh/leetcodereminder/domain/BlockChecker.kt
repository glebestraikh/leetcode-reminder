package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.domain.model.CodeLine

object BlockChecker {
    fun check(expected: List<CodeLine>, actual: List<CodeLine>): CheckResult {
        val firstWrongIndex = expected.indices.firstOrNull { index ->
            actual.getOrNull(index) != expected[index]
        }
        return when {
            firstWrongIndex != null -> CheckResult.Incorrect(firstWrongIndex)
            actual.size > expected.size -> CheckResult.Incorrect(expected.size)
            else -> CheckResult.Correct
        }
    }
}

sealed interface CheckResult {
    data object Correct : CheckResult
    data class Incorrect(val firstWrongIndex: Int) : CheckResult
}
