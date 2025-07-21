package com.appsbase.jetcode.feature.learning.presentation.skill_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.core.ui.components.SkillCard
import org.koin.androidx.compose.koinViewModel

/**
 * Learning Dashboard Screen - Main screen showing available skills
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillListScreen(
    onSkillClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SkillListViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Handle side effects
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SkillListEffect.NavigateToSkillDetail -> {
                    onSkillClick(effect.skillId)
                }

                is SkillListEffect.NavigateToProfile -> {
                    onProfileClick()
                }

                is SkillListEffect.ShowError -> {
                    // Handle error - could show snackbar
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(title = {
            Text(
                text = "JetCode Learning", fontWeight = FontWeight.Bold
            )
        }, actions = {
            IconButton(
                onClick = { viewModel.handleIntent(SkillListIntent.ProfileClicked) },
            ) {
                Icon(
                    imageVector = Icons.Default.Person, contentDescription = "Profile"
                )
            }
        })

        when {
            state.isLoading && state.skills.isEmpty() -> {
                LoadingState(
                    modifier = Modifier.fillMaxSize(), message = "Loading skills..."
                )
            }

            state.error != null && state.skills.isEmpty() -> {
                ErrorState(
                    message = state.error ?: "Unknown error",
                    onRetry = { viewModel.handleIntent(SkillListIntent.RetryClicked) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                LearningContent(
                    state = state,
                    onIntent = viewModel::handleIntent,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun LearningContent(
    state: SkillListState, onIntent: (SkillListIntent) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.searchQuery, onValueChange = { query ->
            onIntent(SkillListIntent.SearchSkills(query))
        }, label = { Text("Search skills...") }, leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search"
            )
        }, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), singleLine = true
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isSyncing) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                if (state.filteredSkills.isEmpty() && state.searchQuery.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No skills found for \"${state.searchQuery}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                } else {
                    items(
                        items = state.filteredSkills, key = { it.id }) { skill ->
                        SkillCard(
                            skill = skill, onClick = {
                                onIntent(SkillListIntent.SkillClicked(skill.id))
                            })
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { onIntent(SkillListIntent.SyncSkills) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSyncing,
                    ) {
                        if (state.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp), strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(if (state.isSyncing) "Syncing..." else "Sync Content")
                    }
                }
            }
        }
    }
}
