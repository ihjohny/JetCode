package com.appsbase.jetcode.feature.learning.presentation.dashboard

import com.appsbase.jetcode.core.common.Result
import com.appsbase.jetcode.domain.model.Difficulty
import com.appsbase.jetcode.domain.model.Skill
import com.appsbase.jetcode.domain.usecase.GetSkillsUseCase
import com.appsbase.jetcode.domain.usecase.SyncContentUseCase
import com.appsbase.jetcode.feature.learning.presentation.skill_list.SkillListIntent
import com.appsbase.jetcode.feature.learning.presentation.skill_list.SkillListViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for LearningDashboardViewModel demonstrating MVI testing patterns
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LearningDashboardViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val mockGetSkillsUseCase: GetSkillsUseCase = mockk()
    private val mockSyncContentUseCase: SyncContentUseCase = mockk()

    private lateinit var viewModel: SkillListViewModel

    private val sampleSkills = listOf(
        Skill(
            id = "kotlin-basics",
            name = "Kotlin Basics",
            description = "Learn the fundamentals of Kotlin",
            iconUrl = null,
            difficulty = Difficulty.BEGINNER,
            estimatedDuration = 120,
            progress = 0.5f
        ),
        Skill(
            id = "jetpack-compose",
            name = "Jetpack Compose",
            description = "Modern UI toolkit for Android",
            iconUrl = null,
            difficulty = Difficulty.INTERMEDIATE,
            estimatedDuration = 180,
            progress = 0.2f
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when loadSkills succeeds, state should be updated with skills`() = runTest {
        // Given
        every { mockGetSkillsUseCase() } returns flowOf(Result.Success(sampleSkills))

        // When
        viewModel = SkillListViewModel(mockGetSkillsUseCase, mockSyncContentUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(sampleSkills, state.skills)
        assertEquals(sampleSkills, state.filteredSkills)
        assertEquals(null, state.error)
    }

    @Test
    fun `when loadSkills fails, state should be updated with error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        every { mockGetSkillsUseCase() } returns flowOf(Result.Error(exception))

        // When
        viewModel = SkillListViewModel(mockGetSkillsUseCase, mockSyncContentUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.skills.isEmpty())
        assertEquals("Network error", state.error)
    }

    @Test
    fun `when searchSkills is called, filteredSkills should be updated`() = runTest {
        // Given
        every { mockGetSkillsUseCase() } returns flowOf(Result.Success(sampleSkills))
        viewModel = SkillListViewModel(mockGetSkillsUseCase, mockSyncContentUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.handleIntent(SkillListIntent.SearchSkills("Kotlin"))
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals("Kotlin", state.searchQuery)
        assertEquals(1, state.filteredSkills.size)
        assertEquals("kotlin-basics", state.filteredSkills.first().id)
    }

    @Test
    fun `when refreshSkills succeeds, state should be updated with new skills`() = runTest {
        // Given
        every { mockGetSkillsUseCase() } returns flowOf(Result.Success(sampleSkills))
        coEvery { mockSyncContentUseCase() } returns Result.Success(Unit)
        viewModel = SkillListViewModel(mockGetSkillsUseCase, mockSyncContentUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.handleIntent(SkillListIntent.RefreshSkills)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertFalse(state.isSyncing)
        assertEquals(sampleSkills, state.skills)
    }
}
