package com.glebestraikh.leetcodereminder.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glebestraikh.leetcodereminder.Graph
import com.glebestraikh.leetcodereminder.domain.ProblemCatalog
import com.glebestraikh.leetcodereminder.model.ProblemTopic
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    problemCatalog: ProblemCatalog = Graph.problemCatalog,
) : ViewModel() {
    val state: StateFlow<HomeScreenUiState> = problemCatalog.observeTopics()
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
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeScreenUiState(),
        )
}

@Immutable
data class HomeScreenUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val topics: List<ProblemTopic> = emptyList(),
)
