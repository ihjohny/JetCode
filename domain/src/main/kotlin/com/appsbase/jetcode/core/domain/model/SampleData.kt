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
                duration = 35, // 15 + 20 minutes total
                materialIds = listOf("var-val-material", "types-material"),
                practiceIds = listOf("var-val-practice", "types-practice")
            ),
            Topic(
                id = "functions-classes",
                name = "Functions and Classes",
                description = "Understanding functions and object-oriented programming in Kotlin",
                duration = 55, // 25 + 30 minutes total
                materialIds = listOf("functions-material", "classes-material"),
                practiceIds = listOf("functions-practice", "classes-practice")
            ),
            Topic(
                id = "compose-basics",
                name = "Compose Basics",
                description = "Introduction to Jetpack Compose fundamentals",
                duration = 20,
                materialIds = listOf("compose-intro-material"),
                practiceIds = listOf("compose-intro-practice")
            ),
            Topic(
                id = "compose-layouts",
                name = "Compose Layouts",
                description = "Building layouts with Jetpack Compose",
                duration = 30,
                materialIds = listOf("layouts-material"),
                practiceIds = listOf("layouts-practice")
            ),
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

    fun getSamplePractices(): List<Practice> {
        return listOf(
            Practice(
                id = "var-val-practice",
                type = PracticeType.MCQ,
                question = "Which keyword is used for immutable variables in Kotlin?",
                options = listOf("var", "val", "const", "let"),
                correctAnswer = "val",
                explanation = "val is used for immutable variables that cannot be reassigned after initialization.",
                difficulty = Difficulty.BEGINNER,
                points = 10,
            ), Practice(
                id = "types-practice",
                type = PracticeType.MCQ,
                question = "What is the inferred type of: val number = 42",
                options = listOf("String", "Int", "Long", "Double"),
                correctAnswer = "Int",
                explanation = "Kotlin infers the type as Int for whole numbers by default.",
                difficulty = Difficulty.BEGINNER,
                points = 10,
            ), Practice(
                id = "functions-practice",
                type = PracticeType.CODE_CHALLENGE,
                question = "Create a function that takes two integers and returns their sum",
                options = emptyList(),
                correctAnswer = "fun add(a: Int, b: Int): Int { return a + b }",
                explanation = "This function takes two Int parameters and returns their sum.",
                difficulty = Difficulty.INTERMEDIATE,
                points = 15,
            ), Practice(
                id = "classes-practice",
                type = PracticeType.CODE_CHALLENGE,
                question = "Create a data class named Student with properties: id (String) and name (String)",
                options = emptyList(),
                correctAnswer = "data class Student(val id: String, val name: String)",
                explanation = "Data classes automatically generate equals(), hashCode(), toString(), and copy() methods.",
                difficulty = Difficulty.INTERMEDIATE,
                points = 15,
            ), Practice(
                id = "compose-intro-practice",
                type = PracticeType.MCQ,
                question = "What annotation is required for a Compose function?",
                options = listOf("@Component", "@Composable", "@UI", "@View"),
                correctAnswer = "@Composable",
                explanation = "@Composable annotation tells the Compose compiler that this function is intended to convert data into UI.",
                difficulty = Difficulty.INTERMEDIATE,
                points = 10,
            ), Practice(
                id = "layouts-practice",
                type = PracticeType.MCQ,
                question = "Which layout arranges children vertically in Compose?",
                options = listOf("Row", "Column", "Box", "Stack"),
                correctAnswer = "Column",
                explanation = "Column arranges its children vertically, while Row arranges them horizontally.",
                difficulty = Difficulty.INTERMEDIATE,
                points = 10,
            )
        )
    }

}