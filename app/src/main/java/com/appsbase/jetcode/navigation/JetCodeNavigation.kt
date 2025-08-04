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
 * Main navigation host for the JetCode app with type-safe routing
 */
@Composable
fun JetCodeNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = JetCodeDestinations.Onboarding.route,
    onOnboardingComplete: () -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // Onboarding flow
        composable(JetCodeDestinations.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = {
                    onOnboardingComplete()
                    navController.navigate(JetCodeDestinations.Dashboard.route) {
                        popUpTo(JetCodeDestinations.Onboarding.route) { inclusive = true }
                    }
                },
            )
        }

        // Dashboard
        composable(JetCodeDestinations.Dashboard.route) {
            DashboardScreen(
                onSkillClick = { skillId ->
                    navController.navigate(
                        JetCodeDestinations.SkillDetail(skillId).createRoute(skillId)
                    )
                },
                onPracticeClick = { practiceSetId ->
                    navController.navigate(
                        JetCodeDestinations.PracticeQuiz(practiceSetId).createRoute(practiceSetId)
                    )
                },
                onViewAllSkillsClick = {
                    navController.navigate(JetCodeDestinations.SkillList.route)
                },
                onViewAllPracticeClick = {
                    navController.navigate(JetCodeDestinations.PracticeList().createRoute())
                },
                onViewPracticeHistoryClick = {
                    navController.navigate(
                        JetCodeDestinations.PracticeList().createRoute(PracticeTab.COMPLETED)
                    )
                },
                onProfileClick = {
                    navController.navigate(JetCodeDestinations.Profile.route)
                },
            )
        }

        // Learning dashboard
        composable(JetCodeDestinations.SkillList.route) {
            SkillListScreen(
                onSkillClick = { skillId ->
                    navController.navigate(
                        JetCodeDestinations.SkillDetail(skillId).createRoute(skillId)
                    )
                },
                onProfileClick = {
                    navController.navigate(JetCodeDestinations.Profile.route)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Skill detail
        composable(JetCodeDestinations.SkillDetail.ROUTE_TEMPLATE) { backStackEntry ->
            val skillId = backStackEntry.arguments?.getString(NavigationArgs.SKILL_ID) ?: ""
            SkillDetailScreen(
                skillId = skillId,
                onLessonClick = { topicId ->
                    navController.navigate(
                        JetCodeDestinations.TopicDetail(topicId).createRoute(topicId)
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Topic detail (replaces lesson detail)
        composable(JetCodeDestinations.TopicDetail.ROUTE_TEMPLATE) { backStackEntry ->
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
                    navController.navigate(
                        JetCodeDestinations.PracticeQuiz(practiceSetId).createRoute(practiceSetId)
                    )
                },
            )
        }

        // Practice List screen
        composable(
            route = JetCodeDestinations.PracticeList.ROUTE_TEMPLATE,
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
                    navController.navigate(
                        JetCodeDestinations.PracticeQuiz(practiceSetId).createRoute(practiceSetId)
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Practice screen
        composable(JetCodeDestinations.PracticeQuiz.ROUTE_TEMPLATE) { backStackEntry ->
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
        composable(JetCodeDestinations.Profile.route) {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}
