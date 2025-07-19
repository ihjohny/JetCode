package com.appsbase.jetcode.core.data.remote

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.network.safeNetworkCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow

/**
 * API service for fetching learning content from GitHub
 */
interface LearningApiService {
    suspend fun getSkills(): List<Skill>
    fun getSkillsFlow(): Flow<Result<List<Skill>>>
}

class LearningApiServiceImpl(
    private val httpClient: HttpClient
) : LearningApiService {

    companion object {
        private const val GITHUB_CONTENT_BASE = "repos/your-username/jetcode-content/contents"
        private const val SKILLS_ENDPOINT = "$GITHUB_CONTENT_BASE/skills.json"
    }

    override suspend fun getSkills(): List<Skill> {
        // For now, return sample data. In production, this would fetch from GitHub
        return getSampleSkills()
    }

    override fun getSkillsFlow(): Flow<Result<List<Skill>>> {
        return safeNetworkCall<List<Skill>> {
            httpClient.get(SKILLS_ENDPOINT)
        }
    }

    private fun getSampleSkills(): List<Skill> {
        return listOf(
            Skill(
                id = "kotlin-basics",
                name = "Kotlin Basics",
                description = "Learn the fundamentals of Kotlin programming language",
                iconUrl = null,
                difficulty = com.appsbase.jetcode.core.domain.model.Difficulty.BEGINNER,
                estimatedDuration = 180,
                topics = listOf(
                    com.appsbase.jetcode.core.domain.model.Topic(
                        id = "variables-types",
                        skillId = "kotlin-basics",
                        name = "Variables and Types",
                        description = "Understanding variables, data types, and type inference",
                        order = 1,
                        isUnlocked = true,
                        lessons = listOf(
                            com.appsbase.jetcode.core.domain.model.Lesson(
                                id = "intro-variables",
                                topicId = "variables-types",
                                title = "Introduction to Variables",
                                description = "Learn how to declare and use variables in Kotlin",
                                order = 1,
                                duration = 15,
                                materials = listOf(
                                    com.appsbase.jetcode.core.domain.model.Material(
                                        id = "var-material-1",
                                        lessonId = "intro-variables",
                                        type = com.appsbase.jetcode.core.domain.model.MaterialType.MARKDOWN,
                                        title = "Variable Declaration",
                                        content = """
                                            # Variables in Kotlin
                                            
                                            In Kotlin, you can declare variables using `val` (immutable) or `var` (mutable):
                                            
                                            ```kotlin
                                            val name = "JetCode"  // Immutable
                                            var count = 0        // Mutable
                                            ```
                                            
                                            ## Key Points:
                                            - Use `val` when the value won't change
                                            - Use `var` when you need to modify the value
                                            - Kotlin has type inference - types are automatically detected
                                        """.trimIndent(),
                                        order = 1
                                    )
                                ),
                                practices = listOf(
                                    com.appsbase.jetcode.core.domain.model.Practice(
                                        id = "var-practice-1",
                                        lessonId = "intro-variables",
                                        type = com.appsbase.jetcode.core.domain.model.PracticeType.MCQ,
                                        question = "Which keyword is used for immutable variables in Kotlin?",
                                        options = listOf("var", "val", "const", "let"),
                                        correctAnswer = "val",
                                        explanation = "`val` is used for immutable (read-only) variables in Kotlin.",
                                        difficulty = com.appsbase.jetcode.core.domain.model.Difficulty.BEGINNER,
                                        points = 10
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            Skill(
                id = "jetpack-compose",
                name = "Jetpack Compose",
                description = "Modern UI toolkit for Android development",
                iconUrl = null,
                difficulty = com.appsbase.jetcode.core.domain.model.Difficulty.INTERMEDIATE,
                estimatedDuration = 240,
                topics = emptyList()
            )
        )
    }
}
