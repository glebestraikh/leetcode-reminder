package com.glebestraikh.leetcodereminder.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.glebestraikh.leetcodereminder.ui.home.MainScreen
import com.glebestraikh.leetcodereminder.ui.problem.ProblemDetailsScreen
import com.glebestraikh.leetcodereminder.ui.problem.ProblemDetailsViewModel
import com.glebestraikh.leetcodereminder.util.viewModelProviderFactoryOf

@Composable
fun LeetCodeReminderApp(appState: LeetCodeReminderAppState = rememberLeetCodeReminderAppState()) {
    NavDisplay(
        backStack = appState.backStack,
        onBack = { appState.navigateBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>(),
        ),
        entryProvider = entryProvider {
            entry<Screen.Home> {
                MainScreen(
                    navigateToProblemDetails = { problem ->
                        appState.navigateToProblemDetails(problem.number)
                    },
                )
            }
            entry<Screen.ProblemDetails> { screen ->
                val viewModel: ProblemDetailsViewModel = viewModel(
                    factory = viewModelProviderFactoryOf {
                        ProblemDetailsViewModel(
                            savedStateHandle = createSavedStateHandle(),
                            problemNumber = screen.problemNumber,
                        )
                    },
                )
                ProblemDetailsScreen(
                    viewModel = viewModel,
                    navigateBackToHome = appState::navigateBackToHome,
                    navigateBack = appState::navigateBack,
                )
            }
        },
    )
}
