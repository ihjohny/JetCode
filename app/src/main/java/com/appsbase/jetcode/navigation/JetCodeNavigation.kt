package com.appsbase.jetcode.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appsbase.jetcode.feature.learning.presentation.dashboard.LearningDashboardScreen
import com.appsbase.jetcode.feature.onboarding.presentation.OnboardingScreen

/**
 * Navigation routes for the JetCode app
 */
object JetCodeDestinations {
    const val ONBOARDING_ROUTE = "onboarding"
    const val HOME_ROUTE = "home"
    const val LEARNING_ROUTE = "learning"
    const val SKILL_DETAIL_ROUTE = "skill_detail"
    const val LESSON_ROUTE = "lesson"
    const val PRACTICE_ROUTE = "practice"
    const val PROFILE_ROUTE = "profile"
}

/**
 * Main navigation host for the JetCode app
 */
@Composable
fun JetCodeNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = JetCodeDestinations.HOME_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding flow - Now using actual implementation
        composable(JetCodeDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(JetCodeDestinations.HOME_ROUTE) {
                        popUpTo(JetCodeDestinations.ONBOARDING_ROUTE) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Home/Learning dashboard - Now using actual implementation
        composable(JetCodeDestinations.HOME_ROUTE) {
            LearningDashboardScreen(
                onSkillClick = { skillId ->
                    navController.navigate("${JetCodeDestinations.SKILL_DETAIL_ROUTE}/$skillId")
                },
                onProfileClick = {
                    navController.navigate(JetCodeDestinations.PROFILE_ROUTE)
                }
            )
        }

        // Skill detail with topics
        composable("${JetCodeDestinations.SKILL_DETAIL_ROUTE}/{skillId}") { backStackEntry ->
            val skillId = backStackEntry.arguments?.getString("skillId") ?: ""
            SkillDetailScreen(
                skillId = skillId,
                onLessonClick = { lessonId ->
                    navController.navigate("${JetCodeDestinations.LESSON_ROUTE}/$lessonId")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Lesson content
        composable("${JetCodeDestinations.LESSON_ROUTE}/{lessonId}") { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
            LessonScreen(
                lessonId = lessonId,
                onPracticeClick = { practiceId ->
                    navController.navigate("${JetCodeDestinations.PRACTICE_ROUTE}/$practiceId")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Practice exercises
        composable("${JetCodeDestinations.PRACTICE_ROUTE}/{practiceId}") { backStackEntry ->
            val practiceId = backStackEntry.arguments?.getString("practiceId") ?: ""
            PracticeScreen(
                practiceId = practiceId,
                onPracticeComplete = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // User profile
        composable(JetCodeDestinations.PROFILE_ROUTE) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

// Placeholder composables - these will be implemented in feature modules
@Composable
private fun SkillDetailScreen(
    skillId: String,
    onLessonClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    // Will be implemented in :feature:learning
}

@Composable
private fun LessonScreen(
    lessonId: String,
    onPracticeClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    // Will be implemented in :feature:learning
}

@Composable
private fun PracticeScreen(
    practiceId: String,
    onPracticeComplete: () -> Unit,
    onBackClick: () -> Unit
) {
    // Will be implemented in :feature:practice
}

@Composable
private fun ProfileScreen(onBackClick: () -> Unit) {
    // Will be implemented in :feature:profile
}
