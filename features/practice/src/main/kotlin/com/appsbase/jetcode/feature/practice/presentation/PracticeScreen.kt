package com.appsbase.jetcode.feature.practice.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.domain.model.PracticeSet
import com.appsbase.jetcode.core.domain.model.Quiz
import com.appsbase.jetcode.core.domain.model.QuizType
import com.appsbase.jetcode.core.ui.components.CommonTopAppBar
import com.appsbase.jetcode.core.ui.components.CompletionCard
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.core.ui.components.NavigationControls
import com.appsbase.jetcode.core.ui.components.ProgressHeaderCard
import com.appsbase.jetcode.core.ui.components.SwipeableCard
import com.appsbase.jetcode.core.ui.components.TypeBadge
import com.appsbase.jetcode.feature.practice.presentation.components.AllAnswersDialog
import org.koin.androidx.compose.koinViewModel

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
            CommonTopAppBar(
                title = state.practiceSet?.name ?: "Loading...",
                onNavigateBack = onBackClick
            )
        },
    ) { paddingValues ->
        when {
            state.isLoading -> LoadingState(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )

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
            ProgressHeaderCard(
                headerLabel = practiceSet.description,
                progressLabel = state.progressLabel,
                progressValue = state.progressValue,
                modifier = Modifier.padding(16.dp),
            )
        }

        if (state.quizzes.isNotEmpty()) {
            QuizSection(
                state = state, onIntent = onIntent, modifier = Modifier.weight(1f)
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
                .padding(horizontal = 16.dp)
        ) {
            state.currentQuiz?.let { currentQuiz ->
                SwipeableCard(
                    dragOffset = dragOffset,
                    onDragOffsetChange = { dragOffset = it },
                    onSwipeLeft = { onIntent(PracticeIntent.NextQuiz) },
                    onSwipeRight = { onIntent(PracticeIntent.PreviousQuiz) },
                    modifier = Modifier.fillMaxSize()
                ) {
                    QuizContent(
                        quiz = currentQuiz,
                        userAnswer = state.userAnswer,
                        onAnswerChanged = { onIntent(PracticeIntent.AnswerChanged(it)) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
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
private fun QuizContent(
    quiz: Quiz,
    userAnswer: String,
    onAnswerChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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

@Composable
private fun QuizTypeBadge(type: QuizType) {
    val (text, emoji) = when (type) {
        QuizType.MCQ -> "Multiple Choice" to "📝"
        QuizType.CODE_CHALLENGE -> "Code Challenge" to "💻"
        QuizType.OUTPUT_PREDICTION -> "Output Prediction" to "🔍"
        QuizType.FILL_BLANK -> "Fill Blank" to "✏️"
    }

    TypeBadge(
        text = text,
        emoji = emoji,
    )
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
                            } else MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)) {
                    RadioButton(selected = userAnswer == option, onClick = null)
                    Text(
                        text = option, modifier = Modifier
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
private fun CompletionScreen(
    state: PracticeState,
    onIntent: (PracticeIntent) -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        state.practiceSet?.let { practiceSet ->
            ProgressHeaderCard(
                headerLabel = practiceSet.description,
                progressLabel = state.progressLabel,
                progressValue = state.progressValue,
                modifier = Modifier.padding(16.dp),
            )
        }

        CompletionCard(
            title = "🎉 Practice Completed!",
            subtitle = "Great job! You've completed all the practice questions.",
            actionButtonText = "View Answers",
            onActionClick = { onIntent(PracticeIntent.ViewAnswers) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            additionalContent = {
                Spacer(modifier = Modifier.height(24.dp))
                StatisticsCard(state.statistics)
            },
        )

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
            }
        }
    }

    if (state.showAllAnswers) {
        AllAnswersDialog(
            quizResults = state.quizResults, onDismiss = { onIntent(PracticeIntent.ViewAnswers) })
    }
}

@Composable
private fun StatisticsCard(statistics: PracticeState.QuizStatistics) {
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
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
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

// Preview composable
@Preview(showBackground = true, name = "Practice Screen - MCQ Quiz")
@Composable
private fun PracticeScreenMCQPreview() {
    JetCodeTheme {
        val samplePracticeSet = PracticeSet(
            id = "1",
            name = "Kotlin Basics",
            description = "Learn the fundamentals of Kotlin programming language including variables, functions, and control structures.",
            quizIds = listOf("q1", "q2", "q3")
        )

        val sampleQuiz = Quiz(
            id = "q1",
            type = QuizType.MCQ,
            question = "Which of the following is the correct way to declare a variable in Kotlin?",
            options = listOf(
                "var name: String = \"John\"",
                "String name = \"John\"",
                "variable name = \"John\"",
                "def name = \"John\""
            ),
            correctAnswer = "var name: String = \"John\"",
            explanation = "In Kotlin, variables are declared using 'var' for mutable variables or 'val' for immutable variables."
        )

        PracticeContent(
            state = PracticeState(
                practiceSet = samplePracticeSet,
                quizzes = listOf(sampleQuiz),
                currentQuizIndex = 0,
                userAnswer = ""
            ), onIntent = {}, modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, name = "Practice Screen - Completed")
@Composable
private fun PracticeScreenCompletedPreview() {
    JetCodeTheme {
        val samplePracticeSet = PracticeSet(
            id = "3",
            name = "Kotlin Advanced",
            description = "Advanced Kotlin concepts including lambdas, higher-order functions, and coroutines.",
            quizIds = listOf("q1", "q2", "q3")
        )

        val sampleQuiz1 = Quiz(
            id = "q1",
            type = QuizType.MCQ,
            question = "What is a lambda expression?",
            options = listOf("A function", "A variable", "A class", "An interface"),
            correctAnswer = "A function",
            explanation = "Lambda expressions are anonymous functions."
        )

        val sampleQuiz2 = Quiz(
            id = "q2",
            type = QuizType.CODE_CHALLENGE,
            question = "Write a higher-order function",
            options = null,
            correctAnswer = "fun calculate(x: Int, operation: (Int) -> Int): Int = operation(x)",
            explanation = "Higher-order functions take functions as parameters."
        )

        val sampleResults = listOf(
            PracticeState.QuizResult(sampleQuiz1, "A function", true), PracticeState.QuizResult(
                sampleQuiz2,
                "fun calculate(x: Int, operation: (Int) -> Int): Int = operation(x)",
                true
            )
        )

        CompletionScreen(
            state = PracticeState(
                practiceSet = samplePracticeSet,
                quizzes = listOf(sampleQuiz1, sampleQuiz2),
                currentQuizIndex = 2,
                quizResults = sampleResults,
                isCompleted = true
            ),
            onIntent = {},
            onComplete = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
