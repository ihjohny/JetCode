package com.appsbase.jetcode.core.domain.model

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
                materialIds = listOf("var-val-material", "types-material"),
                practiceSetId = "kotlin-fundamentals-practice",
                duration = 35
            ),
            Topic(
                id = "functions-classes",
                name = "Functions and Classes",
                description = "Understanding functions and object-oriented programming in Kotlin",
                materialIds = listOf("functions-material", "classes-material"),
                practiceSetId = "oop-kotlin-practice",
                duration = 55
            ),
            Topic(
                id = "compose-basics",
                name = "Compose Basics",
                description = "Introduction to Jetpack Compose fundamentals",
                materialIds = listOf("compose-intro-material"),
                practiceSetId = "jetpack-compose-practice",
                duration = 20
            ),
            Topic(
                id = "compose-layouts",
                name = "Compose Layouts",
                description = "Building layouts with Jetpack Compose",
                materialIds = listOf("layouts-material"),
                practiceSetId = "jetpack-compose-practice",
                duration = 30
            )
        )
    }

    fun getSampleMaterials(): List<Material> {
        return listOf(
            Material(
                id = "var-val-material",
                type = MaterialType.MARKDOWN,
                title = "Variables: var vs val",
                content = """
                    # Variables in Kotlin
                    
                    Kotlin has two keywords for declaring variables:
                    
                    ## val (immutable)
                    ```kotlin
                    val name = "John" // Cannot be reassigned
                    ```
                    
                    ## var (mutable)
                    ```kotlin
                    var age = 25 // Can be reassigned
                    age = 26 // This is allowed
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "beginner")
            ), Material(
                id = "types-material",
                type = MaterialType.MARKDOWN,
                title = "Kotlin Type System",
                content = """
                    # Kotlin Type System
                    
                    Kotlin has a rich type system with type inference:
                    
                    ## Basic Types
                    - String
                    - Int, Long, Double, Float
                    - Boolean
                    - Char
                    
                    ## Type Inference
                    ```kotlin
                    val message = "Hello" // Type inferred as String
                    val count = 42 // Type inferred as Int
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "beginner")
            ), Material(
                id = "functions-material",
                type = MaterialType.MARKDOWN,
                title = "Functions in Kotlin",
                content = """
                    # Functions in Kotlin
                    
                    Functions are first-class citizens in Kotlin:
                    
                    ## Basic Function
                    ```kotlin
                    fun greet(name: String): String {
                        return "Hello, \u0024name!"
                    }
                    ```
                    
                    ## Single Expression Function
                    ```kotlin
                    fun add(a: Int, b: Int) = a + b
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ), Material(
                id = "classes-material",
                type = MaterialType.MARKDOWN,
                title = "Classes in Kotlin",
                content = """
                    # Classes in Kotlin
                    
                    ## Basic Class
                    ```kotlin
                    class Person(val name: String, var age: Int) {
                        fun introduce() {
                            println("Hi, I'm \u0024name and I'm \u0024age years old")
                        }
                    }
                    ```
                    
                    ## Data Classes
                    ```kotlin
                    data class User(val id: String, val name: String)
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ), Material(
                id = "compose-intro-material",
                type = MaterialType.MARKDOWN,
                title = "Introduction to Jetpack Compose",
                content = """
                    # Jetpack Compose
                    
                    Modern UI toolkit for Android:
                    
                    ## Basic Composable
                    ```kotlin
                    @Composable
                    fun Greeting(name: String) {
                        Text(text = "Hello \u0024name!")
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ), Material(
                id = "layouts-material",
                type = MaterialType.MARKDOWN,
                title = "Compose Layouts",
                content = """
                    # Compose Layouts
                    
                    ## Column Layout
                    ```kotlin
                    @Composable
                    fun MyColumn() {
                        Column {
                            Text("First")
                            Text("Second")
                        }
                    }
                    ```
                    
                    ## Row Layout
                    ```kotlin
                    @Composable
                    fun MyRow() {
                        Row {
                            Text("Left")
                            Text("Right")
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            )
        )
    }

    fun getSamplePractices(): List<Quiz> {
        return listOf(
            Quiz(
                id = "var-val-practice",
                type = QuizType.MCQ,
                question = "Which keyword is used for immutable variables in Kotlin?",
                options = listOf("var", "val", "const", "let"),
                correctAnswer = "val",
                explanation = "val is used for immutable variables that cannot be reassigned after initialization.",
                difficulty = Difficulty.BEGINNER,
            ),
            Quiz(
                id = "types-practice",
                type = QuizType.MCQ,
                question = "What is the inferred type of: val number = 42",
                options = listOf("String", "Int", "Long", "Double"),
                correctAnswer = "Int",
                explanation = "Kotlin infers the type as Int for whole numbers by default.",
                difficulty = Difficulty.BEGINNER,
            ),
            Quiz(
                id = "functions-practice",
                type = QuizType.CODE_CHALLENGE,
                question = "Create a function that takes two integers and returns their sum",
                options = emptyList(),
                correctAnswer = "fun add(a: Int, b: Int): Int { return a + b }",
                explanation = "This function takes two Int parameters and returns their sum.",
                difficulty = Difficulty.INTERMEDIATE,
            ),
            Quiz(
                id = "classes-practice",
                type = QuizType.CODE_CHALLENGE,
                question = "Create a data class named Student with properties: id (String) and name (String)",
                options = emptyList(),
                correctAnswer = "data class Student(val id: String, val name: String)",
                explanation = "Data classes automatically generate equals(), hashCode(), toString(), and copy() methods.",
                difficulty = Difficulty.INTERMEDIATE,
            ),
            Quiz(
                id = "compose-intro-practice",
                type = QuizType.MCQ,
                question = "What annotation is required for a Compose function?",
                options = listOf("@Component", "@Composable", "@UI", "@View"),
                correctAnswer = "@Composable",
                explanation = "@Composable annotation tells the Compose compiler that this function is intended to convert data into UI.",
                difficulty = Difficulty.INTERMEDIATE,
            ),
            Quiz(
                id = "layouts-practice",
                type = QuizType.MCQ,
                question = "Which layout arranges children vertically in Compose?",
                options = listOf("Row", "Column", "Box", "Stack"),
                correctAnswer = "Column",
                explanation = "Column arranges its children vertically, while Row arranges them horizontally.",
                difficulty = Difficulty.INTERMEDIATE,
            ),
        )
    }

    fun getSamplePracticeSets(): List<PracticeSet> {
        return listOf(
            PracticeSet(
                id = "kotlin-fundamentals-practice",
                name = "Kotlin Fundamentals Practice",
                description = "Test your knowledge of Kotlin basics including variables, types, and functions",
                quizIds = listOf("var-val-practice", "types-practice", "functions-practice"),
                attributes = listOf("beginner", "kotlin", "fundamentals")
            ),
            PracticeSet(
                id = "oop-kotlin-practice",
                name = "Object-Oriented Programming in Kotlin",
                description = "Practice questions on classes, data classes, and OOP concepts in Kotlin",
                quizIds = listOf("classes-practice"),
                attributes = listOf("intermediate", "kotlin", "oop")
            ),
            PracticeSet(
                id = "jetpack-compose-practice",
                name = "Jetpack Compose Essentials",
                description = "Master the basics of Jetpack Compose UI development",
                quizIds = listOf("compose-intro-practice", "layouts-practice"),
                attributes = listOf("intermediate", "compose", "ui")
            ),
            PracticeSet(
                id = "mixed-kotlin-practice",
                name = "Mixed Kotlin Challenges",
                description = "A comprehensive practice set covering various Kotlin topics",
                quizIds = listOf("var-val-practice", "functions-practice", "compose-intro-practice"),
                attributes = listOf("mixed", "kotlin", "comprehensive")
            )
        )
    }

}