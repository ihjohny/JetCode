package com.appsbase.jetcode.core.data.remote

import com.appsbase.jetcode.core.domain.model.Difficulty
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.MaterialType
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.domain.model.PracticeType
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic
import io.ktor.client.HttpClient

class LearningApiServiceImpl(
    private val httpClient: HttpClient
) : LearningApiService {

    companion object {
        private const val GITHUB_CONTENT_BASE = "repos/your-username/jetcode-content/contents"
        private const val SKILLS_ENDPOINT = "$GITHUB_CONTENT_BASE/skills.json"
        private const val TOPICS_ENDPOINT = "$GITHUB_CONTENT_BASE/topics.json"
        private const val LESSONS_ENDPOINT = "$GITHUB_CONTENT_BASE/lessons.json"
        private const val MATERIALS_ENDPOINT = "$GITHUB_CONTENT_BASE/materials.json"
        private const val PRACTICES_ENDPOINT = "$GITHUB_CONTENT_BASE/practices.json"
    }

    override suspend fun getSkills(): List<Skill> {
        // For now, return sample data. In production, this would fetch from GitHub
        return getSampleSkills()
    }

    override suspend fun getTopics(skillId: String?): List<Topic> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allTopics = getSampleTopics()
        return if (skillId != null) {
            allTopics.filter { it.skillId == skillId }
        } else {
            allTopics
        }
    }

    override suspend fun getLessons(topicId: String?): List<Lesson> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allLessons = getSampleLessons()
        return if (topicId != null) {
            allLessons.filter { it.topicId == topicId }
        } else {
            allLessons
        }
    }

    override suspend fun getMaterials(lessonId: String?): List<Material> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allMaterials = getSampleMaterials()
        return if (lessonId != null) {
            allMaterials.filter { it.lessonId == lessonId }
        } else {
            allMaterials
        }
    }

    override suspend fun getPractices(lessonId: String?): List<Practice> {
        // For now, return sample data. In production, this would fetch from GitHub
        val allPractices = getSamplePractices()
        return if (lessonId != null) {
            allPractices.filter { it.lessonId == lessonId }
        } else {
            allPractices
        }
    }

    private fun getSampleSkills(): List<Skill> {
        return listOf(
            Skill(
                id = "kotlin-basics",
                name = "Kotlin Basics",
                description = "Learn the fundamentals of Kotlin programming language",
                iconUrl = null,
                difficulty = Difficulty.BEGINNER,
                estimatedDuration = 180,
                topicIds = listOf("variables-types", "functions-classes")
            ), Skill(
                id = "jetpack-compose",
                name = "Jetpack Compose",
                description = "Modern UI toolkit for Android development",
                iconUrl = null,
                difficulty = Difficulty.INTERMEDIATE,
                estimatedDuration = 240,
                topicIds = listOf("compose-basics", "compose-layouts")
            )
        )
    }

    private fun getSampleTopics(): List<Topic> {
        return listOf(
            Topic(
                id = "variables-types",
                skillId = "kotlin-basics",
                name = "Variables and Types",
                description = "Learn about Kotlin variables and type system",
                order = 1,
                isUnlocked = true,
                lessonIds = listOf("var-val-lesson", "types-lesson")
            ), Topic(
                id = "functions-classes",
                skillId = "kotlin-basics",
                name = "Functions and Classes",
                description = "Understanding functions and object-oriented programming in Kotlin",
                order = 2,
                isUnlocked = false,
                lessonIds = listOf("functions-lesson", "classes-lesson")
            ), Topic(
                id = "compose-basics",
                skillId = "jetpack-compose",
                name = "Compose Basics",
                description = "Introduction to Jetpack Compose fundamentals",
                order = 1,
                isUnlocked = true,
                lessonIds = listOf("compose-intro-lesson")
            ), Topic(
                id = "compose-layouts",
                skillId = "jetpack-compose",
                name = "Compose Layouts",
                description = "Building layouts with Jetpack Compose",
                order = 2,
                isUnlocked = false,
                lessonIds = listOf("layouts-lesson")
            )
        )
    }

    private fun getSampleLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "var-val-lesson",
                topicId = "variables-types",
                title = "Variables: var vs val",
                description = "Understanding mutable and immutable variables in Kotlin",
                order = 1,
                duration = 15,
                materialIds = listOf("var-val-material"),
                practiceIds = listOf("var-val-practice")
            ), Lesson(
                id = "types-lesson",
                topicId = "variables-types",
                title = "Kotlin Type System",
                description = "Exploring Kotlin's type system and type inference",
                order = 2,
                duration = 20,
                materialIds = listOf("types-material"),
                practiceIds = listOf("types-practice")
            ), Lesson(
                id = "functions-lesson",
                topicId = "functions-classes",
                title = "Functions in Kotlin",
                description = "Creating and using functions in Kotlin",
                order = 1,
                duration = 25,
                materialIds = listOf("functions-material"),
                practiceIds = listOf("functions-practice")
            ), Lesson(
                id = "classes-lesson",
                topicId = "functions-classes",
                title = "Classes and Objects",
                description = "Object-oriented programming with Kotlin classes",
                order = 2,
                duration = 30,
                materialIds = listOf("classes-material"),
                practiceIds = listOf("classes-practice")
            ), Lesson(
                id = "compose-intro-lesson",
                topicId = "compose-basics",
                title = "Introduction to Compose",
                description = "Getting started with Jetpack Compose",
                order = 1,
                duration = 20,
                materialIds = listOf("compose-intro-material"),
                practiceIds = listOf("compose-intro-practice")
            ), Lesson(
                id = "layouts-lesson",
                topicId = "compose-layouts",
                title = "Building Layouts",
                description = "Creating UI layouts with Compose",
                order = 1,
                duration = 35,
                materialIds = listOf("layouts-material"),
                practiceIds = listOf("layouts-practice")
            )
        )
    }

    private fun getSampleMaterials(): List<Material> {
        return listOf(
            Material(
                id = "var-val-material",
                lessonId = "var-val-lesson",
                type = MaterialType.TEXT,
                title = "Variables in Kotlin",
                content = """
                    In Kotlin, you can declare variables using 'var' for mutable variables and 'val' for immutable variables.
                    
                    var mutableVariable = "I can be changed"
                    val immutableVariable = "I cannot be changed"
                    
                    Use 'val' whenever possible for better code safety.
                """.trimIndent(),
                order = 1
            ), Material(
                id = "types-material",
                lessonId = "types-lesson",
                type = MaterialType.TEXT,
                title = "Kotlin Type System",
                content = """
                    Kotlin has a rich type system with type inference.
                    
                    Basic types include:
                    - Int, Long, Float, Double
                    - String, Char
                    - Boolean
                    
                    Kotlin can often infer the type automatically.
                """.trimIndent(),
                order = 1
            ), Material(
                id = "functions-material",
                lessonId = "functions-lesson",
                type = MaterialType.CODE,
                title = "Function Syntax",
                content = """
                    fun functionName(parameter: Type): ReturnType {
                        return value
                    }
                    
                    // Single expression function
                    fun add(a: Int, b: Int) = a + b
                """.trimIndent(),
                order = 1
            ), Material(
                id = "classes-material",
                lessonId = "classes-lesson",
                type = MaterialType.CODE,
                title = "Class Declaration",
                content = """
                    class Person(val name: String, var age: Int) {
                        fun introduce() {
                            println("Hi, I'm " + name + " and I'm " + age + " years old")
                        }
                    }
                """.trimIndent(),
                order = 1
            ), Material(
                id = "compose-intro-material",
                lessonId = "compose-intro-lesson",
                type = MaterialType.TEXT,
                title = "What is Jetpack Compose?",
                content = """
                    Jetpack Compose is Android's modern toolkit for building native UI.
                    It simplifies and accelerates UI development on Android.
                    
                    Key benefits:
                    - Declarative UI
                    - Less code
                    - Intuitive
                    - Accelerates development
                """.trimIndent(),
                order = 1
            ), Material(
                id = "layouts-material",
                lessonId = "layouts-lesson",
                type = MaterialType.CODE,
                title = "Compose Layouts",
                content = """
                    @Composable
                    fun MyLayout() {
                        Column {
                            Text("Header")
                            Row {
                                Text("Left")
                                Text("Right")
                            }
                        }
                    }
                """.trimIndent(),
                order = 1
            )
        )
    }

    private fun getSamplePractices(): List<Practice> {
        return listOf(
            Practice(
                id = "var-val-practice",
                lessonId = "var-val-lesson",
                type = PracticeType.MCQ,
                question = "Which keyword is used for immutable variables in Kotlin?",
                options = listOf("var", "val", "let", "const"),
                correctAnswer = "val",
                explanation = "'val' creates read-only (immutable) variables in Kotlin",
                difficulty = Difficulty.BEGINNER,
                points = 10
            ), Practice(
                id = "types-practice",
                lessonId = "types-lesson",
                type = PracticeType.MCQ,
                question = "What is the default type for whole numbers in Kotlin?",
                options = listOf("Long", "Int", "Short", "Byte"),
                correctAnswer = "Int",
                explanation = "Kotlin infers Int as the default type for whole numbers",
                difficulty = Difficulty.BEGINNER,
                points = 10
            ), Practice(
                id = "functions-practice",
                lessonId = "functions-lesson",
                type = PracticeType.CODE_CHALLENGE,
                question = "Write a function that takes two integers and returns their sum",
                correctAnswer = "fun add(a: Int, b: Int): Int = a + b",
                explanation = "This is a single expression function that adds two integers",
                difficulty = Difficulty.BEGINNER,
                points = 15
            ), Practice(
                id = "classes-practice",
                lessonId = "classes-lesson",
                type = PracticeType.CODE_CHALLENGE,
                question = "Create a class 'Book' with properties 'title' and 'author'",
                correctAnswer = "class Book(val title: String, val author: String)",
                explanation = "This creates a data class with two immutable properties",
                difficulty = Difficulty.INTERMEDIATE,
                points = 20
            ), Practice(
                id = "compose-intro-practice",
                lessonId = "compose-intro-lesson",
                type = PracticeType.MCQ,
                question = "What is the main benefit of Jetpack Compose?",
                options = listOf(
                    "Faster compilation", "Declarative UI", "Smaller APK size", "Better performance"
                ),
                correctAnswer = "Declarative UI",
                explanation = "Compose uses a declarative approach to building UI",
                difficulty = Difficulty.BEGINNER,
                points = 10
            ), Practice(
                id = "layouts-practice",
                lessonId = "layouts-lesson",
                type = PracticeType.CODE_CHALLENGE,
                question = "Create a Column with two Text composables",
                correctAnswer = """
                    Column {
                        Text("First")
                        Text("Second")
                    }
                """.trimIndent(),
                explanation = "Column arranges its children vertically",
                difficulty = Difficulty.INTERMEDIATE,
                points = 15
            )
        )
    }
}
