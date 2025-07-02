package com.appsbase.jetcode.feature.learning.presentation.skilldetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.ui.components.DifficultyChip
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Skill Detail Screen - Shows detailed information about a specific skill with real data
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillDetailScreen(
    skillId: String,
    onLessonClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SkillDetailViewModel = koinViewModel { parametersOf(skillId) }
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle side effects
    LaunchedEffect(skillId) {
        viewModel.handleIntent(SkillDetailIntent.LoadSkill(skillId))
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SkillDetailEffect.NavigateToTopic -> {
                    onLessonClick(effect.topicId)
                }
                is SkillDetailEffect.ShowError -> {
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
                    text = state.skill?.name ?: "Skill Detail",
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        // Content
        when {
            state.isLoading -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize(),
                    message = "Loading skill details..."
                )
            }

            state.error != null && state.skill == null -> {
                ErrorState(
                    message = state.error ?: "Unknown error",
                    onRetry = { viewModel.handleIntent(SkillDetailIntent.RetryClicked) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.skill != null -> {
                SkillDetailContent(
                    state = state,
                    onTopicClick = { topicId ->
                        viewModel.handleIntent(SkillDetailIntent.TopicClicked(topicId))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun SkillDetailContent(
    state: SkillDetailState,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val skill = state.skill ?: return

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Skill Overview Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = skill.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = skill.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }

                        DifficultyChip(difficulty = skill.difficulty)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${(skill.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { skill.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoChip(
                            label = "Duration",
                            value = "${skill.estimatedDuration} min"
                        )
                        InfoChip(
                            label = "Topics",
                            value = "${state.topics.size}"
                        )
                        InfoChip(
                            label = "Status",
                            value = if (skill.isCompleted) "Completed" else "In Progress"
                        )
                    }
                }
            }
        }

        // Topics Section
        item {
            Text(
                text = "Topics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        if (state.topics.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No topics available yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        } else {
            items(
                items = state.topics,
                key = { it.id }
            ) { topic ->
                TopicCard(
                    topic = topic,
                    onClick = { onTopicClick(topic.id) }
                )
            }
        }
    }
}

@Composable
private fun TopicCard(
    topic: com.appsbase.jetcode.core.domain.model.Topic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topic.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = topic.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Surface(
                    color = if (topic.isCompleted) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else if (topic.isUnlocked) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = when {
                            topic.isCompleted -> "✓"
                            topic.isUnlocked -> "→"
                            else -> "🔒"
                        },
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            if (topic.isUnlocked) {
                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { topic.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
