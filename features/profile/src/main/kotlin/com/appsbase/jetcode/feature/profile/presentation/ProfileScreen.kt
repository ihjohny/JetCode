package com.appsbase.jetcode.feature.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.ui.components.CommonTopAppBar
import com.appsbase.jetcode.core.ui.components.DifficultyChip
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.domain.model.ActivityType
import com.appsbase.jetcode.domain.model.Difficulty
import com.appsbase.jetcode.domain.model.RecentActivity
import com.appsbase.jetcode.domain.model.SkillDifficultyStats
import com.appsbase.jetcode.domain.model.UserStatistics
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Profile Screen - User profile and statistics with comprehensive analytics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle effects
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        actionLabel = "Retry",
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = "Profile",
                onNavigateBack = onBackClick,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier,
    ) { paddingValues ->
        when {
            state.isLoading && state.userStatistics == null -> {
                LoadingState(
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            state.error != null && state.userStatistics == null -> {
                ErrorState(
                    message = state.error ?: "An unexpected error occurred",
                    onRetry = { viewModel.handleIntent(ProfileIntent.RetryLoad) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            else -> {
                ProfileContent(
                    state = state,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        state.userStatistics?.let { statistics ->
            // Overall Statistics Card
            OverallStatsCard(statistics = statistics)

            // Skills Progress Section
            SkillsProgressSection(statistics = statistics)

            // Practice Statistics Section
            PracticeStatsSection(statistics = statistics)

            // Recent Activity Section
            RecentActivitySection(activities = statistics.recentActivities)
        }
    }
}

@Composable
private fun OverallStatsCard(
    statistics: UserStatistics,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Overall Progress",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Skills Enrolled",
                    value = statistics.totalSkillsEnrolled.toString(),
                    icon = Icons.Default.School
                )
                StatItem(
                    label = "Skills Completed",
                    value = statistics.totalSkillsCompleted.toString(),
                    icon = Icons.Default.CheckCircle
                )
                StatItem(
                    label = "Topics Done",
                    value = statistics.totalTopicsCompleted.toString(),
                    icon = Icons.Default.Topic
                )
            }

            if (statistics.totalQuestions > 0) {
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(
                        label = "Accuracy",
                        value = "${statistics.overallAccuracy.toInt()}%",
                        icon = Icons.Default.Visibility
                    )
                    StatItem(
                        label = "Practice Sets",
                        value = statistics.totalPracticeSetsCompleted.toString(),
                        icon = Icons.Default.Quiz
                    )
                    StatItem(
                        label = "Avg Score",
                        value = "${statistics.averageScore.toInt()}%",
                        icon = Icons.AutoMirrored.Filled.TrendingUp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SkillsProgressSection(
    statistics: UserStatistics,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Skills by Difficulty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Difficulty.entries.forEach { difficulty ->
                val stats = statistics.skillsByDifficulty[difficulty] ?: SkillDifficultyStats()
                if (stats.total > 0) {
                    SkillDifficultyRow(
                        difficulty = difficulty, stats = stats
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillDifficultyRow(
    difficulty: Difficulty,
    stats: SkillDifficultyStats,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DifficultyChip(difficulty = difficulty)
                Text(
                    text = "${stats.completed}/${stats.total} completed",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (stats.running > 0) {
                Text(
                    text = "${stats.running} in progress",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (stats.total > 0) {
            LinearProgressIndicator(
                progress = { stats.completed.toFloat() / stats.total },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )
        }
    }
}

@Composable
private fun PracticeStatsSection(
    statistics: UserStatistics,
    modifier: Modifier = Modifier,
) {
    if (statistics.totalPracticeSetsCompleted == 0) return

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Practice Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PracticeStatCard(
                    title = "Total Questions",
                    value = statistics.totalQuestions.toString(),
                    modifier = Modifier.weight(1f)
                )
                PracticeStatCard(
                    title = "Correct Answers",
                    value = statistics.totalCorrectAnswers.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PracticeStatCard(
                    title = "Wrong Answers",
                    value = (statistics.totalQuestions - statistics.totalCorrectAnswers).toString(),
                    modifier = Modifier.weight(1f)
                )
                PracticeStatCard(
                    title = "Overall Accuracy",
                    value = "${statistics.overallAccuracy.toInt()}%",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PracticeStatCard(
    title: String, value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RecentActivitySection(
    activities: List<RecentActivity>,
    modifier: Modifier = Modifier,
) {
    if (activities.isEmpty()) return

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Recent Activity (Last 7 Days)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            activities.forEach { activity ->
                ActivityItem(activity = activity)
            }
        }
    }
}

@Composable
private fun ActivityItem(
    activity: RecentActivity,
    modifier: Modifier = Modifier,
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = when (activity.type) {
                ActivityType.SKILL_STARTED -> Icons.Default.PlayArrow
                ActivityType.SKILL_COMPLETED -> Icons.Default.CheckCircle
                ActivityType.TOPIC_COMPLETED -> Icons.Default.Topic
                ActivityType.PRACTICE_COMPLETED -> Icons.Default.Quiz
                ActivityType.MATERIAL_READ -> Icons.AutoMirrored.Filled.MenuBook
            }, contentDescription = null, tint = when (activity.type) {
                ActivityType.SKILL_COMPLETED -> MaterialTheme.colorScheme.primary
                ActivityType.PRACTICE_COMPLETED -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }, modifier = Modifier.size(20.dp)
        )

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = activity.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = activity.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = dateFormat.format(Date(activity.timestamp)),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
