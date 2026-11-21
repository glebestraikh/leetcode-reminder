package com.glebestraikh.leetcodereminder.domain.testing

import com.glebestraikh.leetcodereminder.model.Difficulty
import com.glebestraikh.leetcodereminder.data.database.model.Rating
import com.glebestraikh.leetcodereminder.model.ProblemInfo
import com.glebestraikh.leetcodereminder.model.ProblemToProgressInfo
import com.glebestraikh.leetcodereminder.model.ProblemTopic
import com.glebestraikh.leetcodereminder.model.ProgressInfo

val PreviewProblems = listOf(
    ProblemInfo(
        number = 1,
        slug = "two-sum",
        title = "Two Sum",
        difficulty = Difficulty.EASY,
        topic = "Arrays & Hashing",
        pattern = "Hash Map",
        description = """
            Given an array of integers nums and an integer target, return the indices of the two different elements that add up to target.

            Example 1:
            Input: nums = [2,7,11,15], target = 9
            Output: [0,1]

            Constraints:
            - 2 ≤ len(nums) ≤ 10^4
        """.trimIndent(),
        timeComplexity = "O(n)",
        spaceComplexity = "O(n)",
        idea = "Keep the numbers seen so far in a map from value to index.",
        solution = "func twoSum(nums []int, target int) []int {\n" +
            "\tseen := make(map[int]int)\n" +
            "\tfor i, x := range nums {\n" +
            "\t\tif j, ok := seen[target-x]; ok {\n" +
            "\t\t\treturn []int{j, i}\n" +
            "\t\t}\n" +
            "\t\tseen[x] = i\n" +
            "\t}\n" +
            "\treturn nil\n" +
            "}",
    ),
    ProblemInfo(
        number = 125,
        slug = "valid-palindrome",
        title = "Valid Palindrome",
        difficulty = Difficulty.EASY,
        topic = "Two Pointers",
        pattern = "Two Pointers",
        description = """
            A phrase is a palindrome if, after keeping only letters and digits and ignoring case, it reads the same both ways.

            Example 1:
            Input: s = "A man, a plan, a canal: Panama"
            Output: true
        """.trimIndent(),
        timeComplexity = "O(n)",
        spaceComplexity = "O(1)",
        idea = "Move two pointers from both ends towards the middle, skipping other characters.",
        solution = "func isPalindrome(s string) bool {\n" +
            "\tl, r := 0, len(s)-1\n" +
            "\tfor l < r {\n" +
            "\t\tif s[l] != s[r] {\n" +
            "\t\t\treturn false\n" +
            "\t\t}\n" +
            "\t\tl, r = l+1, r-1\n" +
            "\t}\n" +
            "\treturn true\n" +
            "}",
    ),
)

val PreviewProgress = listOf(
    ProgressInfo(problemNumber = 1, lastRating = Rating.NEED_TO_REPEAT, solvedCount = 3),
    ProgressInfo(problemNumber = 125),
)

val PreviewProblemTopics = listOf(
    ProblemTopic(
        name = "Arrays & Hashing",
        problems = listOf(ProblemToProgressInfo(problem = PreviewProblems[0], progress = PreviewProgress[0])),
    ),
    ProblemTopic(
        name = "Two Pointers",
        problems = listOf(ProblemToProgressInfo(problem = PreviewProblems[1], progress = PreviewProgress[1])),
    ),
)
