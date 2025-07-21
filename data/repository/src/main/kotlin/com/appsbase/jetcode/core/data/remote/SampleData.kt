package com.appsbase.jetcode.core.data.remote

import com.appsbase.jetcode.core.domain.model.Difficulty
import com.appsbase.jetcode.core.domain.model.Lesson
import com.appsbase.jetcode.core.domain.model.Material
import com.appsbase.jetcode.core.domain.model.MaterialType
import com.appsbase.jetcode.core.domain.model.Practice
import com.appsbase.jetcode.core.domain.model.PracticeType
import com.appsbase.jetcode.core.domain.model.Skill
import com.appsbase.jetcode.core.domain.model.Topic

object SampleData {

    fun getSampleSkills(): List<Skill> {
        return listOf(
            Skill(
                id = "kotlin-basics",
                name = "Kotlin Basics",
                description = "Learn the fundamentals of Kotlin programming language",
                iconUrl = null,
                difficulty = Difficulty.BEGINNER,
                estimatedDuration = 180,
                topicIds = listOf("variables-types", "functions-classes")
            ),
            Skill(
                id = "jetpack-compose",
                name = "Jetpack Compose",
                description = "Modern UI toolkit for Android development",
                iconUrl = null,
                difficulty = Difficulty.INTERMEDIATE,
                estimatedDuration = 240,
                topicIds = listOf("compose-basics", "compose-layouts")
            ),
        )
    }

    fun getSampleTopics(): List<Topic> {
        return listOf(
            Topic(
                id = "variables-types",
                name = "Variables and Types",
                description = "Learn about Kotlin variables and type system",
                order = 1,
                isUnlocked = true,
                lessonIds = listOf("var-val-lesson", "types-lesson")
            ),
            Topic(
                id = "functions-classes",
                name = "Functions and Classes",
                description = "Understanding functions and object-oriented programming in Kotlin",
                order = 2,
                isUnlocked = false,
                lessonIds = listOf("functions-lesson", "classes-lesson")
            ),
            Topic(
                id = "compose-basics",
                name = "Compose Basics",
                description = "Introduction to Jetpack Compose fundamentals",
                order = 1,
                isUnlocked = true,
                lessonIds = listOf("compose-intro-lesson")
            ),
            Topic(
                id = "compose-layouts",
                name = "Compose Layouts",
                description = "Building layouts with Jetpack Compose",
                order = 2,
                isUnlocked = false,
                lessonIds = listOf("layouts-lesson")
            ),
        )
    }

    fun getSampleLessons(): List<Lesson> {
        return listOf(
            Lesson(
                id = "var-val-lesson",
                title = "Variables: var vs val",
                description = "Understanding mutable and immutable variables in Kotlin",
                order = 1,
                duration = 15,
                materialIds = listOf("var-val-material"),
                practiceIds = listOf("var-val-practice")
            ),
            Lesson(
                id = "types-lesson",
                title = "Kotlin Type System",
                description = "Exploring Kotlin's type system and type inference",
                order = 2,
                duration = 20,
                materialIds = listOf("types-material"),
                practiceIds = listOf("types-practice")
            ),
            Lesson(
                id = "functions-lesson",
                title = "Functions in Kotlin",
                description = "Creating and using functions in Kotlin",
                order = 1,
                duration = 25,
                materialIds = listOf("functions-material"),
                practiceIds = listOf("functions-practice")
            ),
            Lesson(
                id = "classes-lesson",
                title = "Classes and Objects",
                description = "Object-oriented programming with Kotlin classes",
                order = 2,
                duration = 30,
                materialIds = listOf("classes-material"),
                practiceIds = listOf("classes-practice")
            ),
            Lesson(
                id = "compose-intro-lesson",
                title = "Introduction to Compose",
                description = "Getting started with Jetpack Compose",
                order = 1,
                duration = 20,
                materialIds = listOf("compose-intro-material"),
                practiceIds = listOf("compose-intro-practice")
            ),
            Lesson(
                id = "layouts-lesson",
                title = "Building Layouts",
                description = "Creating UI layouts with Compose",
                order = 1,
                duration = 35,
                materialIds = listOf("layouts-material"),
                practiceIds = listOf("layouts-practice")
            ),
        )
    }

    fun getSampleMaterials(): List<Material> {
        return listOf(
            Material(
                id = "var-val-material",
                type = MaterialType.TEXT,
                title = "Variables in Kotlin",
                content = """
                    In Kotlin, you can declare variables using 'var' for mutable variables and 'val' for immutable variables.
                    
                    var mutableVariable = "I can be changed"
                    val immutableVariable = "I cannot be changed"
                    
                    Use 'val' whenever possible for better code safety.
                """.trimIndent(),
                order = 1
            ),
            Material(
                id = "types-material",
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
            ),
            Material(
                id = "functions-material",
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
            ),
            Material(
                id = "classes-material",
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
            ),
            Material(
                id = "compose-intro-material",
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
            ),
            Material(
                id = "layouts-material",
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
            ),
        )
    }

    fun getSamplePractices(): List<Practice> {
        return listOf(
            Practice(
                id = "var-val-practice",
                type = PracticeType.MCQ,
                question = "Which keyword is used for immutable variables in Kotlin?",
                options = listOf("var", "val", "let", "const"),
                correctAnswer = "val",
                explanation = "'val' creates read-only (immutable) variables in Kotlin",
                difficulty = Difficulty.BEGINNER,
                points = 10
            ),
            Practice(
                id = "types-practice",
                type = PracticeType.MCQ,
                question = "What is the default type for whole numbers in Kotlin?",
                options = listOf("Long", "Int", "Short", "Byte"),
                correctAnswer = "Int",
                explanation = "Kotlin infers Int as the default type for whole numbers",
                difficulty = Difficulty.BEGINNER,
                points = 10
            ),
            Practice(
                id = "functions-practice",
                type = PracticeType.CODE_CHALLENGE,
                question = "Write a function that takes two integers and returns their sum",
                correctAnswer = "fun add(a: Int, b: Int): Int = a + b",
                explanation = "This is a single expression function that adds two integers",
                difficulty = Difficulty.BEGINNER,
                points = 15
            ),
            Practice(
                id = "classes-practice",
                type = PracticeType.CODE_CHALLENGE,
                question = "Create a class 'Book' with properties 'title' and 'author'",
                correctAnswer = "class Book(val title: String, val author: String)",
                explanation = "This creates a data class with two immutable properties",
                difficulty = Difficulty.INTERMEDIATE,
                points = 20
            ),
            Practice(
                id = "compose-intro-practice",
                type = PracticeType.MCQ,
                question = "What is the main benefit of Jetpack Compose?",
                options = listOf(
                    "Faster compilation", "Declarative UI", "Smaller APK size", "Better performance"
                ),
                correctAnswer = "Declarative UI",
                explanation = "Compose uses a declarative approach to building UI",
                difficulty = Difficulty.BEGINNER,
                points = 10
            ),
            Practice(
                id = "layouts-practice",
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
            ),
        )
    }

}