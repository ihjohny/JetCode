package com.appsbase.jetcode.feature.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Topic
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.ui.components.CommonTopAppBar
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.domain.model.ActivityType
import com.appsbase.jetcode.domain.model.RecentActivity
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
    Box(modifier = modifier) {
        // Background gradient
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val gradient = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF6366F1).copy(alpha = 0.05f),
                    Color(0xFF8B5CF6).copy(alpha = 0.02f),
                    Color.Transparent
                ), startY = 0f, endY = size.height * 0.4f
            )
            drawRect(gradient)
        }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            state.userStatistics?.let { statistics ->
                AnimatedVisibility(
                    visible = true, enter = slideInVertically(
                        initialOffsetY = { -40 }, animationSpec = spring()
                    ) + fadeIn()
                ) {
                    // Hero Section with circular progress
                    HeroStatsCard(statistics = statistics)
                }

                AnimatedVisibility(
                    visible = true, enter = slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = tween(durationMillis = 300, delayMillis = 100)
                    ) + fadeIn(animationSpec = tween(delayMillis = 100))
                ) {
                    // Quick Stats Grid
                    QuickStatsGrid(statistics = statistics)
                }

                if (statistics.totalPracticeSetsCompleted > 0) {
                    AnimatedVisibility(
                        visible = true, enter = slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = tween(durationMillis = 300, delayMillis = 300)
                        ) + fadeIn(animationSpec = tween(delayMillis = 300))
                    ) {
                        // Practice Statistics Section
                        EnhancedPracticeStatsSection(statistics = statistics)
                    }
                }

                if (statistics.recentActivities.isNotEmpty()) {
                    AnimatedVisibility(
                        visible = true, enter = slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = tween(durationMillis = 300, delayMillis = 400)
                        ) + fadeIn(animationSpec = tween(delayMillis = 400))
                    ) {
                        // Recent Activity Section
                        EnhancedRecentActivitySection(activities = statistics.recentActivities)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroStatsCard(
    statistics: UserStatistics,
    modifier: Modifier = Modifier,
) {
    val animatedProgress = remember { Animatable(0f) }
    val completionRate = if (statistics.totalSkillsEnrolled > 0) {
        statistics.totalSkillsCompleted.toFloat() / statistics.totalSkillsEnrolled
    } else 0f

    LaunchedEffect(statistics) {
        animatedProgress.animateTo(
            targetValue = completionRate,
            animationSpec = tween(durationMillis = 1500, easing = LinearEasing)
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Circular Progress with animated ring
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)
                ) {
                    CircularProgressIndicator(
                        progress = animatedProgress.value,
                        size = 120.dp,
                        strokeWidth = 8.dp,
                        backgroundColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        progressColor = MaterialTheme.colorScheme.primary
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${(animatedProgress.value * 100).toInt()}%",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Complete",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Achievement level
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = getAchievementLevel(statistics),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Main stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HeroStatItem(
                        value = statistics.totalSkillsCompleted.toString(),
                        label = "Skills\nCompleted",
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF10B981)
                    )
                    HeroStatItem(
                        value = statistics.totalTopicsCompleted.toString(),
                        label = "Topics\nStudied",
                        icon = Icons.Default.Topic,
                        color = Color(0xFF8B5CF6)
                    )
                    HeroStatItem(
                        value = if (statistics.totalQuestions > 0) "${statistics.overallAccuracy.toInt()}%" else "0%",
                        label = "Practice\nAccuracy",
                        icon = Icons.Default.Visibility,
                        color = Color(0xFF06B6D4)
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroStatItem(
    value: String,
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .size(48.dp)
                .background(
                    color.copy(alpha = 0.15f), CircleShape
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = MaterialTheme.typography.labelSmall.lineHeight
        )
    }
}

@Composable
private fun QuickStatsGrid(
    statistics: UserStatistics,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Analytics,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Learning Analytics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickStatCard(
                    title = "Enrolled",
                    value = statistics.totalSkillsEnrolled.toString(),
                    icon = Icons.Default.School,
                    gradient = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Practice Sets",
                    value = statistics.totalPracticeSetsCompleted.toString(),
                    icon = Icons.Default.Quiz,
                    gradient = listOf(Color(0xFFFF6B6B), Color(0xFFFFE66D)),
                    modifier = Modifier.weight(1f)
                )
            }

            if (statistics.totalQuestions > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickStatCard(
                        title = "Questions",
                        value = statistics.totalQuestions.toString(),
                        icon = Icons.AutoMirrored.Filled.TrendingUp,
                        gradient = listOf(Color(0xFF4ECDC4), Color(0xFF44A08D)),
                        modifier = Modifier.weight(1f)
                    )
                    QuickStatCard(
                        title = "Avg Score",
                        value = "${statistics.averageScore.toInt()}%",
                        icon = Icons.Default.EmojiEvents,
                        gradient = listOf(Color(0xFFF093FB), Color(0xFFF5576C)),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = 1f, animationSpec = spring(), label = "scale"
    )

    Card(
        modifier = modifier.scale(scale), colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ), shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(gradient), RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun EnhancedPracticeStatsSection(
    statistics: UserStatistics,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Practice Performance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EnhancedPracticeStatCard(
                    title = "Total Questions",
                    value = statistics.totalQuestions.toString(),
                    gradient = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
                    modifier = Modifier.weight(1f)
                )
                EnhancedPracticeStatCard(
                    title = "Correct Answers",
                    value = statistics.totalCorrectAnswers.toString(),
                    gradient = listOf(Color(0xFF10B981), Color(0xFF34D399)),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EnhancedPracticeStatCard(
                    title = "Wrong Answers",
                    value = (statistics.totalQuestions - statistics.totalCorrectAnswers).toString(),
                    gradient = listOf(Color(0xFFEF4444), Color(0xFFF87171)),
                    modifier = Modifier.weight(1f)
                )
                EnhancedPracticeStatCard(
                    title = "Overall Accuracy",
                    value = "${statistics.overallAccuracy.toInt()}%",
                    gradient = listOf(Color(0xFF8B5CF6), Color(0xFFA78BFA)),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EnhancedPracticeStatCard(
    title: String,
    value: String,
    gradient: List<Color>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(gradient), RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun EnhancedRecentActivitySection(
    activities: List<RecentActivity>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            activities.forEachIndexed { index, activity ->
                AnimatedVisibility(
                    visible = true, enter = slideInVertically(
                        initialOffsetY = { 40 },
                        animationSpec = tween(durationMillis = 300, delayMillis = index * 100)
                    ) + fadeIn(animationSpec = tween(delayMillis = index * 100))
                ) {
                    EnhancedActivityItem(activity = activity)
                }
            }
        }
    }
}

@Composable
private fun EnhancedActivityItem(
    activity: RecentActivity,
    modifier: Modifier = Modifier,
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()) }

    Card(
        modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ), shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .size(48.dp)
                    .background(
                        when (activity.type) {
                            ActivityType.SKILL_COMPLETED -> Color(0xFF10B981).copy(alpha = 0.15f)
                            ActivityType.PRACTICE_COMPLETED -> Color(0xFF8B5CF6).copy(alpha = 0.15f)
                            ActivityType.SKILL_STARTED -> Color(0xFF06B6D4).copy(alpha = 0.15f)
                            ActivityType.TOPIC_COMPLETED -> Color(0xFFF59E0B).copy(alpha = 0.15f)
                            ActivityType.MATERIAL_READ -> Color(0xFF6366F1).copy(alpha = 0.15f)
                        }, CircleShape
                    )
            ) {
                Icon(
                    imageVector = when (activity.type) {
                        ActivityType.SKILL_STARTED -> Icons.Default.PlayArrow
                        ActivityType.SKILL_COMPLETED -> Icons.Default.CheckCircle
                        ActivityType.TOPIC_COMPLETED -> Icons.Default.Topic
                        ActivityType.PRACTICE_COMPLETED -> Icons.Default.Quiz
                        ActivityType.MATERIAL_READ -> Icons.AutoMirrored.Filled.MenuBook
                    }, contentDescription = null, tint = when (activity.type) {
                        ActivityType.SKILL_COMPLETED -> Color(0xFF10B981)
                        ActivityType.PRACTICE_COMPLETED -> Color(0xFF8B5CF6)
                        ActivityType.SKILL_STARTED -> Color(0xFF06B6D4)
                        ActivityType.TOPIC_COMPLETED -> Color(0xFFF59E0B)
                        ActivityType.MATERIAL_READ -> Color(0xFF6366F1)
                    }, modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = dateFormat.format(Date(activity.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CircularProgressIndicator(
    progress: Float,
    size: Dp,
    strokeWidth: Dp,
    backgroundColor: Color,
    progressColor: Color,
    modifier: Modifier = Modifier,
) {
    val animatedSweepAngle by animateFloatAsState(
        targetValue = progress * 360f,
        animationSpec = tween(durationMillis = 1500),
        label = "sweepAngle"
    )

    Canvas(
        modifier = modifier.size(size)
    ) {
        val stroke = Stroke(
            width = strokeWidth.toPx(), cap = StrokeCap.Round
        )

        // Background circle
        drawCircle(
            color = backgroundColor,
            radius = size.toPx() / 2 - strokeWidth.toPx() / 2,
            style = stroke
        )

        // Progress arc
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = animatedSweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}

private fun getAchievementLevel(statistics: UserStatistics): String {
    val totalScore =
        statistics.totalSkillsCompleted * 10 + statistics.totalTopicsCompleted * 2 + statistics.totalCorrectAnswers
    return when {
        totalScore >= 1000 -> "Master Coder"
        totalScore >= 500 -> "Expert Developer"
        totalScore >= 200 -> "Advanced Learner"
        totalScore >= 100 -> "Skilled Programmer"
        totalScore >= 50 -> "Rising Developer"
        else -> "Code Rookie"
    }
}
