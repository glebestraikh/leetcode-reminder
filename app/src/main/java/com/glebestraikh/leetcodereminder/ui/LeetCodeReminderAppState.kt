package com.glebestraikh.leetcodereminder.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import kotlinx.serialization.Serializable

/**
 * List of screens for [LeetCodeReminderApp]
 *
 * In Jetcaster these are string routes for Navigation Compose. We use Navigation 3:
 * a screen is a key, and its arguments live right inside it. The keys are `@Serializable`
 * so that the back stack survives a screen rotation and process death.
 */
sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    /**
     * The problem, its hints and building the solution: one screen, one attempt.
     * Going back removes the screen with its state, so the attempt starts over next time.
     */
    @Serializable
    data class ProblemDetails(val problemNumber: Int) : Screen
}

@Composable
fun rememberLeetCodeReminderAppState(backStack: MutableList<NavKey> = rememberNavBackStack(Screen.Home)) =
    remember(backStack) {
        LeetCodeReminderAppState(backStack)
    }

/**
 * The back stack is a plain list of screen keys. Navigating forward is `add`, back is `removeLastOrNull`.
 * Screens know nothing about the stack: they get callbacks, and this class decides what to do with the stack.
 */
class LeetCodeReminderAppState(val backStack: MutableList<NavKey>) {
    fun navigateToProblemDetails(problemNumber: Int) {
        backStack.add(Screen.ProblemDetails(problemNumber))
    }

    /**
     * After a solved attempt, go back to the home screen and remove everything else from the stack.
     */
    fun navigateBackToHome() {
        backStack.removeAll { it != Screen.Home }
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }
}
