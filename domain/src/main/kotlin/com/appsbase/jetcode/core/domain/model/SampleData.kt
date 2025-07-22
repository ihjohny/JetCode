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
                topicIds = listOf("variables-types", "functions-classes", "control-flow")
            ),
            Skill(
                id = "jetpack-compose",
                name = "Jetpack Compose",
                description = "Modern UI toolkit for Android development",
                iconUrl = null,
                difficulty = Difficulty.INTERMEDIATE,
                estimatedDuration = 240,
                topicIds = listOf("compose-basics", "compose-layouts", "compose-state")
            ),
            Skill(
                id = "android-architecture",
                name = "Android Architecture",
                description = "Learn modern Android architecture patterns and best practices",
                iconUrl = null,
                difficulty = Difficulty.ADVANCED,
                estimatedDuration = 300,
                topicIds = listOf("mvvm-pattern", "repository-pattern", "dependency-injection")
            ),
            Skill(
                id = "coroutines-async",
                name = "Kotlin Coroutines & Async Programming",
                description = "Master asynchronous programming with Kotlin Coroutines",
                iconUrl = null,
                difficulty = Difficulty.INTERMEDIATE,
                estimatedDuration = 200,
                topicIds = listOf("coroutines-basics", "flow-streams", "structured-concurrency")
            ),
            Skill(
                id = "testing-fundamentals",
                name = "Testing Fundamentals",
                description = "Learn unit testing, integration testing, and UI testing in Android",
                iconUrl = null,
                difficulty = Difficulty.INTERMEDIATE,
                estimatedDuration = 180,
                topicIds = listOf("unit-testing", "ui-testing", "test-strategies")
            )
        )
    }

    fun getSampleTopics(): List<Topic> {
        return listOf(
            // Kotlin Basics Topics
            Topic(
                id = "variables-types",
                name = "Variables and Types",
                description = "Learn about Kotlin variables and type system",
                materialIds = listOf("var-val-material", "types-material", "nullable-types-material"),
                practiceSetId = "kotlin-fundamentals-practice",
                duration = 45
            ),
            Topic(
                id = "functions-classes",
                name = "Functions and Classes",
                description = "Understanding functions and object-oriented programming in Kotlin",
                materialIds = listOf("functions-material", "classes-material", "inheritance-material"),
                practiceSetId = "oop-kotlin-practice",
                duration = 60
            ),
            Topic(
                id = "control-flow",
                name = "Control Flow",
                description = "Master conditional statements, loops, and when expressions",
                materialIds = listOf("conditionals-material", "loops-material", "when-expressions-material"),
                practiceSetId = "control-flow-practice",
                duration = 40
            ),

            // Jetpack Compose Topics
            Topic(
                id = "compose-basics",
                name = "Compose Basics",
                description = "Introduction to Jetpack Compose fundamentals",
                materialIds = listOf("compose-intro-material", "composable-functions-material"),
                practiceSetId = "jetpack-compose-basic-practice",
                duration = 30
            ),
            Topic(
                id = "compose-layouts",
                name = "Compose Layouts",
                description = "Building layouts with Jetpack Compose",
                materialIds = listOf("layouts-material", "modifiers-material", "custom-layouts-material"),
                practiceSetId = "compose-layouts-practice",
                duration = 45
            ),
            Topic(
                id = "compose-state",
                name = "State Management in Compose",
                description = "Managing state and recomposition in Jetpack Compose",
                materialIds = listOf("state-material", "remember-material", "viewmodel-compose-material"),
                practiceSetId = "compose-state-practice",
                duration = 50
            ),

            // Android Architecture Topics
            Topic(
                id = "mvvm-pattern",
                name = "MVVM Pattern",
                description = "Model-View-ViewModel architecture pattern",
                materialIds = listOf("mvvm-intro-material", "viewmodel-material", "livedata-material"),
                practiceSetId = "mvvm-practice",
                duration = 60
            ),
            Topic(
                id = "repository-pattern",
                name = "Repository Pattern",
                description = "Data layer abstraction with Repository pattern",
                materialIds = listOf("repository-material", "data-sources-material"),
                practiceSetId = "repository-practice",
                duration = 45
            ),
            Topic(
                id = "dependency-injection",
                name = "Dependency Injection",
                description = "Dependency injection with Hilt/Dagger",
                materialIds = listOf("di-concepts-material", "hilt-material"),
                practiceSetId = "dependency-injection-practice",
                duration = 55
            ),

            // Coroutines Topics
            Topic(
                id = "coroutines-basics",
                name = "Coroutines Basics",
                description = "Introduction to Kotlin Coroutines",
                materialIds = listOf("coroutines-intro-material", "suspend-functions-material"),
                practiceSetId = "coroutines-basic-practice",
                duration = 40
            ),
            Topic(
                id = "flow-streams",
                name = "Flow & Streams",
                description = "Reactive programming with Kotlin Flow",
                materialIds = listOf("flow-material", "operators-material"),
                practiceSetId = "flow-practice",
                duration = 50
            ),
            Topic(
                id = "structured-concurrency",
                name = "Structured Concurrency",
                description = "Advanced coroutines patterns and structured concurrency",
                materialIds = listOf("scopes-material", "exception-handling-material"),
                practiceSetId = "structured-concurrency-practice",
                duration = 45
            ),

            // Testing Topics
            Topic(
                id = "unit-testing",
                name = "Unit Testing",
                description = "Writing effective unit tests",
                materialIds = listOf("junit-material", "mockito-material"),
                practiceSetId = "unit-testing-practice",
                duration = 40
            ),
            Topic(
                id = "ui-testing",
                name = "UI Testing",
                description = "Testing user interfaces with Espresso and Compose Testing",
                materialIds = listOf("espresso-material", "compose-testing-material"),
                practiceSetId = "ui-testing-practice",
                duration = 50
            ),
            Topic(
                id = "test-strategies",
                name = "Testing Strategies",
                description = "Test-driven development and testing best practices",
                materialIds = listOf("tdd-material", "test-pyramid-material"),
                practiceSetId = "test-strategies-practice",
                duration = 35
            )
        )
    }

    fun getSampleMaterials(): List<Material> {
        return listOf(
            // Kotlin Basics Materials
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
            ),
            Material(
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
            ),
            Material(
                id = "nullable-types-material",
                type = MaterialType.MARKDOWN,
                title = "Nullable Types and Null Safety",
                content = """
                    # Null Safety in Kotlin
                    
                    Kotlin's type system distinguishes between nullable and non-null types:
                    
                    ## Nullable Types
                    ```kotlin
                    var name: String? = null // Can be null
                    var age: Int = 25 // Cannot be null
                    ```
                    
                    ## Safe Calls
                    ```kotlin
                    val length = name?.length // Returns null if name is null
                    ```
                    
                    ## Elvis Operator
                    ```kotlin
                    val displayName = name ?: "Unknown"
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "beginner")
            ),
            Material(
                id = "functions-material",
                type = MaterialType.MARKDOWN,
                title = "Functions in Kotlin",
                content = """
                    # Functions in Kotlin
                    
                    Functions are first-class citizens in Kotlin:
                    
                    ## Basic Function
                    ```kotlin
                    fun greet(name: String): String {
                        return "Hello, $/name!"
                    }
                    ```
                    
                    ## Single Expression Function
                    ```kotlin
                    fun add(a: Int, b: Int) = a + b
                    ```
                    
                    ## Default Parameters
                    ```kotlin
                    fun greet(name: String, greeting: String = "Hello") = "$/greeting, $/name!"
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "classes-material",
                type = MaterialType.MARKDOWN,
                title = "Classes in Kotlin",
                content = """
                    # Classes in Kotlin
                    
                    ## Basic Class
                    ```kotlin
                    class Person(val name: String, var age: Int) {
                        fun introduce() {
                            println("Hi, I'm $/name and I'm $/age years old")
                        }
                    }
                    ```
                    
                    ## Data Classes
                    ```kotlin
                    data class User(val id: String, val name: String)
                    ```
                    
                    ## Properties and Getters/Setters
                    ```kotlin
                    class Rectangle(val width: Int, val height: Int) {
                        val area: Int
                            get() = width * height
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "inheritance-material",
                type = MaterialType.MARKDOWN,
                title = "Inheritance and Interfaces",
                content = """
                    # Inheritance in Kotlin
                    
                    ## Open Classes
                    ```kotlin
                    open class Animal(val name: String) {
                        open fun makeSound() = "Some sound"
                    }
                    
                    class Dog(name: String) : Animal(name) {
                        override fun makeSound() = "Woof!"
                    }
                    ```
                    
                    ## Interfaces
                    ```kotlin
                    interface Drawable {
                        fun draw()
                        fun getArea(): Double
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "conditionals-material",
                type = MaterialType.MARKDOWN,
                title = "Conditional Statements",
                content = """
                    # Conditional Statements in Kotlin
                    
                    ## If-Else as Expression
                    ```kotlin
                    val max = if (a > b) a else b
                    ```
                    
                    ## When Expression
                    ```kotlin
                    when (x) {
                        1 -> "One"
                        2, 3 -> "Two or Three"
                        in 4..10 -> "Between 4 and 10"
                        else -> "Other"
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "beginner")
            ),
            Material(
                id = "loops-material",
                type = MaterialType.MARKDOWN,
                title = "Loops in Kotlin",
                content = """
                    # Loops in Kotlin
                    
                    ## For Loop
                    ```kotlin
                    for (i in 1..5) {
                        println(i)
                    }
                    
                    for (item in collection) {
                        println(item)
                    }
                    ```
                    
                    ## While Loop
                    ```kotlin
                    var x = 5
                    while (x > 0) {
                        println(x)
                        x--
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "beginner")
            ),
            Material(
                id = "when-expressions-material",
                type = MaterialType.MARKDOWN,
                title = "When Expressions",
                content = """
                    # When Expressions
                    
                    ## Pattern Matching
                    ```kotlin
                    when (val response = getResponse()) {
                        is Success -> handleSuccess(response.data)
                        is Error -> handleError(response.exception)
                        is Loading -> showLoading()
                    }
                    ```
                    
                    ## Range Checks
                    ```kotlin
                    when (score) {
                        in 90..100 -> "A"
                        in 80..89 -> "B"
                        in 70..79 -> "C"
                        else -> "F"
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),

            // Jetpack Compose Materials
            Material(
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
                        Text(text = "Hello $/name!")
                    }
                    ```
                    
                    ## Preview
                    ```kotlin
                    @Preview
                    @Composable
                    fun GreetingPreview() {
                        Greeting("Android")
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "composable-functions-material",
                type = MaterialType.MARKDOWN,
                title = "Composable Functions",
                content = """
                    # Composable Functions
                    
                    ## Function Composition
                    ```kotlin
                    @Composable
                    fun MessageCard(msg: Message) {
                        Column {
                            Text(text = msg.author)
                            Text(text = msg.body)
                        }
                    }
                    ```
                    
                    ## Parameters and State
                    ```kotlin
                    @Composable
                    fun Counter(count: Int, onIncrement: () -> Unit) {
                        Button(onClick = onIncrement) {
                            Text("Count: $/count")
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
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
                    
                    ## Box Layout
                    ```kotlin
                    @Composable
                    fun MyBox() {
                        Box {
                            Text("Background")
                            Text("Foreground", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "modifiers-material",
                type = MaterialType.MARKDOWN,
                title = "Modifiers in Compose",
                content = """
                    # Modifiers in Compose
                    
                    ## Common Modifiers
                    ```kotlin
                    Text(
                        text = "Hello",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .background(Color.Blue)
                    )
                    ```
                    
                    ## Custom Modifiers
                    ```kotlin
                    fun Modifier.customPadding() = this.padding(horizontal = 16.dp, vertical = 8.dp)
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "custom-layouts-material",
                type = MaterialType.MARKDOWN,
                title = "Custom Layouts",
                content = """
                    # Custom Layouts in Compose
                    
                    ## Layout Composable
                    ```kotlin
                    @Composable
                    fun CustomLayout(
                        modifier: Modifier = Modifier,
                        content: @Composable () -> Unit
                    ) {
                        Layout(
                            modifier = modifier,
                            content = content
                        ) { measurables, constraints ->
                            // Custom layout logic
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "advanced")
            ),
            Material(
                id = "state-material",
                type = MaterialType.MARKDOWN,
                title = "State in Compose",
                content = """
                    # State Management
                    
                    ## Remember
                    ```kotlin
                    @Composable
                    fun Counter() {
                        var count by remember { mutableStateOf(0) }
                        
                        Button(onClick = { count++ }) {
                            Text("Count: $/count")
                        }
                    }
                    ```
                    
                    ## State Hoisting
                    ```kotlin
                    @Composable
                    fun CounterScreen() {
                        var count by remember { mutableStateOf(0) }
                        Counter(count = count, onIncrement = { count++ })
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "remember-material",
                type = MaterialType.MARKDOWN,
                title = "Remember and rememberSaveable",
                content = """
                    # Remember APIs
                    
                    ## remember
                    ```kotlin
                    val scrollState = rememberScrollState()
                    ```
                    
                    ## rememberSaveable
                    ```kotlin
                    var text by rememberSaveable { mutableStateOf("") }
                    ```
                    
                    ## remember with keys
                    ```kotlin
                    val expensiveObject = remember(key1, key2) {
                        createExpensiveObject(key1, key2)
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "viewmodel-compose-material",
                type = MaterialType.MARKDOWN,
                title = "ViewModel with Compose",
                content = """
                    # ViewModel Integration
                    
                    ## Using ViewModel
                    ```kotlin
                    @Composable
                    fun MyScreen(viewModel: MyViewModel = viewModel()) {
                        val uiState by viewModel.uiState.collectAsState()
                        
                        when (uiState) {
                            is Loading -> LoadingScreen()
                            is Success -> SuccessScreen(uiState.data)
                            is Error -> ErrorScreen(uiState.message)
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "advanced")
            ),

            // Architecture Materials
            Material(
                id = "mvvm-intro-material",
                type = MaterialType.MARKDOWN,
                title = "MVVM Pattern Introduction",
                content = """
                    # MVVM Architecture Pattern
                    
                    ## Components
                    - **Model**: Data and business logic
                    - **View**: UI components (Activities, Fragments, Composables)
                    - **ViewModel**: Bridge between Model and View
                    
                    ## Benefits
                    - Separation of concerns
                    - Testability
                    - Lifecycle awareness
                    - Data binding support
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "viewmodel-material",
                type = MaterialType.MARKDOWN,
                title = "ViewModel Implementation",
                content = """
                    # ViewModel Implementation
                    
                    ## Basic ViewModel
                    ```kotlin
                    class UserViewModel : ViewModel() {
                        private val _users = MutableLiveData<List<User>>()
                        val users: LiveData<List<User>> = _users
                        
                        fun loadUsers() {
                            viewModelScope.launch {
                                _users.value = repository.getUsers()
                            }
                        }
                    }
                    ```
                    
                    ## StateFlow ViewModel
                    ```kotlin
                    class UserViewModel : ViewModel() {
                        private val _uiState = MutableStateFlow(UiState.Loading)
                        val uiState: StateFlow<UiState> = _uiState.asStateFlow()
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "livedata-material",
                type = MaterialType.MARKDOWN,
                title = "LiveData and Observing Data",
                content = """
                    # LiveData
                    
                    ## Observing LiveData
                    ```kotlin
                    viewModel.users.observe(this) { users ->
                        // Update UI
                    }
                    ```
                    
                    ## Transformations
                    ```kotlin
                    val userNames = users.map { userList ->
                        userList.map { it.name }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "repository-material",
                type = MaterialType.MARKDOWN,
                title = "Repository Pattern",
                content = """
                    # Repository Pattern
                    
                    ## Repository Implementation
                    ```kotlin
                    class UserRepository(
                        private val apiService: ApiService,
                        private val database: UserDao
                    ) {
                        suspend fun getUsers(): List<User> {
                            return try {
                                val users = apiService.getUsers()
                                database.insertUsers(users)
                                users
                            } catch (e: Exception) {
                                database.getUsers()
                            }
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "data-sources-material",
                type = MaterialType.MARKDOWN,
                title = "Data Sources Architecture",
                content = """
                    # Data Sources
                    
                    ## Remote Data Source
                    ```kotlin
                    interface RemoteDataSource {
                        suspend fun getUsers(): List<User>
                    }
                    
                    class ApiDataSource(private val api: ApiService) : RemoteDataSource {
                        override suspend fun getUsers() = api.getUsers()
                    }
                    ```
                    
                    ## Local Data Source
                    ```kotlin
                    interface LocalDataSource {
                        suspend fun getUsers(): List<User>
                        suspend fun saveUsers(users: List<User>)
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "di-concepts-material",
                type = MaterialType.MARKDOWN,
                title = "Dependency Injection Concepts",
                content = """
                    # Dependency Injection
                    
                    ## What is DI?
                    A design pattern where dependencies are provided from external sources rather than created internally.
                    
                    ## Benefits
                    - Loose coupling
                    - Easy testing
                    - Better code organization
                    - Easier maintenance
                    
                    ## Constructor Injection
                    ```kotlin
                    class UserRepository(
                        private val apiService: ApiService,
                        private val database: UserDao
                    )
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "advanced")
            ),
            Material(
                id = "hilt-material",
                type = MaterialType.MARKDOWN,
                title = "Dependency Injection with Hilt",
                content = """
                    # Hilt - Dependency Injection
                    
                    ## Application Class
                    ```kotlin
                    @HiltAndroidApp
                    class MyApplication : Application()
                    ```
                    
                    ## Module
                    ```kotlin
                    @Module
                    @InstallIn(SingletonComponent::class)
                    object DatabaseModule {
                        @Provides
                        @Singleton
                        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
                            return Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()
                        }
                    }
                    ```
                    
                    ## Injection
                    ```kotlin
                    @AndroidEntryPoint
                    class MainActivity : ComponentActivity() {
                        @Inject lateinit var repository: UserRepository
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "advanced")
            ),

            // Coroutines Materials
            Material(
                id = "coroutines-intro-material",
                type = MaterialType.MARKDOWN,
                title = "Introduction to Coroutines",
                content = """
                    # Kotlin Coroutines
                    
                    ## What are Coroutines?
                    Lightweight threads for asynchronous programming
                    
                    ## Basic Usage
                    ```kotlin
                    fun main() {
                        runBlocking {
                            delay(1000)
                            println("Hello Coroutines!")
                        }
                    }
                    ```
                    
                    ## Launch vs Async
                    ```kotlin
                    // Fire and forget
                    launch {
                        doSomething()
                    }
                    
                    // Get result
                    val deferred = async {
                        computeSomething()
                    }
                    val result = deferred.await()
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "suspend-functions-material",
                type = MaterialType.MARKDOWN,
                title = "Suspend Functions",
                content = """
                    # Suspend Functions
                    
                    ## Declaration
                    ```kotlin
                    suspend fun fetchUser(id: String): User {
                        delay(1000) // Simulate network call
                        return api.getUser(id)
                    }
                    ```
                    
                    ## Calling Suspend Functions
                    ```kotlin
                    class UserRepository {
                        suspend fun getUser(id: String): User {
                            return fetchUser(id)
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "flow-material",
                type = MaterialType.MARKDOWN,
                title = "Kotlin Flow",
                content = """
                    # Kotlin Flow
                    
                    ## Creating Flows
                    ```kotlin
                    fun getUsers(): Flow<List<User>> = flow {
                        while (true) {
                            emit(fetchUsers())
                            delay(5000)
                        }
                    }
                    ```
                    
                    ## Collecting Flows
                    ```kotlin
                    userRepository.getUsers().collect { users ->
                        updateUI(users)
                    }
                    ```
                    
                    ## StateFlow and SharedFlow
                    ```kotlin
                    private val _uiState = MutableStateFlow(UiState.Loading)
                    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "operators-material",
                type = MaterialType.MARKDOWN,
                title = "Flow Operators",
                content = """
                    # Flow Operators
                    
                    ## Transformation Operators
                    ```kotlin
                    flow.map { it.toUpperCase() }
                        .filter { it.length > 5 }
                        .collect { println(it) }
                    ```
                    
                    ## Combination Operators
                    ```kotlin
                    combine(flow1, flow2) { a, b ->
                        "$/a $/b"
                    }.collect { combined ->
                        println(combined)
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "scopes-material",
                type = MaterialType.MARKDOWN,
                title = "Coroutine Scopes",
                content = """
                    # Coroutine Scopes
                    
                    ## ViewModelScope
                    ```kotlin
                    class MyViewModel : ViewModel() {
                        fun loadData() {
                            viewModelScope.launch {
                                // Automatically cancelled when ViewModel is cleared
                            }
                        }
                    }
                    ```
                    
                    ## LifecycleScope
                    ```kotlin
                    class MainActivity : AppCompatActivity() {
                        fun loadData() {
                            lifecycleScope.launch {
                                // Automatically cancelled when lifecycle is destroyed
                            }
                        }
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "advanced")
            ),
            Material(
                id = "exception-handling-material",
                type = MaterialType.MARKDOWN,
                title = "Exception Handling in Coroutines",
                content = """
                    # Exception Handling
                    
                    ## Try-Catch
                    ```kotlin
                    viewModelScope.launch {
                        try {
                            val result = api.getData()
                            updateUI(result)
                        } catch (e: Exception) {
                            handleError(e)
                        }
                    }
                    ```
                    
                    ## CoroutineExceptionHandler
                    ```kotlin
                    val handler = CoroutineExceptionHandler { _, exception ->
                        println("Caught $//exception")
                    }
                    
                    val scope = CoroutineScope(Dispatchers.Main + handler)
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "advanced")
            ),

            // Testing Materials
            Material(
                id = "junit-material",
                type = MaterialType.MARKDOWN,
                title = "JUnit Testing",
                content = """
                    # JUnit Testing
                    
                    ## Basic Test
                    ```kotlin
                    @Test
                    fun `should return sum of two numbers`() {
                        val calculator = Calculator()
                        val result = calculator.add(2, 3)
                        assertEquals(5, result)
                    }
                    ```
                    
                    ## Test Lifecycle
                    ```kotlin
                    @BeforeEach
                    fun setUp() {
                        // Initialize test data
                    }
                    
                    @AfterEach
                    fun tearDown() {
                        // Clean up
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "mockito-material",
                type = MaterialType.MARKDOWN,
                title = "Mocking with Mockito",
                content = """
                    # Mockito for Mocking
                    
                    ## Creating Mocks
                    ```kotlin
                    @Mock
                    lateinit var apiService: ApiService
                    
                    @Test
                    fun `should return users from API`() {
                        `when`(apiService.getUsers()).thenReturn(listOf(user1, user2))
                        
                        val result = repository.getUsers()
                        
                        assertEquals(2, result.size)
                        verify(apiService).getUsers()
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "espresso-material",
                type = MaterialType.MARKDOWN,
                title = "UI Testing with Espresso",
                content = """
                    # Espresso UI Testing
                    
                    ## Basic Interactions
                    ```kotlin
                    @Test
                    fun testButtonClick() {
                        onView(withId(R.id.button))
                            .perform(click())
                        
                        onView(withText("Clicked"))
                            .check(matches(isDisplayed()))
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "compose-testing-material",
                type = MaterialType.MARKDOWN,
                title = "Testing Jetpack Compose",
                content = """
                    # Compose Testing
                    
                    ## Testing Composables
                    ```kotlin
                    @Test
                    fun testCounter() {
                        composeTestRule.setContent {
                            Counter(count = 5, onIncrement = {})
                        }
                        
                        composeTestRule
                            .onNodeWithText("Count: 5")
                            .assertIsDisplayed()
                    }
                    ```
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "tdd-material",
                type = MaterialType.MARKDOWN,
                title = "Test-Driven Development",
                content = """
                    # Test-Driven Development (TDD)
                    
                    ## TDD Cycle
                    1. **Red**: Write a failing test
                    2. **Green**: Write minimal code to pass
                    3. **Refactor**: Improve code while keeping tests green
                    
                    ## Benefits
                    - Better design
                    - Higher confidence
                    - Documentation through tests
                    - Regression prevention
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            ),
            Material(
                id = "test-pyramid-material",
                type = MaterialType.MARKDOWN,
                title = "Testing Strategy and Test Pyramid",
                content = """
                    # Test Pyramid
                    
                    ## Layers
                    1. **Unit Tests**: Fast, isolated, many
                    2. **Integration Tests**: Medium speed, fewer
                    3. **UI Tests**: Slow, comprehensive, fewest
                    
                    ## Best Practices
                    - Write more unit tests than integration tests
                    - Write more integration tests than UI tests
                    - Keep tests independent
                    - Use descriptive test names
                """.trimIndent(),
                metadata = mapOf("difficulty" to "intermediate")
            )
        )
    }

    fun getSamplePractices(): List<Quiz> {
        return listOf(
            // Kotlin Fundamentals
            Quiz(
                id = "var-val-practice",
                type = QuizType.MCQ,
                question = "Which keyword is used for immutable variables in Kotlin?",
                options = listOf("var", "val", "const", "let"),
                correctAnswer = "val",
                explanation = "val is used for immutable variables that cannot be reassigned after initialization.",
            ),
            Quiz(
                id = "types-practice",
                type = QuizType.MCQ,
                question = "What is the inferred type of: val number = 42",
                options = listOf("String", "Int", "Long", "Double"),
                correctAnswer = "Int",
                explanation = "Kotlin infers the type as Int for whole numbers by default.",
            ),
            Quiz(
                id = "nullable-practice",
                type = QuizType.MCQ,
                question = "What does the Elvis operator (?:) do?",
                options = listOf(
                    "Checks if a value is null",
                    "Provides a default value when the left side is null",
                    "Throws an exception",
                    "Converts nullable to non-null"
                ),
                correctAnswer = "Provides a default value when the left side is null",
                explanation = "The Elvis operator (?:) returns the right operand when the left operand is null.",
            ),
            Quiz(
                id = "functions-practice",
                type = QuizType.CODE_CHALLENGE,
                question = "Create a function that takes two integers and returns their sum",
                options = emptyList(),
                correctAnswer = "fun add(a: Int, b: Int): Int = a + b",
                explanation = "This function takes two Int parameters and returns their sum using single expression syntax.",
            ),
            Quiz(
                id = "classes-practice",
                type = QuizType.CODE_CHALLENGE,
                question = "Create a data class named Student with properties: id (String) and name (String)",
                options = emptyList(),
                correctAnswer = "data class Student(val id: String, val name: String)",
                explanation = "Data classes automatically generate equals(), hashCode(), toString(), and copy() methods.",
            ),
            Quiz(
                id = "inheritance-practice",
                type = QuizType.MCQ,
                question = "What keyword is required to make a class inheritable in Kotlin?",
                options = listOf("abstract", "open", "virtual", "inheritable"),
                correctAnswer = "open",
                explanation = "Classes in Kotlin are final by default. Use 'open' to allow inheritance.",
            ),

            // Control Flow
            Quiz(
                id = "conditionals-practice",
                type = QuizType.MCQ,
                question = "Which is true about 'if' in Kotlin?",
                options = listOf(
                    "It's only a statement",
                    "It's only an expression",
                    "It can be both a statement and expression",
                    "It doesn't exist in Kotlin"
                ),
                correctAnswer = "It can be both a statement and expression",
                explanation = "In Kotlin, 'if' can be used as both a statement and an expression that returns a value.",
            ),
            Quiz(
                id = "loops-practice",
                type = QuizType.CODE_CHALLENGE,
                question = "Write a for loop that prints numbers from 1 to 5",
                options = emptyList(),
                correctAnswer = "for (i in 1..5) { println(i) }",
                explanation = "Use range operator (..) to create a range from 1 to 5 inclusive.",
            ),
            Quiz(
                id = "when-practice",
                type = QuizType.MCQ,
                question = "What's the advantage of 'when' over traditional switch statements?",
                options = listOf(
                    "It's faster",
                    "It's an expression and supports pattern matching",
                    "It uses less memory",
                    "It's only for primitive types"
                ),
                correctAnswer = "It's an expression and supports pattern matching",
                explanation = "'when' is more powerful than switch - it's an expression and supports advanced pattern matching.",
            ),

            // Jetpack Compose
            Quiz(
                id = "compose-intro-practice",
                type = QuizType.MCQ,
                question = "What annotation is required for a Compose function?",
                options = listOf("@Component", "@Composable", "@UI", "@View"),
                correctAnswer = "@Composable",
                explanation = "@Composable annotation tells the Compose compiler that this function is intended to convert data into UI.",
            ),
            Quiz(
                id = "composable-functions-practice",
                type = QuizType.CODE_CHALLENGE,
                question = "Create a Composable function named 'WelcomeText' that displays 'Welcome to Jetpack Compose!'",
                options = emptyList(),
                correctAnswer = "@Composable\nfun WelcomeText() {\n    Text(\"Welcome to Jetpack Compose!\")\n}",
                explanation = "Composable functions must be annotated with @Composable and typically use UI components like Text.",
            ),
            Quiz(
                id = "layouts-practice",
                type = QuizType.MCQ,
                question = "Which layout arranges children vertically in Compose?",
                options = listOf("Row", "Column", "Box", "Stack"),
                correctAnswer = "Column",
                explanation = "Column arranges its children vertically, while Row arranges them horizontally.",
            ),
            Quiz(
                id = "modifiers-practice",
                type = QuizType.MCQ,
                question = "What is the purpose of Modifiers in Compose?",
                options = listOf(
                    "To change the content of composables",
                    "To configure layout, appearance, and behavior",
                    "To handle user input only",
                    "To manage state"
                ),
                correctAnswer = "To configure layout, appearance, and behavior",
                explanation = "Modifiers allow you to decorate or add behavior to Compose UI elements.",
            ),
            Quiz(
                id = "state-practice",
                type = QuizType.MCQ,
                question = "Which function is used to create observable state in Compose?",
                options = listOf("remember", "mutableStateOf", "state", "observable"),
                correctAnswer = "mutableStateOf",
                explanation = "mutableStateOf creates observable state that triggers recomposition when changed.",
            ),
            Quiz(
                id = "remember-practice",
                type = QuizType.MCQ,
                question = "What happens to state created with 'remember' during configuration changes?",
                options = listOf(
                    "It's preserved automatically",
                    "It's lost and recreated",
                    "It throws an exception",
                    "It becomes null"
                ),
                correctAnswer = "It's lost and recreated",
                explanation = "'remember' state is lost during configuration changes. Use 'rememberSaveable' to survive them.",
            ),

            // Architecture
            Quiz(
                id = "mvvm-practice",
                type = QuizType.MCQ,
                question = "What is the primary responsibility of a ViewModel in MVVM?",
                options = listOf(
                    "Handle UI rendering",
                    "Manage database operations",
                    "Hold and manage UI-related data",
                    "Handle network requests directly"
                ),
                correctAnswer = "Hold and manage UI-related data",
                explanation = "ViewModel's main job is to hold and manage UI-related data in a lifecycle-conscious way.",
            ),
            Quiz(
                id = "repository-practice",
                type = QuizType.MCQ,
                question = "What is the main benefit of the Repository pattern?",
                options = listOf(
                    "Faster network calls",
                    "Abstract data sources and centralize data access logic",
                    "Reduce memory usage",
                    "Improve UI performance"
                ),
                correctAnswer = "Abstract data sources and centralize data access logic",
                explanation = "Repository pattern provides a clean API for data access and abstracts the data sources.",
            ),
            Quiz(
                id = "dependency-injection-practice",
                type = QuizType.MCQ,
                question = "What is the main advantage of Dependency Injection?",
                options = listOf(
                    "Faster execution",
                    "Less memory usage",
                    "Loose coupling and easier testing",
                    "Better UI performance"
                ),
                correctAnswer = "Loose coupling and easier testing",
                explanation = "DI promotes loose coupling between components and makes testing easier by allowing mock dependencies.",
            ),

            // Coroutines
            Quiz(
                id = "coroutines-basic-practice",
                type = QuizType.MCQ,
                question = "What keyword is used to mark a function as suspendable?",
                options = listOf("async", "suspend", "coroutine", "await"),
                correctAnswer = "suspend",
                explanation = "The 'suspend' keyword marks a function as suspendable, meaning it can be paused and resumed.",
            ),
            Quiz(
                id = "launch-async-practice",
                type = QuizType.MCQ,
                question = "What's the difference between launch and async?",
                options = listOf(
                    "launch is faster than async",
                    "async returns a result, launch doesn't",
                    "launch is for UI, async is for background",
                    "There's no difference"
                ),
                correctAnswer = "async returns a result, launch doesn't",
                explanation = "'async' returns a Deferred that can be awaited for a result, while 'launch' is fire-and-forget.",
            ),
            Quiz(
                id = "flow-practice",
                type = QuizType.MCQ,
                question = "What is Kotlin Flow used for?",
                options = listOf(
                    "Database operations only",
                    "Asynchronous stream of data",
                    "UI layouts",
                    "Network requests only"
                ),
                correctAnswer = "Asynchronous stream of data",
                explanation = "Flow is used to handle asynchronous streams of data that can emit multiple values over time.",
            ),
            Quiz(
                id = "operators-practice",
                type = QuizType.CODE_CHALLENGE,
                question = "Use the map operator to transform a Flow<Int> to uppercase strings",
                options = emptyList(),
                correctAnswer = "flow.map { it.toString().uppercase() }",
                explanation = "The map operator transforms each emitted value using the provided transformation function.",
            ),

            // Testing
            Quiz(
                id = "junit-practice",
                type = QuizType.MCQ,
                question = "Which annotation is used to mark a test method in JUnit?",
                options = listOf("@Test", "@TestMethod", "@UnitTest", "@Method"),
                correctAnswer = "@Test",
                explanation = "@Test annotation marks a method as a test method that should be executed by the test runner.",
            ),
            Quiz(
                id = "mockito-practice",
                type = QuizType.MCQ,
                question = "What is the purpose of mocking in testing?",
                options = listOf(
                    "To slow down tests",
                    "To replace dependencies with controlled fake implementations",
                    "To make tests more complex",
                    "To avoid writing tests"
                ),
                correctAnswer = "To replace dependencies with controlled fake implementations",
                explanation = "Mocking allows you to replace real dependencies with fake implementations you can control in tests.",
            ),
            Quiz(
                id = "compose-testing-practice",
                type = QuizType.MCQ,
                question = "Which rule is used for testing Compose UI?",
                options = listOf(
                    "ActivityTestRule",
                    "ComposeTestRule",
                    "ComposeRule",
                    "UITestRule"
                ),
                correctAnswer = "ComposeTestRule",
                explanation = "ComposeTestRule provides utilities for testing Compose UI components.",
            ),
            Quiz(
                id = "tdd-practice",
                type = QuizType.MCQ,
                question = "What is the correct order in TDD?",
                options = listOf(
                    "Code → Test → Refactor",
                    "Test → Code → Refactor",
                    "Refactor → Test → Code",
                    "Test → Refactor → Code"
                ),
                correctAnswer = "Test → Code → Refactor",
                explanation = "TDD follows Red-Green-Refactor: write failing test, make it pass, then refactor.",
            )
        )
    }

    fun getSamplePracticeSets(): List<PracticeSet> {
        return listOf(
            PracticeSet(
                id = "kotlin-fundamentals-practice",
                name = "Kotlin Fundamentals Practice",
                description = "Test your knowledge of Kotlin basics including variables, types, and null safety",
                quizIds = listOf("var-val-practice", "types-practice", "nullable-practice"),
            ),
            PracticeSet(
                id = "oop-kotlin-practice",
                name = "Object-Oriented Programming in Kotlin",
                description = "Practice questions on classes, data classes, inheritance, and OOP concepts in Kotlin",
                quizIds = listOf("functions-practice", "classes-practice", "inheritance-practice"),
            ),
            PracticeSet(
                id = "control-flow-practice",
                name = "Control Flow Mastery",
                description = "Master conditional statements, loops, and when expressions in Kotlin",
                quizIds = listOf("conditionals-practice", "loops-practice", "when-practice"),
            ),
            PracticeSet(
                id = "jetpack-compose-basic-practice",
                name = "Jetpack Compose Basics",
                description = "Get started with Jetpack Compose fundamentals and composable functions",
                quizIds = listOf("compose-intro-practice", "composable-functions-practice"),
            ),
            PracticeSet(
                id = "compose-layouts-practice",
                name = "Compose Layouts & UI",
                description = "Master layouts, modifiers, and UI composition in Jetpack Compose",
                quizIds = listOf("layouts-practice", "modifiers-practice"),
            ),
            PracticeSet(
                id = "compose-state-practice",
                name = "State Management in Compose",
                description = "Learn state management, remember functions, and reactive UI in Compose",
                quizIds = listOf("state-practice", "remember-practice"),
            ),
            PracticeSet(
                id = "mvvm-practice",
                name = "MVVM Architecture Practice",
                description = "Practice MVVM pattern implementation and ViewModel usage",
                quizIds = listOf("mvvm-practice"),
            ),
            PracticeSet(
                id = "repository-practice",
                name = "Repository Pattern Practice",
                description = "Master the Repository pattern and data layer architecture",
                quizIds = listOf("repository-practice"),
            ),
            PracticeSet(
                id = "dependency-injection-practice",
                name = "Dependency Injection with Hilt",
                description = "Practice dependency injection concepts and Hilt implementation",
                quizIds = listOf("dependency-injection-practice"),
            ),
            PracticeSet(
                id = "coroutines-basic-practice",
                name = "Coroutines Fundamentals",
                description = "Get started with Kotlin Coroutines and suspend functions",
                quizIds = listOf("coroutines-basic-practice", "launch-async-practice"),
            ),
            PracticeSet(
                id = "flow-practice",
                name = "Flow & Reactive Programming",
                description = "Master Kotlin Flow and reactive programming patterns",
                quizIds = listOf("flow-practice", "operators-practice"),
            ),
            PracticeSet(
                id = "structured-concurrency-practice",
                name = "Advanced Coroutines",
                description = "Advanced coroutines concepts including scopes and exception handling",
                quizIds = listOf("coroutines-basic-practice"),
            ),
            PracticeSet(
                id = "unit-testing-practice",
                name = "Unit Testing Essentials",
                description = "Learn unit testing with JUnit and mocking with Mockito",
                quizIds = listOf("junit-practice", "mockito-practice"),
            ),
            PracticeSet(
                id = "ui-testing-practice",
                name = "UI Testing Practice",
                description = "Practice UI testing with Espresso and Compose testing",
                quizIds = listOf("compose-testing-practice"),
            ),
            PracticeSet(
                id = "test-strategies-practice",
                name = "Testing Strategies & Best Practices",
                description = "Learn TDD methodology and testing best practices",
                quizIds = listOf("tdd-practice"),
            ),
            PracticeSet(
                id = "comprehensive-kotlin-practice",
                name = "Comprehensive Kotlin Assessment",
                description = "A comprehensive practice set covering all Kotlin fundamentals",
                quizIds = listOf(
                    "var-val-practice", "functions-practice", "classes-practice",
                    "conditionals-practice", "loops-practice"
                ),
            ),
            PracticeSet(
                id = "android-development-essentials",
                name = "Android Development Essentials",
                description = "Essential concepts for modern Android development",
                quizIds = listOf(
                    "compose-intro-practice", "mvvm-practice", "coroutines-basic-practice",
                    "dependency-injection-practice"
                ),
            ),
            PracticeSet(
                id = "advanced-architecture-practice",
                name = "Advanced Android Architecture",
                description = "Advanced topics in Android architecture and design patterns",
                quizIds = listOf(
                    "mvvm-practice", "repository-practice", "dependency-injection-practice",
                    "flow-practice"
                ),
            )
        )
    }

}