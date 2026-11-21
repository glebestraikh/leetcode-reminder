package com.glebestraikh.leetcodereminder.model

import androidx.compose.runtime.Immutable

/**
 * External data layer representation of a problem.
 */
@Immutable
data class ProblemInfo(
    val number: Int = 0,
    /** The problem name in the LeetCode URL, also the name of its file in assets. */
    val slug: String = "",
    val title: String = "",
    val difficulty: Difficulty = Difficulty.EASY,
    /** A tab on the home screen, such as "Arrays & Hashing". */
    val topic: String = "",
    /** The exact technique, such as "Prefix Sum". One topic has several patterns. */
    val pattern: String = "",
    /** The statement, examples and constraints as a single text, as on LeetCode. */
    val description: String = "",
    val timeComplexity: String = "",
    val spaceComplexity: String = "",
    val idea: String = "",
    /** The reference solution as plain code, indented with tabs. */
    val solution: String = "",
)

fun Problem.asExternalModel(): ProblemInfo = ProblemInfo(
    number = number,
    slug = slug,
    title = title,
    difficulty = difficulty,
    topic = topic,
    pattern = pattern,
    description = description,
    timeComplexity = timeComplexity,
    spaceComplexity = spaceComplexity,
    idea = idea,
    solution = solution,
)

val ProblemInfo.leetCodeUrl: String
    get() = "https://leetcode.com/problems/$slug/"
