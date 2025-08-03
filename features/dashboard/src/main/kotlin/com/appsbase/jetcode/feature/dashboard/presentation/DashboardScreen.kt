package com.appsbase.jetcode.feature.dashboard.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.ui.components.DifficultyChip
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.domain.model.Difficulty
import com.appsbase.jetcode.domain.model.PracticeSet
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.UserPracticeSet
import com.appsbase.jetcode.domain.model.UserSkill
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Dashboard Screen - Creative home screen for JetCode app
 * Showcases learning progress, practice activities, and user achievements
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onSkillClick: (String) -> Unit,
    onPracticeClick: (String) -> Unit,
    onViewAllSkillsClick: () -> Unit,
    onViewAllPracticeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DashboardEffect.NavigateToSkill -> onSkillClick(effect.skillId)
                is DashboardEffect.NavigateToPractice -> onPracticeClick(effect.practiceSetId)
                is DashboardEffect.NavigateToSkillsList -> onViewAllSkillsClick()
                is DashboardEffect.NavigateToPracticeList -> onViewAllPracticeClick()
                is DashboardEffect.NavigateToProfile -> onProfileClick()
                is DashboardEffect.ShowError -> {
                    // Handle error display if needed
                }
            }
        }
    }

    Scaffold(
        modifier = modifier, topBar = {
            DashboardTopBar(
                userName = state.userName,
                onProfileClick = { viewModel.handleIntent(DashboardIntent.ProfileClicked) })
        }) { paddingValues ->
        when {
            state.isLoading && state.userSkills.isEmpty() -> {
                LoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            !state.error.isNullOrEmpty() && state.userSkills.isEmpty() -> {
                ErrorState(
                    message = state.error ?: "Something went wrong",
                    onRetry = { viewModel.handleIntent(DashboardIntent.RetryClicked) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            else -> {
                DashboardContent(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(
    userName: String, onProfileClick: () -> Unit
) {
    TopAppBar(title = {
        Column {
            Text(
                text = getCurrentGreeting(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }, actions = {
        IconButton(onClick = onProfileClick) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardContent(
    state: DashboardState, onIntent: (DashboardIntent) -> Unit, modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = state.isLoading,
        onRefresh = { onIntent(DashboardIntent.RefreshDashboard) },
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Continue Learning Section
            if (state.inProgressSkills.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Continue Learning",
                        emoji = "📚",
                        actionText = "View All",
                        onActionClick = { onIntent(DashboardIntent.ViewAllSkillsClicked) })
                }
                item {
                    ContinueLearningSection(
                        inProgressSkills = state.inProgressSkills,
                        onSkillClick = { onIntent(DashboardIntent.SkillClicked(it)) })
                }
            }

            // Featured Skills Section
            if (state.featuredSkills.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Featured Skills",
                        emoji = "⭐",
                        actionText = "Explore All",
                        onActionClick = { onIntent(DashboardIntent.ViewAllSkillsClicked) })
                }
                item {
                    FeaturedSkillsSection(
                        featuredSkills = state.featuredSkills,
                        onSkillClick = { onIntent(DashboardIntent.SkillClicked(it)) })
                }
            }

            // Practice Challenges Section
            if (state.recentPracticeSets.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Practice Challenges",
                        emoji = "🎯",
                        actionText = "View All",
                        onActionClick = { onIntent(DashboardIntent.ViewAllPracticeClicked) })
                }
                item {
                    PracticeChallengesSection(
                        practiceSets = state.recentPracticeSets,
                        onPracticeClick = { onIntent(DashboardIntent.PracticeClicked(it)) })
                }
            }

            // Recent Achievements
            if (state.completedPracticeSets.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Recent Achievements",
                        emoji = "🏆",
                        actionText = "View History",
                        onActionClick = { onIntent(DashboardIntent.ViewAllPracticeClicked) })
                }
                item {
                    RecentAchievementsSection(
                        completedPracticeSets = state.completedPracticeSets,
                        onPracticeClick = { onIntent(DashboardIntent.PracticeClicked(it)) })
                }
            }

            // Get Started Section (if no progress)
            if (state.nextSkillToStart != null && state.inProgressSkills.isEmpty()) {
                item {
                    GetStartedSection(
                        nextSkill = state.nextSkillToStart!!,
                        onSkillClick = { onIntent(DashboardIntent.SkillClicked(it)) })
                }
            }
        }
    }
}

@Composable
private fun StatisticsOverview(
    totalSkillsCompleted: Int, totalPracticeCompleted: Int, currentStreak: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ), shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    icon = Icons.Outlined.School,
                    value = totalSkillsCompleted.toString(),
                    label = "Skills\nCompleted",
                    color = MaterialTheme.colorScheme.primary
                )
                StatisticItem(
                    icon = Icons.Outlined.Quiz,
                    value = totalPracticeCompleted.toString(),
                    label = "Practice\nCompleted",
                    color = MaterialTheme.colorScheme.secondary
                )
                StatisticItem(
                    icon = Icons.Default.Star,
                    value = currentStreak.toString(),
                    label = "Day\nStreak",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun StatisticItem(
    icon: ImageVector, value: String, label: String, color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
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
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun SectionHeader(
    title: String, emoji: String, actionText: String, onActionClick: () -> Unit
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
            Text(emoji, style = MaterialTheme.typography.titleLarge)
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedButton(
            onClick = onActionClick,
            modifier = Modifier.height(32.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            Text(
                text = actionText, style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun ContinueLearningSection(
    inProgressSkills: List<UserSkill>, onSkillClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(inProgressSkills.take(5)) { userSkill ->
            ContinueLearningCard(
                userSkill = userSkill, onClick = { onSkillClick(userSkill.skill.id) })
        }
    }
}

@Composable
private fun ContinueLearningCard(
    userSkill: UserSkill, onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f, animationSpec = tween(300), label = "scale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(280.dp)
            .scale(scale),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
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
                        text = userSkill.skill.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = userSkill.skill.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                DifficultyChip(difficulty = userSkill.skill.difficulty)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${userSkill.progressPercentageValue}% Complete",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Continue",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { userSkill.progressValue },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun FeaturedSkillsSection(
    featuredSkills: List<UserSkill>, onSkillClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(featuredSkills) { userSkill ->
            FeaturedSkillCard(
                userSkill = userSkill, onClick = { onSkillClick(userSkill.skill.id) })
        }
    }
}

@Composable
private fun FeaturedSkillCard(
    userSkill: UserSkill, onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = userSkill.skill.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                DifficultyChip(difficulty = userSkill.skill.difficulty)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${userSkill.skill.estimatedDuration} min",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun PracticeChallengesSection(
    practiceSets: List<UserPracticeSet>, onPracticeClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        practiceSets.take(2).forEach { userPracticeSet ->
            PracticeChallengeCard(
                userPracticeSet = userPracticeSet,
                onClick = { onPracticeClick(userPracticeSet.practiceSet.id) })
        }
    }
}

@Composable
private fun PracticeChallengeCard(
    userPracticeSet: UserPracticeSet, onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer, CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Quiz,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userPracticeSet.practiceSet.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${userPracticeSet.practiceSet.quizIds.size} questions",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
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
private fun RecentAchievementsSection(
    completedPracticeSets: List<UserPracticeSet>, onPracticeClick: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(completedPracticeSets.take(3)) { userPracticeSet ->
            AchievementCard(
                userPracticeSet = userPracticeSet,
                onClick = { onPracticeClick(userPracticeSet.practiceSet.id) })
        }
    }
}

@Composable
private fun AchievementCard(
    userPracticeSet: UserPracticeSet, onClick: () -> Unit
) {
    val result = userPracticeSet.practiceSetResult!!

    Card(
        onClick = onClick,
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🏆", style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = userPracticeSet.practiceSet.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${result.practiceSessionStatistics.scorePercentage}%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = formatDate(result.updatedAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun GetStartedSection(
    nextSkill: UserSkill, onSkillClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🚀", style = MaterialTheme.typography.displaySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ready to start your coding journey?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Begin with ${nextSkill.skill.name}",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { onSkillClick(nextSkill.skill.id) }) {
                Text("Start Learning")
            }
        }
    }
}

@Composable
private fun OnboardingPrompt(
    onStartOnboarding: () -> Unit, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(24.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👋", style = MaterialTheme.typography.displayMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Welcome to JetCode!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Let's get you started with a quick setup",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = onStartOnboarding
                ) {
                    Text("Get Started")
                }
            }
        }
    }
}

private fun getCurrentGreeting(): String {
    val calendar = java.util.Calendar.getInstance()
    return when (calendar.get(java.util.Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        else -> "Good evening"
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@Preview(showBackground = true, name = "Dashboard Screen")
@Composable
private fun DashboardScreenPreview() {
    JetCodeTheme {
        val sampleSkills = listOf(
            UserSkill(
                skill = Skill(
                    id = "1",
                    name = "Kotlin Basics",
                    description = "Learn the fundamentals of Kotlin programming",
                    iconUrl = null,
                    difficulty = Difficulty.BEGINNER,
                    estimatedDuration = 120
                ),
                completedMaterial = 2,
                totalMaterial = 5,
            ), UserSkill(
                skill = Skill(
                    id = "2",
                    name = "Object-Oriented Programming",
                    description = "Master OOP concepts in Kotlin",
                    iconUrl = null,
                    difficulty = Difficulty.INTERMEDIATE,
                    estimatedDuration = 180
                ),
                completedMaterial = 0,
                totalMaterial = 8,
            )
        )

        val samplePracticeSets = listOf(
            UserPracticeSet(
                practiceSet = PracticeSet(
                    id = "1",
                    name = "Kotlin Fundamentals Quiz",
                    description = "Test your knowledge of Kotlin basics",
                    quizIds = listOf("q1", "q2", "q3")
                ),
                practiceSetResult = null,
            )
        )

        DashboardContent(
            state = DashboardState(
                userSkills = sampleSkills,
                userPracticeSets = samplePracticeSets,
                userName = "Alex",
            ),
            onIntent = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
