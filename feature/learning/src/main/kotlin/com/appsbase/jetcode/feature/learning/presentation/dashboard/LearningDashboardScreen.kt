package com.appsbase.jetcode.feature.learning.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.core.ui.components.SkillCard
import org.koin.androidx.compose.koinViewModel

/**
 * Learning Dashboard Screen - Main screen showing available skills
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningDashboardScreen(
    onSkillClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LearningDashboardViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle side effects
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LearningDashboardEffect.NavigateToSkillDetail -> {
                    onSkillClick(effect.skillId)
                }
                is LearningDashboardEffect.NavigateToProfile -> {
                    onProfileClick()
                }
                is LearningDashboardEffect.ShowError -> {
                    // Handle error - could show snackbar
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "JetCode Learning",
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = { viewModel.handleIntent(LearningDashboardIntent.ProfileClicked) }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile"
                    )
                }
            }
        )

        // Content
        when {
            state.isLoading && state.skills.isEmpty() -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize(),
                    message = "Loading skills..."
                )
            }

            state.error != null && state.skills.isEmpty() -> {
                ErrorState(
                    message = state.error ?: "Unknown error",
                    onRetry = { viewModel.handleIntent(LearningDashboardIntent.RetryClicked) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                LearningContent(
                    state = state,
                    onIntent = viewModel::handleIntent,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun LearningContent(
    state: LearningDashboardState,
    onIntent: (LearningDashboardIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { query ->
                onIntent(LearningDashboardIntent.SearchSkills(query))
            },
            label = { Text("Search skills...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        // Pull to refresh
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Skills List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (state.filteredSkills.isEmpty() && state.searchQuery.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No skills found for \"${state.searchQuery}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                } else {
                    items(
                        items = state.filteredSkills,
                        key = { it.id }
                    ) { skill ->
                        SkillCard(
                            skill = skill,
                            onClick = {
                                onIntent(LearningDashboardIntent.SkillClicked(skill.id))
                            }
                        )
                    }
                }

                // Add refresh button at the bottom
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { onIntent(LearningDashboardIntent.RefreshSkills) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isRefreshing
                    ) {
                        if (state.isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(if (state.isRefreshing) "Syncing..." else "Sync Content")
                    }
                }
            }
        }
    }
}
