package com.appsbase.jetcode.feature.practice.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.domain.model.Quiz
import com.appsbase.jetcode.core.domain.model.QuizType
import com.appsbase.jetcode.feature.practice.presentation.PracticeState

@Composable
internal fun AllAnswersDialog(
    quizResults: List<PracticeState.QuizResult>,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false,
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                DialogHeader(onDismiss = onDismiss)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    itemsIndexed(quizResults) { index, result ->
                        QuizAnswerItem(
                            index = index + 1, quiz = result.quiz, quizResult = result
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogHeader(onDismiss: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "All Answers",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    "Close",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        )
    }
}

@Composable
private fun QuizAnswerItem(
    index: Int,
    quiz: Quiz,
    quizResult: PracticeState.QuizResult,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (quizResult.isCorrect) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            } else {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            }
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$index.",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = if (quizResult.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (quizResult.isCorrect) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = quiz.question,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnswerSection(
                userAnswer = quizResult.userAnswer,
                correctAnswer = quiz.correctAnswer,
                explanation = quiz.explanation
            )
        }
    }
}

@Composable
private fun AnswerSection(
    userAnswer: String, correctAnswer: String, explanation: String?
) {
    Text(
        text = "Your answer: $userAnswer",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
    )

    Text(
        text = "Correct answer: $correctAnswer",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.Medium,
    )

    if (!explanation.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = explanation,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
        )
    }
}

// Preview composable
@Preview(showBackground = true, name = "All Answers Dialog - Mixed Results")
@Composable
private fun AllAnswersDialogPreview() {
    JetCodeTheme {
        val sampleQuiz1 = Quiz(
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
            explanation = "In Kotlin, variables are declared using 'var' for mutable variables or 'val' for immutable variables.",
        )

        val sampleQuiz2 = Quiz(
            id = "q2",
            type = QuizType.CODE_CHALLENGE,
            question = "Write a function that takes two integers and returns their sum:",
            options = null,
            correctAnswer = "fun sum(a: Int, b: Int): Int {\n    return a + b\n}",
            explanation = "Functions in Kotlin are declared using the 'fun' keyword followed by the function name, parameters, and return type.",
        )

        val sampleQuiz3 = Quiz(
            id = "q3",
            type = QuizType.OUTPUT_PREDICTION,
            question = "What will be the output of the following code?\n\nval x = 5\nval y = 10\nprintln(x + y)",
            options = null,
            correctAnswer = "15",
            explanation = "The code adds two integers (5 + 10) and prints the result.",
        )

        val sampleQuiz4 = Quiz(
            id = "q4",
            type = QuizType.FILL_BLANK,
            question = "Complete the code: val numbers = listOf(1, 2, 3, 4, 5)\nval doubled = numbers._____ { it * 2 }",
            options = null,
            correctAnswer = "map",
            explanation = "The map function transforms each element of a collection using the provided lambda.",
        )

        val sampleResults = listOf(
            PracticeState.QuizResult(
                quiz = sampleQuiz1,
                userAnswer = "var name: String = \"John\"",
                isCorrect = true,
                timeTaken = 15000L,
            ),
            PracticeState.QuizResult(
                quiz = sampleQuiz2,
                userAnswer = "fun sum(a: Int, b: Int) = a + b",
                isCorrect = false,
                timeTaken = 45000L,
            ),
            PracticeState.QuizResult(
                quiz = sampleQuiz3,
                userAnswer = "15",
                isCorrect = true,
                timeTaken = 8000L,
            ),
            PracticeState.QuizResult(
                quiz = sampleQuiz4,
                userAnswer = "filter",
                isCorrect = false,
                timeTaken = 25000L,
            ),
        )

        AllAnswersDialog(
            quizResults = sampleResults,
            onDismiss = {},
        )
    }
}
