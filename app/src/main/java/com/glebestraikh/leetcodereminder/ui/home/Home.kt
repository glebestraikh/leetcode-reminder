package com.glebestraikh.leetcodereminder.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.glebestraikh.leetcodereminder.R
import com.glebestraikh.leetcodereminder.model.Problem
import com.glebestraikh.leetcodereminder.model.ProblemTopic
import com.glebestraikh.leetcodereminder.model.ProblemWithProgress
import com.glebestraikh.leetcodereminder.ui.preview.PreviewProblemTopics
import com.glebestraikh.leetcodereminder.ui.shared.AppTopAppBar
import com.glebestraikh.leetcodereminder.ui.shared.DifficultyLabel
import com.glebestraikh.leetcodereminder.ui.shared.Loading
import com.glebestraikh.leetcodereminder.ui.shared.labelRes
import com.glebestraikh.leetcodereminder.ui.theme.LeetCodeReminderTheme

@Composable
fun MainScreen(
    navigateToProblemDetails: (Problem) -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    Box {
        HomeScreen(
            uiState = uiState,
            navigateToProblemDetails = navigateToProblemDetails,
        )

        if (uiState.errorMessage != null) {
            HomeScreenError()
        }
    }
}

@Composable
private fun HomeScreenError(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(
                text = stringResource(id = R.string.an_error_has_occurred),
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenErrorPreview() {
    LeetCodeReminderTheme {
        HomeScreenError()
    }
}

@Composable
private fun HomeScreen(
    uiState: HomeScreenUiState,
    navigateToProblemDetails: (Problem) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        topBar = { AppTopAppBar(title = stringResource(R.string.problem_list_title)) },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
        ) {
            when {
                uiState.isLoading -> Loading(modifier = Modifier.fillMaxSize())
                uiState.topics.isEmpty() -> Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.problem_list_empty),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                else -> {
                    val tab = selectedTab.coerceIn(0, uiState.topics.lastIndex)
                    TopicTabs(
                        topics = uiState.topics,
                        selectedTab = tab,
                        onTabSelected = { selectedTab = it },
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = uiState.topics[tab].problems, key = { it.problem.number }) { item ->
                            ProblemListItem(
                                item = item,
                                onClick = { navigateToProblemDetails(item.problem) },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicTabs(
    topics: List<ProblemTopic>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    PrimaryScrollableTabRow(
        selectedTabIndex = selectedTab,
        edgePadding = 16.dp,
    ) {
        topics.forEachIndexed { index, topic ->
            Tab(
                selected = index == selectedTab,
                onClick = { onTabSelected(index) },
                text = { Text(text = topic.name) },
            )
        }
    }
}

@Composable
private fun ProblemListItem(
    item: ProblemWithProgress,
    onClick: () -> Unit,
) {
    val problem = item.problem
    ListItem(
        headlineContent = { Text(text = "${problem.number}. ${problem.title}") },
        modifier = Modifier.clickable(onClick = onClick),
        supportingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = problem.pattern,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                DifficultyLabel(difficulty = problem.difficulty)
            }
        },
        trailingContent = item.progress.lastRating?.let { rating ->
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check),
                        contentDescription = stringResource(R.string.problem_solved),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        text = stringResource(rating.labelRes),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun PreviewHome() {
    LeetCodeReminderTheme {
        HomeScreen(
            uiState = HomeScreenUiState(isLoading = false, topics = PreviewProblemTopics),
            navigateToProblemDetails = {},
        )
    }
}

@Preview(name = "Empty catalog")
@Composable
private fun EmptyHomePreview() {
    LeetCodeReminderTheme {
        HomeScreen(uiState = HomeScreenUiState(isLoading = false), navigateToProblemDetails = {})
    }
}
