package com.appsbase.jetcode.domain.usecase

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.common.util.DispatcherProvider
import com.appsbase.jetcode.domain.model.ActivityType
import com.appsbase.jetcode.domain.model.Difficulty
import com.appsbase.jetcode.domain.model.PracticeSetResult
import com.appsbase.jetcode.domain.model.RecentActivity
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.model.SkillDifficultyStats
import com.appsbase.jetcode.domain.model.SkillProgress
import com.appsbase.jetcode.domain.model.UserStatistics
import com.appsbase.jetcode.domain.repository.LearningRepository
import com.appsbase.jetcode.domain.repository.PracticeResultRepository
import com.appsbase.jetcode.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

/**
 * Use case for getting comprehensive user statistics
 */
class GetUserStatisticsUseCase(
    private val learningRepository: LearningRepository,
    private val progressRepository: ProgressRepository,
    private val practiceResultRepository: PracticeResultRepository,
    private val dispatcherProvider: DispatcherProvider,
) {
    operator fun invoke(): Flow<Result<UserStatistics>> {
        return combine(
            learningRepository.getAllSkills(),
            progressRepository.getAllSkillsProgress(userId = DummyUserId),
            practiceResultRepository.getAllPracticeResults(userId = DummyUserId),
        ) { skillsResult, skillProgressResult, practiceResultsResult ->
            try {
                when (skillsResult) {
                    is Result.Success -> {
                        val skills = skillsResult.data
                        val skillProgress = when (skillProgressResult) {
                            is Result.Success -> skillProgressResult.data
                            is Result.Error -> emptyList()
                        }
                        val practiceResults = when (practiceResultsResult) {
                            is Result.Success -> practiceResultsResult.data
                            is Result.Error -> emptyList()
                        }

                        val statistics =
                            calculateUserStatistics(skills, skillProgress, practiceResults)
                        Result.Success(statistics)
                    }

                    is Result.Error -> skillsResult
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }.flowOn(dispatcherProvider.io)
    }

    private fun calculateUserStatistics(
        skills: List<Skill>,
        skillProgress: List<SkillProgress>,
        practiceResults: List<PracticeSetResult>
    ): UserStatistics {
        // Calculate skills by difficulty
        val skillsByDifficulty = mutableMapOf<Difficulty, SkillDifficultyStats>()

        Difficulty.entries.forEach { difficulty ->
            val skillsInDifficulty = skills.filter { it.difficulty == difficulty }
            val progressInDifficulty = skillProgress.filter { progress ->
                skillsInDifficulty.any { it.id == progress.skillId }
            }

            val enrolled = progressInDifficulty.size
            val completed =
                progressInDifficulty.count { it.completedMaterial >= it.totalMaterial && it.totalMaterial > 0 }
            val running =
                progressInDifficulty.count { it.completedMaterial > 0 && it.completedMaterial < it.totalMaterial }

            skillsByDifficulty[difficulty] = SkillDifficultyStats(
                enrolled = enrolled, completed = completed, running = running
            )
        }

        // Calculate practice statistics
        val totalPracticeSetsCompleted = practiceResults.size
        val totalCorrectAnswers =
            practiceResults.sumOf { it.practiceSessionStatistics.correctAnswers }
        val totalQuestions = practiceResults.sumOf { it.practiceSessionStatistics.totalQuizzes }
        val averageScore = if (practiceResults.isNotEmpty()) {
            practiceResults.map { it.practiceSessionStatistics.scorePercentage }.average().toFloat()
        } else 0f

        // Calculate topic completion (from skill progress)
        val totalTopicsCompleted = skillProgress.sumOf { it.completedMaterial }

        // Generate recent activities (last 7 days)
        val currentTime = System.currentTimeMillis()
        val sevenDaysAgo = currentTime - (7 * 24 * 60 * 60 * 1000)

        val recentActivities = mutableListOf<RecentActivity>()

        // Add recent practice completions
        practiceResults.filter { it.updatedAt >= sevenDaysAgo }.sortedByDescending { it.updatedAt }
            .take(10).forEach { result ->
                recentActivities.add(
                    RecentActivity(
                        id = "practice_${result.id}",
                        type = ActivityType.PRACTICE_COMPLETED,
                        title = "Practice Completed",
                        description = "Scored ${result.practiceSessionStatistics.scorePercentage}%",
                        timestamp = result.updatedAt,
                        metadata = mapOf(
                            "score" to result.practiceSessionStatistics.scorePercentage.toString(),
                            "correct" to result.practiceSessionStatistics.correctAnswers.toString(),
                            "total" to result.practiceSessionStatistics.totalQuizzes.toString()
                        )
                    )
                )
            }

        // Add recent skill progress updates
        skillProgress.filter { it.updatedAt >= sevenDaysAgo }.sortedByDescending { it.updatedAt }
            .take(10).forEach { progress ->
                val skill = skills.find { it.id == progress.skillId }
                if (skill != null) {
                    val activityType =
                        if (progress.completedMaterial >= progress.totalMaterial && progress.totalMaterial > 0) {
                            ActivityType.SKILL_COMPLETED
                        } else if (progress.completedMaterial == 1) {
                            ActivityType.SKILL_STARTED
                        } else {
                            ActivityType.MATERIAL_READ
                        }

                    recentActivities.add(
                        RecentActivity(
                            id = "skill_${progress.id}",
                            type = activityType,
                            title = skill.name,
                            description = when (activityType) {
                                ActivityType.SKILL_COMPLETED -> "Skill completed!"
                                ActivityType.SKILL_STARTED -> "Started learning"
                                ActivityType.MATERIAL_READ -> "Progress: ${progress.completedMaterial}/${progress.totalMaterial}"
                                else -> ""
                            },
                            timestamp = progress.updatedAt,
                            metadata = mapOf(
                                "skillId" to skill.id,
                                "difficulty" to skill.difficulty.name,
                                "progress" to "${progress.completedMaterial}/${progress.totalMaterial}"
                            )
                        )
                    )
                }
            }

        return UserStatistics(
            totalSkillsEnrolled = skillProgress.size,
            totalSkillsCompleted = skillProgress.count { it.completedMaterial >= it.totalMaterial && it.totalMaterial > 0 },
            totalTopicsCompleted = totalTopicsCompleted,
            totalPracticeSetsCompleted = totalPracticeSetsCompleted,
            totalCorrectAnswers = totalCorrectAnswers,
            totalQuestions = totalQuestions,
            averageScore = averageScore,
            skillsByDifficulty = skillsByDifficulty,
            recentActivities = recentActivities.sortedByDescending { it.timestamp }.take(7)
        )
    }
}
