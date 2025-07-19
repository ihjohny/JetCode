package com.appsbase.jetcode.feature.learning.presentation.lesson

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.MaterialType
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Lesson Screen - Shows lesson content and materials with real data
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    lessonId: String,
    onPracticeClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LessonViewModel = koinViewModel { parametersOf(lessonId) }
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle side effects
    LaunchedEffect(lessonId) {
        viewModel.handleIntent(LessonIntent.LoadLesson(lessonId))
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LessonEffect.NavigateToPractice -> {
                    onPracticeClick(effect.practiceId)
                }
                is LessonEffect.ShowError -> {
                    // Handle error - could show snackbar
                }
                is LessonEffect.ShowCompletionDialog -> {
                    // Show completion celebration
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
                    text = state.lesson?.title ?: "Lesson",
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
            },
            actions = {
                if (state.lesson != null && !state.isCompleted) {
                    IconButton(
                        onClick = { viewModel.handleIntent(LessonIntent.MarkAsCompleted) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Mark as Completed",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        )

        // Content
        when {
            state.isLoading -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize(),
                    message = "Loading lesson content..."
                )
            }

            state.error != null && state.lesson == null -> {
                ErrorState(
                    message = state.error ?: "Unknown error",
                    onRetry = { viewModel.handleIntent(LessonIntent.RetryClicked) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            state.lesson != null -> {
                LessonContent(
                    state = state,
                    onMaterialSelected = { index ->
                        viewModel.handleIntent(LessonIntent.MaterialSelected(index))
                    },
                    onPracticeClick = { practiceId ->
                        viewModel.handleIntent(LessonIntent.PracticeClicked(practiceId))
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun LessonContent(
    state: LessonState,
    onMaterialSelected: (Int) -> Unit,
    onPracticeClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val lesson = state.lesson ?: return

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Lesson Overview
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
                                text = lesson.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = lesson.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }

                        if (state.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoChip(
                            label = "Duration",
                            value = "${lesson.duration} min"
                        )
                        InfoChip(
                            label = "Materials",
                            value = "${state.materials.size}"
                        )
                        InfoChip(
                            label = "Practices",
                            value = "${state.practices.size}"
                        )
                    }
                }
            }
        }

        // Materials Section
        if (state.materials.isNotEmpty()) {
            item {
                Text(
                    text = "Learning Materials",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = state.materials,
                key = { it.id }
            ) { material ->
                MaterialCard(
                    material = material,
                    isSelected = state.materials.indexOf(material) == state.currentMaterialIndex,
                    onClick = {
                        onMaterialSelected(state.materials.indexOf(material))
                    }
                )
            }
        }

        // Current Material Content
        if (state.materials.isNotEmpty() && state.currentMaterialIndex < state.materials.size) {
            item {
                MaterialContentCard(
                    material = state.materials[state.currentMaterialIndex]
                )
            }
        }

        // Practice Section
        if (state.practices.isNotEmpty()) {
            item {
                Text(
                    text = "Practice Exercises",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(
                items = state.practices,
                key = { it.id }
            ) { practice ->
                PracticeCard(
                    practice = practice,
                    onClick = { onPracticeClick(practice.id) }
                )
            }
        }
    }
}

@Composable
private fun MaterialCard(
    material: Material,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (material.type) {
                    MaterialType.TEXT, MaterialType.MARKDOWN -> Icons.Default.ArrowBack // Use appropriate icons
                    MaterialType.CODE -> Icons.Default.ArrowBack
                    MaterialType.VIDEO -> Icons.Default.PlayArrow
                    MaterialType.IMAGE -> Icons.Default.ArrowBack
                },
                contentDescription = material.type.name,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = material.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                Text(
                    text = material.type.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    }
                )
            }
        }
    }
}

@Composable
private fun MaterialContentCard(
    material: Material,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = material.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (material.type) {
                MaterialType.TEXT, MaterialType.MARKDOWN -> {
                    Text(
                        text = material.content,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                MaterialType.CODE -> {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = material.content,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
                MaterialType.VIDEO, MaterialType.IMAGE -> {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${material.type.name} Content\n${material.title}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PracticeCard(
    practice: Practice,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = practice.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = practice.type.name,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "${practice.points} pts",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Start Practice",
                tint = MaterialTheme.colorScheme.primary
            )
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
