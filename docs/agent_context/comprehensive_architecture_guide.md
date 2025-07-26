
# JetCode Comprehensive Architecture Guide

## Project Overview

JetCode is an Android learning application built with **Clean Architecture**, **MVI Pattern**, and **Offline-First Strategy**. This comprehensive guide provides complete architectural context for consistent development and AI-assisted code generation.

## Table of Contents
1. [Architecture Principles](#architecture-principles)
2. [Project Structure](#project-structure)
3. [Technology Stack](#technology-stack)
4. [Architecture Layers](#architecture-layers)
5. [Design Patterns](#design-patterns)
6. [Data Flow & State Management](#data-flow--state-management)
7. [Domain Models](#domain-models)
8. [Dependency Injection](#dependency-injection)
9. [Navigation Architecture](#navigation-architecture)
10. [Error Handling Strategy](#error-handling-strategy)
11. [Code Generation Guidelines](#code-generation-guidelines)

## Architecture Principles

### 1. Clean Architecture Layers
```
Presentation Layer (Features) ← Domain Layer ← Data Layer
```

- **Presentation**: UI components, ViewModels, MVI contracts
- **Domain**: Business logic, use cases, repository interfaces, models
- **Data**: Repository implementations, database, remote APIs, preferences

### 2. Core Design Principles
- **Separation of Concerns**: Each layer has distinct responsibilities
- **Dependency Inversion**: Dependencies flow toward the domain layer
- **Single Responsibility**: One concern per class/module
- **Interface Segregation**: Focused, minimal interfaces
- **Offline-First**: Local database as single source of truth
- **Reactive Programming**: Flow-based data streams

## Project Structure

```
JetCode/
├── app/                          # Main application module
│   ├── src/main/                 # Application entry point
│   └── navigation/               # App-level navigation setup
├── core/                         # Core shared modules
│   ├── analytics/                # Analytics and tracking
│   ├── common/                   # Common utilities and base classes
│   │   ├── base/                 # Base MVI classes
│   │   ├── result/               # Result wrapper
│   │   └── utils/                # Utility functions
│   ├── designsystem/             # Design system and theming
│   │   ├── theme/                # App theme, colors, typography
│   │   └── components/           # Reusable UI components
│   └── ui/                       # Common UI utilities
├── data/                         # Data layer modules
│   ├── database/                 # Local database (Room)
│   │   ├── dao/                  # Database access objects
│   │   ├── entity/               # Database entities
│   │   └── database/             # Database configuration
│   ├── preferences/              # User preferences (DataStore)
│   ├── remote/                   # Network layer (Ktor)
│   │   ├── api/                  # API service interfaces
│   │   ├── dto/                  # Data transfer objects
│   │   └── client/               # HTTP client setup
│   └── repository/               # Repository implementations
│       ├── mapper/               # Entity ↔ Domain mappers
│       └── repository/           # Repository implementations
├── domain/                       # Business logic and use cases
│   ├── model/                    # Domain models
│   ├── repository/               # Repository interfaces
│   ├── usecase/                  # Business use cases
│   └── common/                   # Domain-level utilities
└── features/                     # Feature-specific modules
    ├── learning/                 # Learning content features
    │   ├── domain/               # Feature-specific use cases
    │   ├── data/                 # Feature-specific data (if any)
    │   └── presentation/         # UI layer
    │       ├── skill_list/       # Screen-specific packages
    │       └── skill_detail/
    ├── onboarding/               # User onboarding
    ├── practice/                 # Practice and quizzes
    └── profile/                  # User profile
```

## Technology Stack

### Core Technologies
- **Kotlin**: Primary language with coroutines and flows
- **Jetpack Compose**: Modern declarative UI framework
- **Coroutines + Flow**: Asynchronous programming and reactive streams
- **Koin**: Dependency injection framework
- **Room**: Local database with SQLite
- **Ktor**: HTTP client for API calls
- **DataStore**: Preferences and settings storage

### Architecture Components
- **Navigation Compose**: Type-safe navigation between screens
- **ViewModel**: UI state management following MVVM
- **StateFlow/SharedFlow**: Reactive state management
- **WorkManager**: Background task scheduling (future implementation)

### Additional Libraries
- **Kotlinx Serialization**: JSON serialization/deserialization
- **Detekt**: Static code analysis
- **KtLint**: Code formatting
- **MockK**: Testing framework for mocking
- **JUnit 4**: Unit testing framework

## Architecture Layers

### 1. Domain Layer (`domain/`)
**Pure Kotlin module** containing business logic, independent of Android framework.

#### Components:
- **Models**: Core business entities (`Skill`, `Topic`, `Material`, `Quiz`, `PracticeSet`)
- **Repository Interfaces**: Abstract contracts for data access
- **Use Cases**: Single-responsibility business operations
- **Common Types**: `Result<T>` wrapper, error handling

#### Key Patterns:
```kotlin
// Domain models inherit from abstract Content class for polymorphism
abstract class Content {
    abstract val id: String
    abstract val title: String
    abstract val description: String
}

data class Skill(
    override val id: String,
    override val title: String,
    override val description: String,
    val topics: List<Topic>,
    val difficulty: Difficulty,
    val estimatedHours: Int
) : Content()

data class Topic(
    override val id: String,
    override val title: String,
    override val description: String,
    val skillId: String,
    val materials: List<Material>,
    val practiceSet: PracticeSet?
) : Content()
```

#### Use Case Pattern:
```kotlin
class GetSkillsUseCase(
    private val learningRepository: LearningRepository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<List<Skill>>> = 
        learningRepository.getSkills()
            .flowOn(dispatcherProvider.io)
            .catch { e ->
                emit(Result.Error(AppError.DataError.DatabaseError))
            }
}
```

#### Repository Interface:
```kotlin
interface LearningRepository {
    fun getSkills(): Flow<Result<List<Skill>>>
    fun getTopicsBySkillId(skillId: String): Flow<Result<List<Topic>>>
    suspend fun syncLearningContent(): Result<Unit>
    suspend fun updateSkillProgress(skillId: String, progress: Float): Result<Unit>
}
```

### 2. Data Layer (`data/`)
Implements domain repository interfaces and manages data sources.

#### Modules:
- **`database/`**: Room database with DAOs and entities
- **`remote/`**: Ktor HTTP client and API services
- **`preferences/`**: DataStore for user preferences
- **`repository/`**: Repository pattern implementations

#### Database Layer (`data/database/`)
```kotlin
// Database entities
@Entity(tableName = "skills")
data class SkillEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val estimatedHours: Int,
    val createdAt: Long,
    val updatedAt: Long
)

// Data Access Objects
@Dao
interface LearningDao {
    @Query("SELECT * FROM skills ORDER BY title ASC")
    fun getAllSkills(): Flow<List<SkillEntity>>
    
    @Query("SELECT * FROM topics WHERE skillId = :skillId")
    fun getTopicsBySkillId(skillId: String): Flow<List<TopicEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkills(skills: List<SkillEntity>)
}

// Database configuration
@Database(
    entities = [SkillEntity::class, TopicEntity::class, MaterialEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class JetCodeDatabase : RoomDatabase() {
    abstract fun learningDao(): LearningDao
}
```

#### Repository Implementation:
```kotlin
class LearningRepositoryImpl(
    private val learningDao: LearningDao,
    private val apiService: LearningApiService,
    private val syncManager: SyncManager
) : LearningRepository {
    
    override fun getSkills(): Flow<Result<List<Skill>>> = flow {
        emit(Result.Loading)
        
        learningDao.getAllSkills()
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e -> emit(Result.Error(AppError.DataError.DatabaseError)) }
            .collect { skills ->
                emit(Result.Success(skills))
                
                // Trigger background sync if data is stale
                if (syncManager.shouldSync()) {
                    syncLearningContent()
                }
            }
    }
    
    override suspend fun syncLearningContent(): Result<Unit> = try {
        val remoteSkills = apiService.getSkills()
        learningDao.insertSkills(remoteSkills.map { it.toEntity() })
        syncManager.updateLastSyncTime()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(AppError.NetworkError.SyncFailed)
    }
}
```

#### Data Mapping Strategy:
```kotlin
// Entity ↔ Domain model mapping
fun SkillEntity.toDomain(): Skill = Skill(
    id = id,
    title = title,
    description = description,
    topics = emptyList(), // Loaded separately for performance
    difficulty = Difficulty.valueOf(difficulty),
    estimatedHours = estimatedHours
)

fun Skill.toEntity(): SkillEntity = SkillEntity(
    id = id,
    title = title,
    description = description,
    difficulty = difficulty.name,
    estimatedHours = estimatedHours,
    createdAt = System.currentTimeMillis(),
    updatedAt = System.currentTimeMillis()
)
```

### 3. Presentation Layer (`features/`)
Feature-based modularization with MVI pattern.

#### MVI Architecture Implementation:
```kotlin
// Base MVI interfaces in core/common
interface UiState
interface UiIntent  
interface UiEffect

// Base ViewModel
abstract class BaseViewModel<State : UiState, Intent : UiIntent, Effect : UiEffect>(
    initialState: State
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<State> = _uiState.asStateFlow()
    
    private val _effect = Channel<Effect>()
    val effect: Flow<Effect> = _effect.receiveAsFlow()
    
    protected fun updateState(newState: State) {
        _uiState.value = newState
    }
    
    protected fun currentState(): State = _uiState.value
    
    protected suspend fun sendEffect(effect: Effect) {
        _effect.send(effect)
    }
    
    abstract fun handleIntent(intent: Intent)
}
```

#### Feature Implementation Pattern:
```kotlin
// Contract definition
object SkillListContract {
    data class State(
        val skills: List<Skill> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val searchQuery: String = ""
    ) : UiState
    
    sealed class Intent : UiIntent {
        data object LoadSkills : Intent()
        data class SearchSkills(val query: String) : Intent()
        data class SelectSkill(val skill: Skill) : Intent()
        data object Retry : Intent()
    }
    
    sealed class Effect : UiEffect {
        data class NavigateToSkillDetail(val skillId: String) : Effect()
        data class ShowError(val message: String) : Effect()
    }
}

// ViewModel implementation
class SkillListViewModel(
    private val getSkillsUseCase: GetSkillsUseCase,
    private val searchSkillsUseCase: SearchSkillsUseCase
) : BaseViewModel<SkillListContract.State, SkillListContract.Intent, SkillListContract.Effect>(
    SkillListContract.State()
) {
    
    init {
        handleIntent(SkillListContract.Intent.LoadSkills)
    }
    
    override fun handleIntent(intent: SkillListContract.Intent) {
        when (intent) {
            is SkillListContract.Intent.LoadSkills -> loadSkills()
            is SkillListContract.Intent.SearchSkills -> searchSkills(intent.query)
            is SkillListContract.Intent.SelectSkill -> selectSkill(intent.skill)
            is SkillListContract.Intent.Retry -> retry()
        }
    }
    
    private fun loadSkills() {
        viewModelScope.launch {
            getSkillsUseCase()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            updateState(currentState().copy(isLoading = true, error = null))
                        }
                        is Result.Success -> {
                            updateState(
                                currentState().copy(
                                    skills = result.data,
                                    isLoading = false,
                                    error = null
                                )
                            )
                        }
                        is Result.Error -> {
                            updateState(
                                currentState().copy(
                                    isLoading = false,
                                    error = result.exception.message
                                )
                            )
                        }
                    }
                }
        }
    }
    
    private fun selectSkill(skill: Skill) {
        viewModelScope.launch {
            sendEffect(SkillListContract.Effect.NavigateToSkillDetail(skill.id))
        }
    }
}
```

#### Composable Screen Implementation:
```kotlin
@Composable
fun SkillListScreen(
    onSkillClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SkillListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SkillListContract.Effect.NavigateToSkillDetail -> {
                    onSkillClick(effect.skillId)
                }
                is SkillListContract.Effect.ShowError -> {
                    // Handle error display
                }
            }
        }
    }
    
    SkillListContent(
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@Composable
private fun SkillListContent(
    state: SkillListContract.State,
    onIntent: (SkillListContract.Intent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { query ->
                onIntent(SkillListContract.Intent.SearchSkills(query))
            }
        )
        
        when {
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorMessage(
                message = state.error,
                onRetry = { onIntent(SkillListContract.Intent.Retry) }
            )
            else -> SkillList(
                skills = state.skills,
                onSkillClick = { skill ->
                    onIntent(SkillListContract.Intent.SelectSkill(skill))
                }
            )
        }
    }
}
```

## Design Patterns

### 1. MVI (Model-View-Intent) Pattern
- **Unidirectional Data Flow**: Intent → State Update → UI Render
- **Immutable State**: State objects are immutable data classes
- **Side Effects**: Navigation and one-time events handled via effects
- **Reactive**: UI reacts to state changes automatically

### 2. Repository Pattern
- **Interface Segregation**: Separate repository interfaces for different domains
- **Offline-First**: Local database as single source of truth
- **Data Synchronization**: Background sync with remote APIs
- **Error Handling**: Consistent error propagation via Result wrapper

### 3. Use Case Pattern
- **Single Responsibility**: Each use case handles one business operation
- **Dependency Injection**: Use cases receive dependencies via constructor
- **Coroutines Integration**: Async operations with proper error handling
- **Testability**: Easy to unit test business logic

### 4. Observer Pattern
- **Flow-based**: Use Kotlin Flow for reactive data streams
- **StateFlow**: For UI state management
- **SharedFlow**: For one-time events and effects

## Data Flow & State Management

### Offline-First Data Flow:
1. **UI Layer** requests data via **Use Cases**
2. **Repository** checks **Local Database** first
3. If data is stale/missing, fetch from **Remote API**
4. **Sync Manager** handles background synchronization
5. **Database** serves as single source of truth

### State Management Flow:
```
User Intent → ViewModel → Use Case → Repository → Database/API
                ↓
UI State ← ViewModel ← Result<T> ← Repository ← Data Source
```

### Result Wrapper Pattern:
```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

// Extension functions for easier handling
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) action(exception)
    return this
}
```

## Domain Models

### Core Domain Models Hierarchy:
```kotlin
// Base content class for polymorphism
abstract class Content {
    abstract val id: String
    abstract val title: String
    abstract val description: String
    abstract val createdAt: Long
    abstract val updatedAt: Long
}

// Learning skill containing multiple topics
data class Skill(
    override val id: String,
    override val title: String,
    override val description: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    val topics: List<Topic>,
    val difficulty: Difficulty,
    val estimatedHours: Int,
    val tags: List<String>,
    val prerequisites: List<String>,
    val progress: Float = 0f
) : Content()

// Individual topic within a skill
data class Topic(
    override val id: String,
    override val title: String,
    override val description: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    val skillId: String,
    val materials: List<Material>,
    val practiceSet: PracticeSet?,
    val order: Int,
    val isCompleted: Boolean = false
) : Content()

// Learning material (text, code, video)
data class Material(
    override val id: String,
    override val title: String,
    override val description: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    val topicId: String,
    val type: MaterialType,
    val content: String,
    val duration: Int? = null,
    val order: Int
) : Content()

// Practice question set
data class PracticeSet(
    override val id: String,
    override val title: String,
    override val description: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    val topicId: String,
    val questions: List<Quiz>,
    val timeLimit: Int? = null,
    val passingScore: Float = 0.7f
) : Content()

// Individual quiz question
data class Quiz(
    override val id: String,
    override val title: String,
    override val description: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    val practiceSetId: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String,
    val difficulty: Difficulty,
    val points: Int = 10
) : Content()

// Enums and value objects
enum class Difficulty { BEGINNER, INTERMEDIATE, ADVANCED }
enum class MaterialType { TEXT, CODE, VIDEO, INTERACTIVE }
```

## Error Handling Strategy

### Structured Error Hierarchy:
```kotlin
sealed class AppError : Exception() {
    abstract val userMessage: String
    
    sealed class NetworkError : AppError() {
        override val userMessage: String get() = "Network connection problem"
        
        data object NoConnection : NetworkError()
        data object Timeout : NetworkError()
        data object ServerError : NetworkError()
        data object SyncFailed : NetworkError()
    }
    
    sealed class DataError : AppError() {
        override val userMessage: String get() = "Data access problem"
        
        data object DatabaseError : DataError()
        data object CacheError : DataError()
        data object ParseError : DataError()
        data object NotFound : DataError()
    }
    
    sealed class BusinessError : AppError() {
        override val userMessage: String get() = "Operation not allowed"
        
        data object InvalidInput : BusinessError()
        data object AccessDenied : BusinessError()
        data object QuotaExceeded : BusinessError()
    }
    
    sealed class UIError : AppError() {
        override val userMessage: String get() = "Display problem"
        
        data object RenderError : UIError()
        data object NavigationError : UIError()
    }
}

// Error mapping utilities
fun Throwable.toAppError(): AppError = when (this) {
    is SocketTimeoutException -> AppError.NetworkError.Timeout
    is UnknownHostException -> AppError.NetworkError.NoConnection
    is SQLException -> AppError.DataError.DatabaseError
    is JsonException -> AppError.DataError.ParseError
    else -> AppError.DataError.DatabaseError
}
```

## Dependency Injection (Koin)

### Module Organization:
```kotlin
// Core application modules
val coreModule = module {
    // Dispatchers
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    
    // Database
    single<JetCodeDatabase> {
        Room.databaseBuilder(
            androidContext(),
            JetCodeDatabase::class.java,
            "jetcode_database"
        ).build()
    }
    
    // DAOs
    single<LearningDao> { get<JetCodeDatabase>().learningDao() }
    
    // HTTP Client
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }
    
    // API Services
    single<LearningApiService> { LearningApiServiceImpl(get()) }
    
    // Sync Manager
    single<SyncManager> { SyncManagerImpl(get()) }
}

// Repository modules
val repositoryModule = module {
    single<LearningRepository> {
        LearningRepositoryImpl(
            learningDao = get(),
            apiService = get(),
            syncManager = get()
        )
    }
    
    single<UserRepository> {
        UserRepositoryImpl(
            userDao = get(),
            preferencesManager = get()
        )
    }
}

// Use case modules
val useCaseModule = module {
    // Learning use cases
    factory { GetSkillsUseCase(get(), get()) }
    factory { GetTopicsBySkillIdUseCase(get(), get()) }
    factory { SearchSkillsUseCase(get(), get()) }
    factory { UpdateSkillProgressUseCase(get(), get()) }
    
    // User use cases
    factory { GetUserProfileUseCase(get(), get()) }
    factory { UpdateUserPreferencesUseCase(get(), get()) }
}

// Feature-specific ViewModel modules
val learningModule = module {
    viewModel { SkillListViewModel(get(), get()) }
    viewModel { (skillId: String) -> 
        SkillDetailViewModel(skillId, get(), get()) 
    }
    viewModel { (topicId: String) ->
        TopicDetailViewModel(topicId, get(), get())
    }
}

val practiceModule = module {
    viewModel { PracticeListViewModel(get()) }
    viewModel { (practiceSetId: String) ->
        PracticeDetailViewModel(practiceSetId, get(), get())
    }
}

val profileModule = module {
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
}

val onboardingModule = module {
    viewModel { OnboardingViewModel(get()) }
}

// Sync and background task modules
val syncModule = module {
    single<WorkManager> { WorkManager.getInstance(androidContext()) }
    single<SyncWorkScheduler> { SyncWorkSchedulerImpl(get()) }
}

// All modules combined for app initialization
val appModules = listOf(
    coreModule,
    repositoryModule,
    useCaseModule,
    learningModule,
    practiceModule,
    profileModule,
    onboardingModule,
    syncModule
)
```

### Dependency Declaration Patterns:
```kotlin
// Singletons for shared resources
single<Database> { /* database instance */ }
single<HttpClient> { /* HTTP client instance */ }
single<Repository> { /* repository implementation */ }

// Factories for use cases (new instance each time)
factory { GetDataUseCase(get(), get()) }

// ViewModels with proper scoping
viewModel { FeatureViewModel(get(), get()) }

// Parameterized dependencies
viewModel { (id: String) -> DetailViewModel(id, get()) }
```

## Navigation Architecture

### Centralized Navigation Setup:
```kotlin
// Navigation destinations
object JetCodeDestinations {
    const val ONBOARDING = "onboarding"
    const val SKILL_LIST = "skill_list"
    const val SKILL_DETAIL = "skill_detail/{skillId}"
    const val TOPIC_DETAIL = "topic_detail/{topicId}"
    const val PRACTICE_LIST = "practice_list"
    const val PRACTICE_DETAIL = "practice_detail/{practiceSetId}"
    const val PROFILE = "profile"
    
    fun skillDetail(skillId: String) = "skill_detail/$skillId"
    fun topicDetail(topicId: String) = "topic_detail/$topicId"
    fun practiceDetail(practiceSetId: String) = "practice_detail/$practiceSetId"
}

// Main navigation host
@Composable
fun JetCodeNavHost(
    navController: NavHostController,
    startDestination: String = JetCodeDestinations.SKILL_LIST
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding flow
        composable(JetCodeDestinations.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(JetCodeDestinations.SKILL_LIST) {
                        popUpTo(JetCodeDestinations.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        
        // Main learning flow
        composable(JetCodeDestinations.SKILL_LIST) {
            SkillListScreen(
                onSkillClick = { skillId ->
                    navController.navigate(JetCodeDestinations.skillDetail(skillId))
                },
                onNavigateToProfile = {
                    navController.navigate(JetCodeDestinations.PROFILE)
                }
            )
        }
        
        composable(
            route = JetCodeDestinations.SKILL_DETAIL,
            arguments = listOf(navArgument("skillId") { type = NavType.StringType })
        ) { backStackEntry ->
            val skillId = backStackEntry.arguments?.getString("skillId") ?: ""
            SkillDetailScreen(
                skillId = skillId,
                onTopicClick = { topicId ->
                    navController.navigate(JetCodeDestinations.topicDetail(topicId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Practice flow
        composable(JetCodeDestinations.PRACTICE_LIST) {
            PracticeListScreen(
                onPracticeSetClick = { practiceSetId ->
                    navController.navigate(JetCodeDestinations.practiceDetail(practiceSetId))
                }
            )
        }
        
        // Profile
        composable(JetCodeDestinations.PROFILE) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
```

### Navigation Pattern in Screens:
```kotlin
// Navigation handled via effects in ViewModels
sealed class NavigationEffect : UiEffect {
    data class NavigateToSkillDetail(val skillId: String) : NavigationEffect()
    data class NavigateToTopicDetail(val topicId: String) : NavigationEffect()
    data object NavigateBack : NavigationEffect()
}

// Screen implementation
@Composable
fun SkillListScreen(
    onSkillClick: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: SkillListViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SkillListContract.Effect.NavigateToSkillDetail -> {
                    onSkillClick(effect.skillId)
                }
                is SkillListContract.Effect.NavigateToProfile -> {
                    onNavigateToProfile()
                }
            }
        }
    }
    
    // Screen content...
}
```

## Code Generation Guidelines

### 1. When Creating New Features

#### Step 1: Domain Layer
```kotlin
// 1. Create domain model in domain/model/
data class {Entity}(
    override val id: String,
    override val title: String,
    override val description: String,
    override val createdAt: Long,
    override val updatedAt: Long,
    // entity-specific properties
) : Content()

// 2. Add repository interface in domain/repository/
interface {Domain}Repository {
    fun get{Entities}(): Flow<Result<List<{Entity}>>>
    fun get{Entity}ById(id: String): Flow<Result<{Entity}>>
    suspend fun sync{Entities}(): Result<Unit>
}

// 3. Create use cases in domain/usecase/
class Get{Entities}UseCase(
    private val repository: {Domain}Repository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(): Flow<Result<List<{Entity}>>> = 
        repository.get{Entities}()
            .flowOn(dispatcherProvider.io)
            .catch { e ->
                emit(Result.Error(e.toAppError()))
            }
}
```

#### Step 2: Data Layer
```kotlin
// 1. Create database entity in data/database/entity/
@Entity(tableName = "{entities}")
data class {Entity}Entity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val createdAt: Long,
    val updatedAt: Long,
    // entity-specific columns
)

// 2. Add DAO methods in data/database/dao/
@Dao
interface {Domain}Dao {
    @Query("SELECT * FROM {entities} ORDER BY title ASC")
    fun getAll{Entities}(): Flow<List<{Entity}Entity>>
    
    @Query("SELECT * FROM {entities} WHERE id = :id")
    suspend fun get{Entity}ById(id: String): {Entity}Entity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert{Entities}(entities: List<{Entity}Entity>)
}

// 3. Create API service in data/remote/api/
interface {Domain}ApiService {
    suspend fun get{Entities}(): List<{Entity}Dto>
    suspend fun get{Entity}ById(id: String): {Entity}Dto
}

// 4. Create mappers in data/repository/mapper/
fun {Entity}Entity.toDomain(): {Entity} = {Entity}(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    // map entity-specific fields
)

fun {Entity}.toEntity(): {Entity}Entity = {Entity}Entity(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    // map domain-specific fields
)

// 5. Implement repository in data/repository/repository/
class {Domain}RepositoryImpl(
    private val {domain}Dao: {Domain}Dao,
    private val apiService: {Domain}ApiService,
    private val syncManager: SyncManager
) : {Domain}Repository {
    
    override fun get{Entities}(): Flow<Result<List<{Entity}>>> = flow {
        emit(Result.Loading)
        
        {domain}Dao.getAll{Entities}()
            .map { entities -> entities.map { it.toDomain() } }
            .catch { e -> emit(Result.Error(e.toAppError())) }
            .collect { entities ->
                emit(Result.Success(entities))
                
                if (syncManager.shouldSync()) {
                    sync{Entities}()
                }
            }
    }
    
    override suspend fun sync{Entities}(): Result<Unit> = try {
        val remote{Entities} = apiService.get{Entities}()
        {domain}Dao.insert{Entities}(remote{Entities}.map { it.toEntity() })
        syncManager.updateLastSyncTime()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e.toAppError())
    }
}
```

#### Step 3: Presentation Layer
```kotlin
// 1. Create MVI contract in features/{feature}/presentation/{screen}/
object {Feature}Contract {
    data class State(
        val {entities}: List<{Entity}> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val searchQuery: String = ""
    ) : UiState
    
    sealed class Intent : UiIntent {
        data object Load{Entities} : Intent()
        data class Search{Entities}(val query: String) : Intent()
        data class Select{Entity}(val {entity}: {Entity}) : Intent()
        data object Retry : Intent()
    }
    
    sealed class Effect : UiEffect {
        data class NavigateTo{Entity}Detail(val {entity}Id: String) : Effect()
        data class ShowError(val message: String) : Effect()
    }
}

// 2. Create ViewModel
class {Feature}ViewModel(
    private val get{Entities}UseCase: Get{Entities}UseCase,
    private val search{Entities}UseCase: Search{Entities}UseCase
) : BaseViewModel<{Feature}Contract.State, {Feature}Contract.Intent, {Feature}Contract.Effect>(
    {Feature}Contract.State()
) {
    
    init {
        handleIntent({Feature}Contract.Intent.Load{Entities})
    }
    
    override fun handleIntent(intent: {Feature}Contract.Intent) {
        when (intent) {
            is {Feature}Contract.Intent.Load{Entities} -> load{Entities}()
            is {Feature}Contract.Intent.Search{Entities} -> search{Entities}(intent.query)
            is {Feature}Contract.Intent.Select{Entity} -> select{Entity}(intent.{entity})
            is {Feature}Contract.Intent.Retry -> retry()
        }
    }
    
    private fun load{Entities}() {
        viewModelScope.launch {
            get{Entities}UseCase()
                .collect { result ->
                    when (result) {
                        is Result.Loading -> updateState(currentState().copy(isLoading = true))
                        is Result.Success -> updateState(
                            currentState().copy(
                                {entities} = result.data,
                                isLoading = false,
                                error = null
                            )
                        )
                        is Result.Error -> updateState(
                            currentState().copy(
                                isLoading = false,
                                error = result.exception.userMessage
                            )
                        )
                    }
                }
        }
    }
    
    private fun select{Entity}({entity}: {Entity}) {
        viewModelScope.launch {
            sendEffect({Feature}Contract.Effect.NavigateTo{Entity}Detail({entity}.id))
        }
    }
}

// 3. Create Composable screen
@Composable
fun {Feature}Screen(
    on{Entity}Click: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: {Feature}ViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is {Feature}Contract.Effect.NavigateTo{Entity}Detail -> {
                    on{Entity}Click(effect.{entity}Id)
                }
                is {Feature}Contract.Effect.ShowError -> {
                    // Handle error display
                }
            }
        }
    }
    
    {Feature}Content(
        state = state,
        onIntent = viewModel::handleIntent
    )
}

// 4. Add to DI module
val {feature}Module = module {
    factory { Get{Entities}UseCase(get(), get()) }
    factory { Search{Entities}UseCase(get(), get()) }
    viewModel { {Feature}ViewModel(get(), get()) }
}

// 5. Add to navigation
composable(
    route = "{feature}_list",
    arguments = listOf(/* navigation arguments */)
) {
    {Feature}Screen(
        on{Entity}Click = { {entity}Id ->
            navController.navigate("{entity}_detail/${{entity}Id}")
        },
        onNavigateBack = { navController.popBackStack() }
    )
}
```

### 2. Naming Conventions

#### Package Structure:
- `com.appsbase.jetcode.{layer}.{module}.{feature}`
- Example: `com.appsbase.jetcode.feature.learning.presentation.skill_list`

#### Class Naming:
- **ViewModels**: `{Feature}ViewModel` (e.g., `SkillListViewModel`)
- **Use Cases**: `{Action}{Entity}UseCase` (e.g., `GetSkillsUseCase`)
- **Repositories**: `{Domain}Repository{Impl}` (e.g., `LearningRepositoryImpl`)
- **Entities**: `{Entity}Entity` (e.g., `SkillEntity`)
- **DTOs**: `{Entity}Dto` (e.g., `SkillDto`)
- **Screens**: `{Feature}Screen` (e.g., `SkillListScreen`)
- **DAOs**: `{Domain}Dao` (e.g., `LearningDao`)

#### File Naming:
- **Contracts**: `{Feature}Contract.kt` for MVI state/intent/effect definitions
- **Mappers**: `{Domain}Mappers.kt` for entity ↔ model conversions
- **Modules**: `{Feature}Module.kt` for Koin DI modules
- **API Services**: `{Domain}ApiService.kt` for HTTP API interfaces

### 3. Required Patterns

#### MVI State Updates:
```kotlin
// Always use updateState() in ViewModel
private fun handleLoadData() {
    updateState(currentState().copy(isLoading = true))
    viewModelScope.launch {
        // async operation
        updateState(currentState().copy(isLoading = false, data = result))
    }
}
```

#### Error Handling:
```kotlin
// Always map exceptions to AppError types
.catch { e ->
    emit(Result.Error(e.toAppError()))
}
```

#### Navigation:
```kotlin
// Use effect-based navigation
sealed class {Feature}Effect : UiEffect {
    data class NavigateTo{Screen}(val id: String) : {Feature}Effect()
    data object NavigateBack : {Feature}Effect()
}
```

#### Use Case Implementation:
```kotlin
// Standard use case pattern
class {Action}{Entity}UseCase(
    private val repository: {Domain}Repository,
    private val dispatcherProvider: DispatcherProvider
) {
    operator fun invoke(/* parameters */): Flow<Result<{ReturnType}>> = 
        repository.{action}{Entity}(/* parameters */)
            .flowOn(dispatcherProvider.io)
            .catch { e ->
                emit(Result.Error(e.toAppError()))
            }
}
```
