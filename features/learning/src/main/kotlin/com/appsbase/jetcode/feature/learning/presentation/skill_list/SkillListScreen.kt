package com.appsbase.jetcode.feature.learning.presentation.skill_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.ui.components.DifficultyChip
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.domain.model.Difficulty
import com.appsbase.jetcode.domain.model.Skill
import org.koin.androidx.compose.koinViewModel

/**
 * Learning Dashboard Screen - Main screen showing available skills
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillListScreen(
    onSkillClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SkillListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SkillListEffect.NavigateToSkillDetail -> {
                    onSkillClick(effect.skillId)
                }

                is SkillListEffect.NavigateToProfile -> {
                    onProfileClick()
                }

                is SkillListEffect.ShowError -> {
                    // Handle error - could show snackbar
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "JetCode Learning", fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = { viewModel.handleIntent(SkillListIntent.ProfileClicked) },
                ) {
                    Icon(
                        imageVector = Icons.Default.Person, contentDescription = "Profile"
                    )
                }
            },
        )

        when {
            state.error != null && state.skills.isEmpty() -> {
                ErrorState(
                    message = state.error ?: "Unknown error",
                    onRetry = { viewModel.handleIntent(SkillListIntent.RetryClicked) },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LearningContent(
    state: SkillListState,
    onIntent: (SkillListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { query ->
                onIntent(SkillListIntent.SearchSkills(query))
            },
            label = { Text("Search skills...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true,
        )

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = state.isLoading,
            onRefresh = { onIntent(SkillListIntent.SyncSkills) },
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
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
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            )
                        }
                    }
                } else {
                    items(
                        items = state.filteredSkills, key = { it.id }) { skill ->
                        SkillCard(
                            skill = skill,
                            onClick = {
                                onIntent(SkillListIntent.SkillClicked(skill.id))
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SkillCard(
    skill: Skill, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = skill.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = skill.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                DifficultyChip(difficulty = skill.difficulty)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = { .33f },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp),
                    color = JetCodeTheme.extendedColors.progressInProgress,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "33%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SkillListScreenPreview() {
    val sampleSkills = listOf(
        Skill(
            id = "1",
            name = "Kotlin Fundamentals",
            description = "Learn the basics of Kotlin programming language including variables, functions, and classes.",
            difficulty = Difficulty.BEGINNER,
            iconUrl = "https://example.com/kotlin-icon.png",
            estimatedDuration = 120,
        ),
        Skill(
            id = "2",
            name = "Jetpack Compose",
            description = "Master modern Android UI development with Jetpack Compose declarative toolkit.",
            difficulty = Difficulty.INTERMEDIATE,
            iconUrl = "https://example.com/compose-icon.png",
            estimatedDuration = 240,
        ),
        Skill(
            id = "3",
            name = "Coroutines & Flow",
            description = "Advanced asynchronous programming patterns in Kotlin for Android development.",
            difficulty = Difficulty.ADVANCED,
            iconUrl = "https://example.com/coroutines-icon.png",
            estimatedDuration = 360,
        ),
        Skill(
            id = "4",
            name = "Room Database",
            description = "Local data persistence using Room database with SQL queries and migrations.",
            difficulty = Difficulty.INTERMEDIATE,
            iconUrl = "https://example.com/room-icon.png",
            estimatedDuration = 180,
        ),
    )

    val sampleState = SkillListState(
        skills = sampleSkills,
        filteredSkills = sampleSkills,
        searchQuery = "",
        isLoading = false,
        error = null,
    )

    JetCodeTheme {
        LearningContent(
            state = sampleState, onIntent = { },
            modifier = Modifier.fillMaxSize(),
        )
    }
}
