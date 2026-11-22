package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Hint
import com.glebestraikh.leetcodereminder.model.HintType
import com.glebestraikh.leetcodereminder.model.Problem

fun Problem.hints(): List<Hint> = listOf(
    Hint(HintType.COMPLEXITY, "Time: $timeComplexity, space: $spaceComplexity"),
    Hint(HintType.IDEA, idea),
    Hint(HintType.SOLUTION, solution.replace("\t", "    ")),
)
