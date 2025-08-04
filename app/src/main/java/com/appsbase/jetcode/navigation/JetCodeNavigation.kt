package com.appsbase.jetcode.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.appsbase.jetcode.feature.dashboard.presentation.DashboardScreen
import com.appsbase.jetcode.feature.learning.presentation.skill_detail.SkillDetailScreen
import com.appsbase.jetcode.feature.learning.presentation.skill_list.SkillListScreen
import com.appsbase.jetcode.feature.learning.presentation.topic_detail.TopicDetailScreen
import com.appsbase.jetcode.feature.onboarding.presentation.OnboardingScreen
import com.appsbase.jetcode.feature.practice.presentation.practice_list.PracticeListScreen
import com.appsbase.jetcode.feature.practice.presentation.practice_list.PracticeTab
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
                onSkillClick = { skillId ->
                    navController.navigate("${JetCodeDestinations.SKILL_DETAIL_ROUTE}/$skillId")
                },
                onPracticeClick = { practiceSetId ->
                    navController.navigate("${JetCodeDestinations.PRACTICE_QUIZ_ROUTE}/$practiceSetId")
                },
                onViewAllSkillsClick = {
                    navController.navigate(JetCodeDestinations.SKILL_LIST_ROUTE)
                },
                onViewAllPracticeClick = {
                    navController.navigate(JetCodeDestinations.PRACTICE_LIST_ROUTE)
                },
                onViewPracticeHistoryClick = {
                    navController.navigate("${JetCodeDestinations.PRACTICE_LIST_ROUTE}?${NavigationArgs.SELECTED_TAB_KEY}=${PracticeTab.COMPLETED.name}")
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
                    navController.navigate("${JetCodeDestinations.PRACTICE_QUIZ_ROUTE}/$practiceSetId")
                },
            )
        }

        // Practice List screen
        composable(
            route = "${JetCodeDestinations.PRACTICE_LIST_ROUTE}?${NavigationArgs.SELECTED_TAB_KEY}={${NavigationArgs.SELECTED_TAB_ARG}}",
            arguments = listOf(
                navArgument(NavigationArgs.SELECTED_TAB_ARG) {
                    type = NavType.StringType
                    defaultValue = PracticeTab.INCOMPLETE.name
                }
            )
        ) { backStackEntry ->
            val selectedTabString =
                backStackEntry.arguments?.getString(NavigationArgs.SELECTED_TAB_ARG)
                    ?: PracticeTab.INCOMPLETE.name
            val initialTab = when (selectedTabString) {
                PracticeTab.COMPLETED.name -> PracticeTab.COMPLETED
                else -> PracticeTab.INCOMPLETE
            }

            PracticeListScreen(
                initialSelectedTab = initialTab,
                onPracticeClick = { practiceSetId ->
                    navController.navigate("${JetCodeDestinations.PRACTICE_QUIZ_ROUTE}/$practiceSetId")
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Practice screen
        composable("${JetCodeDestinations.PRACTICE_QUIZ_ROUTE}/{${NavigationArgs.PRACTICE_SET_ID}}") { backStackEntry ->
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
