package com.appsbase.jetcode.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appsbase.jetcode.feature.learning.presentation.dashboard.LearningDashboardScreen
import com.appsbase.jetcode.feature.learning.presentation.lesson.LessonScreen
import com.appsbase.jetcode.feature.learning.presentation.skilldetail.SkillDetailScreen
import com.appsbase.jetcode.feature.onboarding.presentation.OnboardingScreen
import com.appsbase.jetcode.feature.practice.presentation.PracticeScreen
import com.appsbase.jetcode.feature.profile.presentation.ProfileScreen

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
 * Navigation argument keys used throughout the app
 */
object NavigationArgs {
    const val SKILL_ID = "skillId"
    const val LESSON_ID = "lessonId"
    const val PRACTICE_ID = "practiceId"
}

/**
 * Main navigation host for the JetCode app
 */
@Composable
fun JetCodeNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = JetCodeDestinations.ONBOARDING_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding flow
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

        // Home/Learning dashboard
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
        composable("${JetCodeDestinations.SKILL_DETAIL_ROUTE}/{${NavigationArgs.SKILL_ID}}") { backStackEntry ->
            val skillId = backStackEntry.arguments?.getString(NavigationArgs.SKILL_ID) ?: ""
            SkillDetailScreen(
                skillId = skillId,
                onLessonClick = { lessonId ->
                    navController.navigate("${JetCodeDestinations.LESSON_ROUTE}/$lessonId")
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Lesson content
        composable("${JetCodeDestinations.LESSON_ROUTE}/{${NavigationArgs.LESSON_ID}}") { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString(NavigationArgs.LESSON_ID) ?: ""
            LessonScreen(
                lessonId = lessonId,
                onPracticeClick = { practiceId ->
                    navController.navigate("${JetCodeDestinations.PRACTICE_ROUTE}/$practiceId")
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Practice exercises
        composable("${JetCodeDestinations.PRACTICE_ROUTE}/{${NavigationArgs.PRACTICE_ID}}") { backStackEntry ->
            val practiceId = backStackEntry.arguments?.getString(NavigationArgs.PRACTICE_ID) ?: ""
            PracticeScreen(
                practiceId = practiceId,
                onPracticeComplete = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // User profile
        composable(JetCodeDestinations.PROFILE_ROUTE) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
