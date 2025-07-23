package com.appsbase.jetcode.feature.practice.presentation

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.model.Quiz
import com.appsbase.jetcode.core.domain.model.QuizType
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.feature.practice.presentation.components.AllAnswersDialog
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

/**
 * Practice Screen - Interactive coding challenges and exercises
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    practiceId: String,
    onPracticeComplete: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: PracticeViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PracticeEffect.NavigateBack -> onBackClick()
                is PracticeEffect.ShowError -> {}
                is PracticeEffect.QuizCompleted -> {}
            }
        }
    }

    LaunchedEffect(practiceId) {
        viewModel.handleIntent(PracticeIntent.LoadPracticeSet(practiceId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.practiceSet?.name ?: "Loading...",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingState(Modifier
                .fillMaxSize()
                .padding(paddingValues))

            !state.error.isNullOrEmpty() -> ErrorState(
                message = state.error ?: "Unknown error",
                onRetry = { viewModel.handleIntent(PracticeIntent.RetryClicked(practiceId)) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            state.isCompleted -> CompletionScreen(
                state = state,
                onIntent = viewModel::handleIntent,
                onComplete = onPracticeComplete,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

            else -> PracticeContent(
                state = state,
                onIntent = viewModel::handleIntent,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }
    }
}

@Composable
private fun PracticeContent(
    state: PracticeState,
    onIntent: (PracticeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.practiceSet?.let { practiceSet ->
            PracticeHeaderCard(
                practiceSet = practiceSet,
                progressLabel = state.progressLabel,
                progressValue = state.progressValue,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (state.quizzes.isNotEmpty()) {
            QuizSection(
                state = state,
                onIntent = onIntent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PracticeHeaderCard(
    practiceSet: PracticeSet,
    progressLabel: String,
    progressValue: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = practiceSet.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Progress: $progressLabel",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progressValue },
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
private fun QuizSection(
    state: PracticeState,
    onIntent: (PracticeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            state.currentQuiz?.let { currentQuiz ->
                QuizCard(
                    quiz = currentQuiz,
                    userAnswer = state.userAnswer,
                    dragOffset = dragOffset,
                    onDragOffsetChange = { dragOffset = it },
                    onSwipeLeft = { onIntent(PracticeIntent.NextQuiz) },
                    onSwipeRight = { onIntent(PracticeIntent.PreviousQuiz) },
                    onAnswerChanged = { onIntent(PracticeIntent.AnswerChanged(it)) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        NavigationControls(
            canGoPrevious = state.currentQuizIndex > 0,
            onPrevious = { onIntent(PracticeIntent.PreviousQuiz) },
            onNext = { onIntent(PracticeIntent.NextQuiz) },
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun QuizCard(
    quiz: Quiz,
    userAnswer: String,
    dragOffset: Float,
    onDragOffsetChange: (Float) -> Unit,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onAnswerChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedOffset by animateFloatAsState(
        targetValue = dragOffset,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "quizCardOffset",
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
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
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
                QuizTypeBadge(quiz.type)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = quiz.question,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                QuizInput(quiz, userAnswer, onAnswerChanged)
            }
        }
    }
}

@Composable
private fun QuizTypeBadge(type: QuizType) {
    val (text, emoji) = when (type) {
        QuizType.MCQ -> "Multiple Choice" to "📝"
        QuizType.CODE_CHALLENGE -> "Code Challenge" to "💻"
        QuizType.OUTPUT_PREDICTION -> "Output Prediction" to "🔍"
        QuizType.FILL_BLANK -> "Fill Blank" to "✏️"
    }

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun QuizInput(
    quiz: Quiz,
    userAnswer: String,
    onAnswerChanged: (String) -> Unit,
) {
    when (quiz.type) {
        QuizType.MCQ -> {
            quiz.options?.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAnswerChanged(option) }
                        .background(
                            if (userAnswer == option) {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            } else MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                ) {
                    RadioButton(selected = userAnswer == option, onClick = null)
                    Text(
                        text = option,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        else -> {
            val label = when (quiz.type) {
                QuizType.CODE_CHALLENGE -> "Write your code here"
                QuizType.OUTPUT_PREDICTION -> "Predict the output"
                QuizType.FILL_BLANK -> "Fill in the blank"
                else -> "Your answer"
            }

            OutlinedTextField(
                value = userAnswer,
                onValueChange = onAnswerChanged,
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(),
                minLines = if (quiz.type == QuizType.CODE_CHALLENGE) 5 else 1,
                textStyle = if (quiz.type == QuizType.CODE_CHALLENGE) {
                    MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace)
                } else {
                    MaterialTheme.typography.bodyMedium
                }
            )
        }
    }
}

@Composable
private fun NavigationControls(
    canGoPrevious: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onPrevious,
            enabled = canGoPrevious,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Previous")
        }

        Button(
            onClick = onNext,
            modifier = Modifier.weight(1f)
        ) {
            Text("Next")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(20.dp))
        }
    }
}

@Composable
private fun CompletionScreen(
    state: PracticeState,
    onIntent: (PracticeIntent) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.practiceSet?.let { practiceSet ->
            PracticeHeaderCard(
                practiceSet = practiceSet,
                progressLabel = state.progressLabel,
                progressValue = state.progressValue,
                modifier = Modifier.padding(16.dp)
            )
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    "Completed",
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "🎉 Practice Completed!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                StatisticsCard(state.statistics)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onIntent(PracticeIntent.ToggleAllAnswers) },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Info, null, Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Answers", fontWeight = FontWeight.Bold)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { onIntent(PracticeIntent.RestartPractice) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Refresh, null, Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Restart")
            }

            Button(
                onClick = onComplete,
                modifier = Modifier.weight(1f),
            ) {
                Text("Finish", color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }

    if (state.showAllAnswers) {
        AllAnswersDialog(
            quizResults = state.quizResults,
            onDismiss = { onIntent(PracticeIntent.ToggleAllAnswers) }
        )
    }
}

@Composable
private fun StatisticsCard(statistics: QuizStatistics) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Results Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem("Score", "${statistics.scorePercentage}%", "🎯")
                StatisticItem("Correct", "${statistics.correctAnswers}", "✅")
                StatisticItem("Wrong", "${statistics.wrongAnswers}", "❌")
            }
        }
    }
}

@Composable
private fun StatisticItem(label: String, value: String, icon: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
