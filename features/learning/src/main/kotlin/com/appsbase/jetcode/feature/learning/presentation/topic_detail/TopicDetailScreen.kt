package com.appsbase.jetcode.feature.learning.presentation.topic_detail

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.MaterialType
import com.appsbase.jetcode.core.domain.model.Topic
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

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

    Scaffold(
        topBar = {
            TopicDetailTopAppBar(
                title = state.topic?.name ?: "Loading...",
                onNavigateBack = onNavigateBack,
                onPracticeClick = {
                    state.topic?.practiceSetId?.let { practiceSetId ->
                        onPracticeClick(practiceSetId)
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
            TopicHeaderCard(
                topic = topic,
                currentMaterialIndex = state.currentMaterialIndex,
                totalMaterials = state.materials.size,
                progressValueLabel = state.progressValueLabel,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
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
private fun TopicHeaderCard(
    topic: Topic,
    currentMaterialIndex: Int,
    totalMaterials: Int,
    progressValueLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = topic.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                    Text(
                        text = progressValueLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = "⏱️ ${topic.duration} min",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { if (totalMaterials > 0) (currentMaterialIndex + 1f) / totalMaterials else 0f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
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
                    onPracticeClick = onPracticeClick,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                val currentMaterial = materials[currentIndex]

                MaterialCard(
                    material = currentMaterial,
                    dragOffset = dragOffset,
                    onDragOffsetChange = { dragOffset = it },
                    onSwipeLeft = onNext,
                    onSwipeRight = onPrevious,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Navigation Controls
        NavigationControls(
            canGoPrevious = currentIndex > 0,
            canGoNext = currentIndex < materials.size,
            isLastMaterial = currentIndex == materials.size - 1,
            isCompletionState = currentIndex >= materials.size,
            onPrevious = onPrevious,
            onNext = onNext,
            onFinish = onFinishClick,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun MaterialCard(
    material: Material,
    dragOffset: Float,
    onDragOffsetChange: (Float) -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedOffset by animateFloatAsState(
        targetValue = dragOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
        ),
        label = "cardOffset",
    )

    Card(
        modifier = modifier
            .offset { IntOffset(animatedOffset.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        when {
                            dragOffset > 300 -> onSwipeRight()
                            dragOffset < -300 -> onSwipeLeft()
                        }
                        onDragOffsetChange(0f)
                    },
                ) { _, dragAmount ->
                    onDragOffsetChange(dragOffset + dragAmount)
                }
            }
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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

    Row(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji, style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
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

@Composable
private fun CompletionCard(
    onPracticeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Completed",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "🎉 Well Done!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "You've completed all materials for this topic. Ready to practice what you've learned?",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onPracticeClick,
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start Practice",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NavigationControls(
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    isLastMaterial: Boolean,
    isCompletionState: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onPrevious, enabled = canGoPrevious, modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Previous")
        }

        Spacer(modifier = Modifier.width(16.dp))

        when {
            isCompletionState -> {
                Button(
                    onClick = onFinish,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Finish")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            else -> {
                Button(
                    onClick = onNext,
                    enabled = canGoNext || isLastMaterial,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicDetailTopAppBar(
    title: String,
    onNavigateBack: () -> Unit,
    onPracticeClick: () -> Unit,
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
        actions = {
            Box {
                IconButton(onClick = { showDropdownMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options",
                    )
                }

                DropdownMenu(
                    expanded = showDropdownMenu,
                    onDismissRequest = { showDropdownMenu = false },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Start Practice")
                        },
                        onClick = {
                            showDropdownMenu = false
                            onPracticeClick()
                        },
                    )
                }
            }
        },
    )
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
