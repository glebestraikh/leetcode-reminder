package com.glebestraikh.leetcodereminder.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebestraikh.leetcodereminder.Graph
import com.glebestraikh.leetcodereminder.domain.ProblemTopicsUseCase
import com.glebestraikh.leetcodereminder.model.ProblemTopic
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    problemTopicsUseCase: ProblemTopicsUseCase = Graph.problemTopicsUseCase,
) : ViewModel() {

    val state: StateFlow<HomeScreenUiState> = problemTopicsUseCase()
        .map { topics -> HomeScreenUiState(isLoading = false, topics = topics) }
        .catch { throwable ->
            emit(
                HomeScreenUiState(
                    isLoading = false,
                    errorMessage = throwable.message ?: "Could not load problems",
                ),
            )
        }
        .stateIn(
            scope = viewModelScope,
            // When the screen has been hidden for more than 5 seconds, the database subscription stops.
            // 5 seconds so that a screen rotation does not restart the queries.
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeScreenUiState(),
        )
}

@Immutable
data class HomeScreenUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    /** Topics from easy to hard. Each one is a tab. */
    val topics: List<ProblemTopic> = emptyList(),
)
