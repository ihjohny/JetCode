package com.appsbase.jetcode.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.navigation.JetCodeDestinations
import com.appsbase.jetcode.navigation.JetCodeNavHost
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Main activity for JetCode app
 * Entry point with navigation setup and theme application
 */
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JetCodeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainContent(mainViewModel = mainViewModel)
                }
            }
        }
    }
}

@Composable
private fun MainContent(mainViewModel: MainViewModel) {
    val state by mainViewModel.uiState.collectAsState()

    if (state.shouldShowOnboarding != null) {
        val startDestination = if (state.shouldShowOnboarding == true) {
            JetCodeDestinations.Onboarding.route
        } else {
            JetCodeDestinations.Dashboard.route
        }

        JetCodeNavHost(
            startDestination = startDestination,
            onOnboardingComplete = {
                mainViewModel.handleIntent(MainIntent.CompleteOnboarding)
            },
        )
    }
}
