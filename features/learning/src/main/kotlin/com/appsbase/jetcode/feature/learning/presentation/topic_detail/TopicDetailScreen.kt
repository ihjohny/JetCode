package com.appsbase.jetcode.feature.learning.presentation.topic_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import org.koin.androidx.compose.koinViewModel

/**
 * Screen for displaying topic details with materials and practices
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    topicId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopicDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle effects
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TopicDetailEffect.NavigateBack -> onNavigateBack()
                is TopicDetailEffect.ShowError -> {
                    // Handle error display
                }

                is TopicDetailEffect.ShowCorrectAnswer -> {
                    // Show success feedback
                }

                is TopicDetailEffect.ShowIncorrectAnswer -> {
                    // Show incorrect answer feedback
                }

                is TopicDetailEffect.ShowTopicCompleted -> {
                    // Show completion dialog
                }
            }
        }
    }

    LaunchedEffect(topicId) {
        viewModel.handleIntent(TopicDetailIntent.LoadTopic(topicId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.topic?.name ?: "Loading...",
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        when {
            state.isLoading -> {
                LoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            !state.error.isNullOrEmpty() -> {
                ErrorState(
                    message = state.error ?: "Unknown error",
                    onRetry = {
                        viewModel.handleIntent(TopicDetailIntent.RetryClicked(topicId = topicId))
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }

            else -> {
                TopicContent(
                    state = state,
                    onIntent = viewModel::handleIntent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun TopicContent(
    state: TopicDetailState,
    onIntent: (TopicDetailIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Topic description
        state.topic?.let { topic ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = topic.description,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = "Duration: ${topic.duration} min",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Progress: ${(topic.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!state.isShowingPractice) {
            // Show materials
            MaterialSection(
                materials = state.materials,
                currentIndex = state.currentMaterialIndex,
                onNext = { onIntent(TopicDetailIntent.NextMaterial) },
                onPrevious = { onIntent(TopicDetailIntent.PreviousMaterial) })
        } else {
            // Show practices
            PracticeSection(
                practices = state.practices,
                currentIndex = state.currentPracticeIndex,
                onSubmitAnswer = { answer -> onIntent(TopicDetailIntent.SubmitAnswer(answer)) },
                onNext = { onIntent(TopicDetailIntent.NextPractice) },
                onPrevious = { onIntent(TopicDetailIntent.PreviousPractice) })
        }
    }
}

@Composable
private fun MaterialSection(
    materials: List<Material>,
    currentIndex: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (materials.isEmpty()) return

    val currentMaterial = materials[currentIndex]

    Column(modifier = modifier) {
        Text(
            text = "Learning Material (${currentIndex + 1}/${materials.size})",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = currentMaterial.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentMaterial.content,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Button(
                        onClick = onPrevious,
                        enabled = currentIndex > 0,
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = onNext,
                    ) {
                        Text(if (currentIndex < materials.size - 1) "Next" else "Start Practice")
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeSection(
    practices: List<com.appsbase.jetcode.core.domain.model.Practice>,
    currentIndex: Int,
    onSubmitAnswer: (String) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (practices.isEmpty()) return

    val currentPractice = practices[currentIndex]
    var selectedAnswer by remember(currentIndex) { mutableStateOf("") }

    Column(modifier = modifier) {
        Text(
            text = "Practice (${currentIndex + 1}/${practices.size})",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = currentPractice.question,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Show options for MCQ
                if (currentPractice.options.isNotEmpty()) {
                    currentPractice.options.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedAnswer == option,
                                onClick = { selectedAnswer = option })
                            Text(
                                text = option, modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                } else {
                    // Text input for code challenges
                    OutlinedTextField(
                        value = selectedAnswer,
                        onValueChange = { selectedAnswer = it },
                        label = { Text("Your answer") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onPrevious
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = { onSubmitAnswer(selectedAnswer) },
                        enabled = selectedAnswer.isNotEmpty()
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}
