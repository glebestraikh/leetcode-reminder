package com.glebestraikh.leetcodereminder.domain.model

data class Problem(
    val id: String,
    val number: Int,
    val title: String,
    val difficulty: Difficulty,
    val pattern: String,
    val statement: String,
    val examples: List<Example>,
    val constraints: List<String>,
    val timeComplexity: String,
    val spaceComplexity: String,
    val idea: String,
    val keyLine: String,
    val solution: List<CodeLine>,
)

data class Example(
    val input: String,
    val output: String,
    val explanation: String? = null,
)

data class CodeLine(
    val code: String,
    val indent: Int,
)
