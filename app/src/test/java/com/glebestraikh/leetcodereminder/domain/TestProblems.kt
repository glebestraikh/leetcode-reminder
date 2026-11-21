package com.glebestraikh.leetcodereminder.domain

import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.model.Problem

/**
 * A problem for tests: only the fields that tests usually check are filled in.
 */
fun testProblem(
    number: Int = 1,
    difficulty: Difficulty = Difficulty.EASY,
    topic: String = "Arrays & Hashing",
    topicOrder: Int = 0,
    solution: String = "func f() {\n\treturn\n}",
) = Problem(
    number = number,
    slug = "problem-$number",
    title = "Problem $number",
    difficulty = difficulty,
    topic = topic,
    topicOrder = topicOrder,
    pattern = "Hash Map",
    description = "",
    timeComplexity = "O(1)",
    spaceComplexity = "O(1)",
    idea = "",
    solution = solution,
)
