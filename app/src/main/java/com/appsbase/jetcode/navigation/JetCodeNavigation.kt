package com.appsbase.jetcode.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appsbase.jetcode.feature.dashboard.presentation.DashboardScreen
import com.appsbase.jetcode.feature.learning.presentation.skill_detail.SkillDetailScreen
import com.appsbase.jetcode.feature.learning.presentation.skill_list.SkillListScreen
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailScreen
import com.appsbase.jetcode.feature.onboarding.presentation.OnboardingScreen
import com.appsbase.jetcode.feature.practice.presentation.practice_list.PracticeListScreen
import com.appsbase.jetcode.feature.practice.presentation.practice_quiz.PracticeQuizScreen
import com.appsbase.jetcode.feature.profile.presentation.ProfileScreen

/**
 * Main navigation host for the JetCode app
 */
@Composable
fun JetCodeNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = JetCodeDestinations.ONBOARDING_ROUTE,
    onOnboardingComplete: () -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // Onboarding flow
        composable(JetCodeDestinations.ONBOARDING_ROUTE) {
            OnboardingScreen(
                onOnboardingComplete = {
                    onOnboardingComplete()
                    navController.navigate(JetCodeDestinations.DASHBOARD_ROUTE) {
                        popUpTo(JetCodeDestinations.ONBOARDING_ROUTE) { inclusive = true }
                    }
                },
            )
        }

        // Dashboard
        composable(JetCodeDestinations.DASHBOARD_ROUTE) {
            DashboardScreen(
                onEnterLearning = {
                    navController.navigate(JetCodeDestinations.SKILL_LIST_ROUTE)
                },
                onEnterPractice = {
                    navController.navigate(JetCodeDestinations.PRACTICE_LIST_ROUTE)
                },
                onProfileClick = {
                    navController.navigate(JetCodeDestinations.PROFILE_ROUTE)
                },
            )
        }

        // Learning dashboard
        composable(JetCodeDestinations.SKILL_LIST_ROUTE) {
            SkillListScreen(
                onSkillClick = { skillId ->
                    navController.navigate("${JetCodeDestinations.SKILL_DETAIL_ROUTE}/$skillId")
                },
                onProfileClick = {
                    navController.navigate(JetCodeDestinations.PROFILE_ROUTE)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Skill detail
        composable("${JetCodeDestinations.SKILL_DETAIL_ROUTE}/{${NavigationArgs.SKILL_ID}}") { backStackEntry ->
            val skillId = backStackEntry.arguments?.getString(NavigationArgs.SKILL_ID) ?: ""
            SkillDetailScreen(
                skillId = skillId,
                onLessonClick = { topicId ->
                    navController.navigate("${JetCodeDestinations.TOPIC_DETAIL_ROUTE}/$topicId")
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Topic detail (replaces lesson detail)
        composable("${JetCodeDestinations.TOPIC_DETAIL_ROUTE}/{${NavigationArgs.TOPIC_ID}}") { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString(NavigationArgs.TOPIC_ID) ?: ""
            TopicDetailScreen(
                topicId = topicId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onFinishClick = {
                    navController.popBackStack()
                },
                onPracticeClick = { practiceSetId ->
                    navController.navigate(JetCodeDestinations.PRACTICE_LIST_ROUTE)
                },
            )
        }

        // Practice List screen
        composable(JetCodeDestinations.PRACTICE_LIST_ROUTE) {
            PracticeListScreen(
                onPracticeClick = { practiceSetId ->
                    navController.navigate("${JetCodeDestinations.PRACTICE_ROUTE}/$practiceSetId")
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Practice screen
        composable("${JetCodeDestinations.PRACTICE_ROUTE}/{${NavigationArgs.PRACTICE_SET_ID}}") { backStackEntry ->
            val practiceSetId =
                backStackEntry.arguments?.getString(NavigationArgs.PRACTICE_SET_ID) ?: ""
            PracticeQuizScreen(
                practiceId = practiceSetId,
                onPracticeComplete = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Profile
        composable(JetCodeDestinations.PROFILE_ROUTE) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
