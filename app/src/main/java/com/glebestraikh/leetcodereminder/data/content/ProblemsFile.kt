package com.glebestraikh.leetcodereminder.data.content

import kotlinx.serialization.Serializable

@Serializable
data class ProblemsFile(
    val contentVersion: Int,
    val problems: List<ProblemJson>,
)

@Serializable
data class ProblemJson(
    val id: String,
    val number: Int,
    val title: String,
    val difficulty: String,
    val pattern: String,
    val statement: String,
    val examples: List<ExampleJson>,
    val constraints: List<String>,
    val timeComplexity: String,
    val spaceComplexity: String,
    val idea: String,
    val keyLine: String,
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
