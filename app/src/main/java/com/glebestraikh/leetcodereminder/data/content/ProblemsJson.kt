package com.glebestraikh.leetcodereminder.data.content

import com.glebestraikh.leetcodereminder.data.database.model.CodeLine
import com.glebestraikh.leetcodereminder.data.database.model.Difficulty
import com.glebestraikh.leetcodereminder.data.database.model.Example
import com.glebestraikh.leetcodereminder.data.database.model.Problem
import kotlinx.serialization.Serializable

@Serializable
data class ProblemsJson(
    val problems: List<ProblemJson>,
)

@Serializable
data class ProblemJson(
    val id: String,
    val number: Int,
    val title: String,
    val difficulty: Difficulty,
    val pattern: String,
    val statement: String,
    val examples: List<ExampleJson>,
    val constraints: List<String>,
    val timeComplexity: String,
    val spaceComplexity: String,
    val idea: String,
    val solution: List<CodeLineJson>,
)

@Serializable
data class ExampleJson(
    val input: String,
    val output: String,
    val explanation: String? = null,
)

@Serializable
data class CodeLineJson(
    val code: String,
    val indent: Int = 0,
)

fun ProblemsJson.toProblems(): List<Problem> = problems.map { it.toProblem() }

fun ProblemJson.toProblem(): Problem = Problem(
    id = id,
    number = number,
    title = title,
    difficulty = difficulty,
    pattern = pattern,
    statement = statement,
    examples = examples.map { it.toExample() },
    constraints = constraints,
    timeComplexity = timeComplexity,
    spaceComplexity = spaceComplexity,
    idea = idea,
    solution = solution.map { it.toCodeLine() },
)

fun ExampleJson.toExample(): Example = Example(
    input = input,
    output = output,
    explanation = explanation,
)

fun CodeLineJson.toCodeLine(): CodeLine = CodeLine(
    code = code,
    indent = indent,
)
