package com.appsbase.jetcode.feature.learning.presentation.skill_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.domain.model.Difficulty
import com.appsbase.jetcode.domain.model.SampleData
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.Topic
import com.appsbase.jetcode.core.ui.components.DifficultyChip
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import org.koin.androidx.compose.koinViewModel

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
    viewModel: SkillDetailViewModel = koinViewModel()
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
        TopAppBar(
            title = {
                Text(
                    text = state.skill?.name ?: "Skill Detail", fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            },
        )

        when {
            state.isLoading -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize(),
                    message = "Loading skill details...",
                )
            }

            state.error != null && state.skill == null -> {
                ErrorState(
                    message = state.error ?: "Unknown error",
                    onRetry = {
                        viewModel.handleIntent(SkillDetailIntent.RetryClicked(skillId = skillId))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }

            state.skill != null -> {
                SkillDetailContent(
                    state = state,
                    onTopicClick = { topicId ->
                        viewModel.handleIntent(SkillDetailIntent.TopicClicked(topicId))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun SkillDetailContent(
    state: SkillDetailState,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val skill = state.skill ?: return

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
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
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = skill.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = skill.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            )
                        }

                        DifficultyChip(difficulty = skill.difficulty)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Progress",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = "33%",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { .33f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        InfoChip(
                            label = "Duration",
                            value = "${skill.estimatedDuration} min",
                        )
                        InfoChip(
                            label = "Topics",
                            value = "${state.topics.size}",
                        )
                        InfoChip(
                            label = "Status", value = if (false) "Completed" else "In Progress"
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Topics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
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
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        )
                    }
                }
            }
        } else {
            items(
                items = state.topics,
                key = { it.id },
            ) { topic ->
                TopicCard(
                    topic = topic,
                    onClick = { onTopicClick(topic.id) },
                )
            }
        }
    }
}

@Composable
private fun TopicCard(
    topic: Topic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = topic.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = topic.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }

                Surface(
                    color = if (false) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        text = when {
                            false -> "✓"
                            else -> "🔒"
                        },
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { .33f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        )
    }
}

// Preview Data
private val mockSkill = Skill(
    id = "kotlin-basics",
    name = "Kotlin Fundamentals",
    description = "Learn the fundamentals of Kotlin programming language including variables, functions, classes, and control flow.",
    iconUrl = null,
    difficulty = Difficulty.BEGINNER,
    estimatedDuration = 120,
)

private val mockStateWithData = SkillDetailState(
    isLoading = false,
    skill = mockSkill,
    topics = SampleData.getSampleTopics(),
    error = null,
)

@Preview(showBackground = true)
@Composable
private fun SkillDetailContentPreview() {
    MaterialTheme {
        SkillDetailContent(
            state = mockStateWithData,
            onTopicClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SkillDetailContentEmptyTopicsPreview() {
    MaterialTheme {
        SkillDetailContent(
            state = mockStateWithData.copy(topics = emptyList()),
            onTopicClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
