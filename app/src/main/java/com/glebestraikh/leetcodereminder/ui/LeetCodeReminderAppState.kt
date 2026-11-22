package com.glebestraikh.leetcodereminder.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data class ProblemDetails(val problemNumber: Int) : Screen
}

@Composable
fun rememberLeetCodeReminderAppState(backStack: MutableList<NavKey> = rememberNavBackStack(Screen.Home)) =
    remember(backStack) {
        LeetCodeReminderAppState(backStack)
    }

class LeetCodeReminderAppState(val backStack: MutableList<NavKey>) {
    fun navigateToProblemDetails(problemNumber: Int) {
        backStack.add(Screen.ProblemDetails(problemNumber))
    }

    fun navigateBackToHome() {
        backStack.removeAll { it != Screen.Home }
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }
}
