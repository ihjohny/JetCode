package com.appsbase.jetcode.feature.learning.presentation.topic_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.MaterialType
import com.appsbase.jetcode.core.domain.model.Topic
import com.appsbase.jetcode.core.ui.components.CommonTopAppBar
import com.appsbase.jetcode.core.ui.components.CompletionCard
import com.appsbase.jetcode.core.ui.components.DropdownMenuItem
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.core.ui.components.NavigationControls
import com.appsbase.jetcode.core.ui.components.ProgressHeaderCard
import com.appsbase.jetcode.core.ui.components.SwipeableCard
import com.appsbase.jetcode.core.ui.components.TypeBadge
import org.koin.androidx.compose.koinViewModel

/**
 * Screen for displaying topic details with materials
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    topicId: String,
    onNavigateBack: () -> Unit,
    onFinishClick: (String) -> Unit,
    onPracticeClick: (String) -> Unit,
    viewModel: TopicDetailViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TopicDetailEffect.NavigateBack -> onNavigateBack()
                is TopicDetailEffect.ShowError -> {}
                is TopicDetailEffect.NavigateToPractice -> onPracticeClick(effect.practiceSetId)
            }
        }
    }

    LaunchedEffect(topicId) {
        viewModel.handleIntent(TopicDetailIntent.LoadTopic(topicId))
    }

    val dropdownMenuItems = listOf(
        DropdownMenuItem(
            text = "Start Practice",
            onClick = {
                state.topic?.practiceSetId?.let { practiceSetId ->
                    onPracticeClick(practiceSetId)
                }
            }
        )
    )

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = state.topic?.name ?: "Loading...",
                onNavigateBack = onNavigateBack,
                dropdownMenuItems = dropdownMenuItems
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
                TopicContentSection(
                    state = state,
                    onIntent = viewModel::handleIntent,
                    onPracticeClick = {
                        state.topic?.practiceSetId?.let { practiceSetId ->
                            onPracticeClick(practiceSetId)
                        }
                    },
                    onFinishClick = {
                        state.topic?.id?.let { topicId ->
                            onFinishClick(topicId)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun TopicContentSection(
    state: TopicDetailState,
    onIntent: (TopicDetailIntent) -> Unit,
    onPracticeClick: () -> Unit,
    onFinishClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Topic Header Card
        state.topic?.let { topic ->
            ProgressHeaderCard(
                headerLabel = topic.description,
                progressLabel = state.progressValueLabel,
                progressValue = if (state.materials.isNotEmpty())
                    (state.currentMaterialIndex + 1f) / state.materials.size else 0f,
                extraInfo = "⏱️ ${topic.duration} min",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
            )
        }

        // Materials Section
        if (state.materials.isNotEmpty()) {
            MaterialFlashCardSection(
                materials = state.materials,
                currentIndex = state.currentMaterialIndex,
                onNext = { onIntent(TopicDetailIntent.NextMaterial) },
                onPrevious = { onIntent(TopicDetailIntent.PreviousMaterial) },
                onPracticeClick = onPracticeClick,
                onFinishClick = onFinishClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MaterialFlashCardSection(
    materials: List<Material>,
    currentIndex: Int,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onPracticeClick: () -> Unit,
    onFinishClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Column(modifier = modifier) {
        // Flash Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            if (currentIndex >= materials.size) {
                // Completion View
                CompletionCard(
                    title = "🎉 Well Done!",
                    subtitle = "You've completed all materials for this topic. Ready to practice what you've learned?",
                    actionButtonText = "Start Practice",
                    onActionClick = onPracticeClick,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                val currentMaterial = materials[currentIndex]

                SwipeableCard(
                    dragOffset = dragOffset,
                    onDragOffsetChange = { dragOffset = it },
                    onSwipeLeft = onNext,
                    onSwipeRight = onPrevious,
                    modifier = Modifier.fillMaxSize()
                ) {
                    MaterialContent(
                        material = currentMaterial,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
        }

        // Navigation Controls
        NavigationControls(
            canGoPrevious = currentIndex > 0,
            onPrevious = onPrevious,
            onNext = onNext,
            nextButtonText = "Next",
            showFinishButton = currentIndex >= materials.size,
            onFinish = if (currentIndex >= materials.size) onFinishClick else null,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun MaterialContent(
    material: Material,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Material Type Badge
        MaterialTypeBadge(type = material.type)

        Spacer(modifier = Modifier.height(16.dp))

        // Material Title
        Text(
            text = material.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Material Content based on type
        MaterialContentRenderer(material = material)
    }
}

@Composable
private fun MaterialTypeBadge(type: MaterialType) {
    val (text, emoji) = when (type) {
        MaterialType.TEXT -> "Text" to "📝"
        MaterialType.MARKDOWN -> "Markdown" to "📄"
        MaterialType.CODE -> "Code" to "💻"
        MaterialType.IMAGE -> "Image" to "🖼️"
        MaterialType.VIDEO -> "Video" to "🎥"
    }

    TypeBadge(
        text = text,
        emoji = emoji,
    )
}

@Composable
private fun MaterialContentRenderer(material: Material) {
    when (material.type) {
        MaterialType.TEXT, MaterialType.MARKDOWN -> {
            Text(
                text = material.content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4,
            )
        }

        MaterialType.CODE -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
            ) {
                Text(
                    text = material.content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }

        MaterialType.IMAGE -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "🖼️",
                        style = MaterialTheme.typography.displayMedium,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = material.content,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        MaterialType.VIDEO -> {
            Card(
                modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Video",
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                MaterialTheme.colorScheme.primary, CircleShape
                            )
                            .padding(12.dp)
                            .clickable { /* Handle video play */ },
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = material.content,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// Preview section
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TopicDetailScreenPreview() {
    JetCodeTheme {
        TopicContentSection(
            state = TopicDetailState(
                isLoading = false,
                topic = Topic(
                    id = "1",
                    name = "Kotlin Basics",
                    description = "Learn the fundamentals of Kotlin programming language including variables, functions, classes, and more.",
                    materialIds = listOf("1", "2", "3"),
                    practiceSetId = "practice_1",
                    duration = 45,
                ),
                materials = listOf(
                    Material(
                        id = "1",
                        type = MaterialType.TEXT,
                        title = "Introduction to Kotlin",
                        content = "Kotlin is a modern programming language that runs on the Java Virtual Machine (JVM). It's fully interoperable with Java and offers many features that make development more productive and enjoyable."
                    ),
                    Material(
                        id = "2",
                        type = MaterialType.CODE,
                        title = "Variables and Data Types",
                        content = """
                            // Declaring variables in Kotlin
                            val name: String = "John" // Immutable
                            var age: Int = 25 // Mutable
                            
                            // Type inference
                            val city = "New York"
                            var temperature = 23.5
                        """.trimIndent(),
                    ),
                    Material(
                        id = "3",
                        type = MaterialType.VIDEO,
                        title = "Kotlin Functions",
                        content = "Watch this comprehensive video about Kotlin functions, parameters, and return types.",
                    ),
                ),
                currentMaterialIndex = 0,
                error = null,
            ),
            onIntent = { },
            onPracticeClick = { },
            onFinishClick = { },
        )
    }
}
