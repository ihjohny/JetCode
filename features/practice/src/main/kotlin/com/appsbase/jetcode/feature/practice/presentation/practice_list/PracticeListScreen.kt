package com.appsbase.jetcode.feature.practice.presentation.practice_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.ui.components.CommonTopAppBar
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.domain.model.PracticeSessionStatistics
import com.appsbase.jetcode.domain.model.PracticeSet
import com.appsbase.jetcode.domain.model.PracticeSetResult
import com.appsbase.jetcode.domain.model.UserPracticeSet
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Practice List Screen - Shows incomplete and completed practice sets in tabs
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeListScreen(
    onPracticeClick: (String) -> Unit,
    onBackClick: () -> Unit,
    initialSelectedTab: PracticeTab = PracticeTab.INCOMPLETE,
    viewModel: PracticeListViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PracticeListEffect.NavigateToPractice -> onPracticeClick(effect.practiceSetId)
                is PracticeListEffect.ShowError -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(PracticeListIntent.LoadPracticeSets)
    }

    LaunchedEffect(initialSelectedTab) {
        viewModel.handleIntent(PracticeListIntent.TabChanged(initialSelectedTab))
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = "Practice", onNavigateBack = onBackClick
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
                onRetry = { viewModel.handleIntent(PracticeListIntent.Retry) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )

            else -> PracticeListContent(
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
private fun PracticeListContent(
    state: PracticeListState,
    onIntent: (PracticeListIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Custom Tab Row
        PracticeTabRow(
            selectedTab = state.selectedTab,
            incompleteCount = state.incompletePracticeSets.size,
            completedCount = state.completedPracticeSets.size,
            onTabSelected = { onIntent(PracticeListIntent.TabChanged(it)) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Practice Sets List
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            when (state.selectedTab) {
                PracticeTab.INCOMPLETE -> IncompletePracticeList(
                    practiceSets = state.incompletePracticeSets,
                    onPracticeClick = { onIntent(PracticeListIntent.PracticeSetClicked(it)) },
                    modifier = Modifier.weight(1f)
                )

                PracticeTab.COMPLETED -> CompletedPracticeList(
                    practiceSets = state.completedPracticeSets,
                    onPracticeClick = { onIntent(PracticeListIntent.PracticeSetClicked(it)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PracticeTabRow(
    selectedTab: PracticeTab,
    incompleteCount: Int,
    completedCount: Int,
    onTabSelected: (PracticeTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        TabRow(
            selectedTabIndex = when (selectedTab) {
                PracticeTab.INCOMPLETE -> 0
                PracticeTab.COMPLETED -> 1
            },
            modifier = Modifier.padding(4.dp),
            containerColor = Color.Transparent,
            divider = {},
            indicator = {}) {
            Tab(
                selected = selectedTab == PracticeTab.INCOMPLETE,
                onClick = { onTabSelected(PracticeTab.INCOMPLETE) },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selectedTab == PracticeTab.INCOMPLETE) {
                            MaterialTheme.colorScheme.primary
                        } else Color.Transparent
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(
                        "To Practice", color = if (selectedTab == PracticeTab.INCOMPLETE) {
                            MaterialTheme.colorScheme.onPrimary
                        } else MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium
                    )
                    if (incompleteCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CountBadge(
                            count = incompleteCount,
                            isSelected = selectedTab == PracticeTab.INCOMPLETE
                        )
                    }
                }
            }

            Tab(
                selected = selectedTab == PracticeTab.COMPLETED,
                onClick = { onTabSelected(PracticeTab.COMPLETED) },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selectedTab == PracticeTab.COMPLETED) {
                            MaterialTheme.colorScheme.primary
                        } else Color.Transparent
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                ) {
                    Text(
                        "Completed", color = if (selectedTab == PracticeTab.COMPLETED) {
                            MaterialTheme.colorScheme.onPrimary
                        } else MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium
                    )
                    if (completedCount > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CountBadge(
                            count = completedCount,
                            isSelected = selectedTab == PracticeTab.COMPLETED
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountBadge(
    count: Int,
    isSelected: Boolean,
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                }, CircleShape
            ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun IncompletePracticeList(
    practiceSets: List<UserPracticeSet>,
    onPracticeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (practiceSets.isEmpty()) {
        EmptyState(
            message = "🎯 No practice sets available",
            subtitle = "Check back later for new challenges!",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(practiceSets) { userPracticeSet ->
                IncompletePracticeItem(
                    userPracticeSet = userPracticeSet,
                    onClick = { onPracticeClick(userPracticeSet.practiceSet.id) })
            }
        }
    }
}

@Composable
private fun CompletedPracticeList(
    practiceSets: List<UserPracticeSet>,
    onPracticeClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (practiceSets.isEmpty()) {
        EmptyState(
            message = "📝 No completed practice sets",
            subtitle = "Start practicing to see your achievements here!",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(practiceSets) { userPracticeSet ->
                CompletedPracticeItem(
                    userPracticeSet = userPracticeSet,
                    onClick = { onPracticeClick(userPracticeSet.practiceSet.id) },
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun IncompletePracticeItem(
    userPracticeSet: UserPracticeSet,
    onClick: () -> Unit,
) {
    val scale by animateFloatAsState(
        targetValue = 1f, animationSpec = tween(300), label = "scale"
    )

    Box(
        modifier = Modifier.padding(vertical = 6.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userPracticeSet.practiceSet.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = userPracticeSet.practiceSet.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoChip(
                            label = "${userPracticeSet.practiceSet.quizIds.size} Quizzes",
                            emoji = "📝"
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        ActionButton(
                            icon = Icons.Filled.PlayArrow,
                            onClick = onClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompletedPracticeItem(
    userPracticeSet: UserPracticeSet,
    onClick: () -> Unit,
) {
    val result = userPracticeSet.practiceSetResult!!
    val scale by animateFloatAsState(
        targetValue = 1f, animationSpec = tween(300), label = "scale"
    )

    Box(
        modifier = Modifier.padding(vertical = 6.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Content
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = userPracticeSet.practiceSet.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = userPracticeSet.practiceSet.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats Row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ScoreBadge(score = result.practiceSessionStatistics.scorePercentage)
                            InfoChip(
                                label = "${result.practiceSessionStatistics.correctAnswers}/${result.practiceSessionStatistics.totalQuizzes}",
                                emoji = "✅"
                            )
                            InfoChip(
                                label = result.practiceSessionStatistics.formattedAverageTime,
                                emoji = "⏱️"
                            )
                            InfoChip(
                                label = formatDate(result.updatedAt), emoji = "📅"
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            ActionButton(
                                icon = Icons.Filled.Refresh,
                                onClick = onClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoChip(
    label: String,
    emoji: String,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(emoji, style = MaterialTheme.typography.labelSmall)
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ScoreBadge(score: Int) {
    val color = when {
        score >= 80 -> Color(0xFF4CAF50)
        score >= 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(color),
    ) {
        Text(
            text = "$score%",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
private fun EmptyState(
    message: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

// Preview composables
@Preview(showBackground = true, name = "Practice List - Incomplete Tab")
@Composable
private fun PracticeListIncompletePreview() {
    JetCodeTheme {
        val samplePracticeSets = listOf(
            UserPracticeSet(
                practiceSet = PracticeSet(
                    id = "1",
                    name = "Kotlin Basics",
                    description = "Learn the fundamentals of Kotlin programming including variables, functions, and control structures.",
                    quizIds = listOf("q1", "q2", "q3", "q4", "q5")
                ), practiceSetResult = null
            ), UserPracticeSet(
                practiceSet = PracticeSet(
                    id = "2",
                    name = "Object-Oriented Programming",
                    description = "Master classes, objects, inheritance, and polymorphism in Kotlin.",
                    quizIds = listOf("q6", "q7", "q8")
                ), practiceSetResult = null
            )
        )

        PracticeListContent(
            state = PracticeListState(
                userPracticeSets = samplePracticeSets, selectedTab = PracticeTab.INCOMPLETE
            ), onIntent = {}, modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, name = "Practice List - Completed Tab")
@Composable
private fun PracticeListCompletedPreview() {
    JetCodeTheme {
        val samplePracticeSets = listOf(
            UserPracticeSet(
                practiceSet = PracticeSet(
                    id = "3",
                    name = "Advanced Functions",
                    description = "Deep dive into lambda expressions, higher-order functions, and inline functions.",
                    quizIds = listOf("q9", "q10", "q11", "q12")
                ), practiceSetResult = PracticeSetResult(
                    id = "r1",
                    userId = "user1",
                    practiceSetId = "3",
                    practiceSessionStatistics = PracticeSessionStatistics(
                        totalQuizzes = 4, correctAnswers = 3, averageTimeSeconds = 45.5f
                    ),
                    updatedAt = System.currentTimeMillis() - 86400000 // 1 day ago
                )
            ), UserPracticeSet(
                practiceSet = PracticeSet(
                    id = "4",
                    name = "Collections & Generics",
                    description = "Work with lists, sets, maps, and understand generic programming.",
                    quizIds = listOf("q13", "q14", "q15")
                ), practiceSetResult = PracticeSetResult(
                    id = "r2",
                    userId = "user1",
                    practiceSetId = "4",
                    practiceSessionStatistics = PracticeSessionStatistics(
                        totalQuizzes = 3, correctAnswers = 2, averageTimeSeconds = 60.0f
                    ),
                    updatedAt = System.currentTimeMillis() - 172800000 // 2 days ago
                )
            )
        )

        PracticeListContent(
            state = PracticeListState(
                userPracticeSets = samplePracticeSets, selectedTab = PracticeTab.COMPLETED
            ), onIntent = {}, modifier = Modifier.fillMaxSize()
        )
    }
}
