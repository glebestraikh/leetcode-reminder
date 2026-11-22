package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Hint
import com.glebestraikh.leetcodereminder.model.HintType
import com.glebestraikh.leetcodereminder.model.Problem

/**
 * Hints in the order they are revealed: from a small nudge to the full solution.
 * The pattern is not a hint: the problem screen shows it right after the description.
 */
fun Problem.hints(): List<Hint> = listOf(
    Hint(HintType.COMPLEXITY, "Time: $timeComplexity, space: $spaceComplexity"),
    Hint(HintType.IDEA, idea),
    Hint(HintType.SOLUTION, solution.replace("\t", "    ")),
)
