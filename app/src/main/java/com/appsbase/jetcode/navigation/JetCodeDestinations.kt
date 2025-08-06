package com.appsbase.jetcode.navigation

import com.appsbase.jetcode.feature.practice.presentation.practice_list.PracticeTab

/**
 * Constants for route paths
 */
object RouteConstants {
    const val ONBOARDING = "onboarding"
    const val DASHBOARD = "dashboard"
    const val SKILL_LIST = "skill_list"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val SKILL_DETAIL = "skill_detail"
    const val TOPIC_DETAIL = "topic_detail"
    const val PRACTICE_LIST = "practice_list"
    const val PRACTICE_QUIZ = "practice_quiz"
}

/**
 * Type-safe navigation routes for the JetCode app
 * Using sealed classes to ensure compile-time safety and eliminate hardcoded strings
 */
sealed class JetCodeDestinations(val route: String) {
    object Onboarding : JetCodeDestinations(RouteConstants.ONBOARDING)
    object Dashboard : JetCodeDestinations(RouteConstants.DASHBOARD)
    object SkillList : JetCodeDestinations(RouteConstants.SKILL_LIST)
    object Profile : JetCodeDestinations(RouteConstants.PROFILE)
    object Settings : JetCodeDestinations(RouteConstants.SETTINGS)

    data class SkillDetail(val skillId: String) :
        JetCodeDestinations("${RouteConstants.SKILL_DETAIL}/{${NavigationArgs.SKILL_ID}}") {
        fun createRoute(skillId: String) = "${RouteConstants.SKILL_DETAIL}/$skillId"

        companion object {
            const val ROUTE_TEMPLATE = "${RouteConstants.SKILL_DETAIL}/{${NavigationArgs.SKILL_ID}}"
        }
    }

    data class TopicDetail(val topicId: String) :
        JetCodeDestinations("${RouteConstants.TOPIC_DETAIL}/{${NavigationArgs.TOPIC_ID}}") {
        fun createRoute(topicId: String) = "${RouteConstants.TOPIC_DETAIL}/$topicId"

        companion object {
            const val ROUTE_TEMPLATE = "${RouteConstants.TOPIC_DETAIL}/{${NavigationArgs.TOPIC_ID}}"
        }
    }

    data class PracticeQuiz(val practiceSetId: String) :
        JetCodeDestinations("${RouteConstants.PRACTICE_QUIZ}/{${NavigationArgs.PRACTICE_SET_ID}}") {
        fun createRoute(practiceSetId: String) = "${RouteConstants.PRACTICE_QUIZ}/$practiceSetId"

        companion object {
            const val ROUTE_TEMPLATE =
                "${RouteConstants.PRACTICE_QUIZ}/{${NavigationArgs.PRACTICE_SET_ID}}"
        }
    }

    data class PracticeList(val selectedTab: PracticeTab? = null) :
        JetCodeDestinations(RouteConstants.PRACTICE_LIST) {
        fun createRoute(selectedTab: PracticeTab? = null): String {
            return if (selectedTab != null) {
                "${RouteConstants.PRACTICE_LIST}?${NavigationArgs.SELECTED_TAB_KEY}=${selectedTab.name}"
            } else {
                RouteConstants.PRACTICE_LIST
            }
        }

        companion object {
            const val ROUTE_TEMPLATE =
                "${RouteConstants.PRACTICE_LIST}?${NavigationArgs.SELECTED_TAB_KEY}={${NavigationArgs.SELECTED_TAB_ARG}}"
        }
    }
}
