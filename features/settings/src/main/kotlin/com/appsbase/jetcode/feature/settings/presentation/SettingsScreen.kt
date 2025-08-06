package com.appsbase.jetcode.feature.settings.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme
import com.appsbase.jetcode.core.ui.components.CommonTopAppBar
import com.appsbase.jetcode.core.ui.components.ErrorState
import com.appsbase.jetcode.core.ui.components.LoadingState
import com.appsbase.jetcode.domain.model.AppThemeSettings
import com.appsbase.jetcode.domain.model.ColorPalette
import com.appsbase.jetcode.domain.model.FontFamily
import com.appsbase.jetcode.domain.model.ThemeMode
import org.koin.androidx.compose.koinViewModel

/**
 * Ultra-modern Settings Screen - Slick mobile-first UX design with grid layouts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle effects
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SettingsEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message, actionLabel = "Try Again"
                    )
                }

                is SettingsEffect.ShowAutoSaveSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = "✨ Settings saved", actionLabel = null
                    )
                }

                is SettingsEffect.ShowResetSuccess -> {
                    snackbarHostState.showSnackbar(
                        message = "🔄 Reset to defaults", actionLabel = "OK"
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
        CommonTopAppBar(
            title = "Appearance", onNavigateBack = onBackClick, actions = {
                IconButton(
                    onClick = { viewModel.handleIntent(SettingsIntent.ResetToDefaults) }) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset settings",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            })
    }, snackbarHost = { SnackbarHost(snackbarHostState) }, modifier = modifier
    ) { paddingValues ->
        when {
            state.isLoading && state.themeSettings == AppThemeSettings() -> {
                LoadingState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            state.error != null && state.themeSettings == AppThemeSettings() -> {
                ErrorState(
                    message = state.error ?: "Something went wrong",
                    onRetry = { viewModel.handleIntent(SettingsIntent.RetryLoad) },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            else -> {
                SlickSettingsContent(
                    state = state,
                    onIntent = viewModel::handleIntent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun SlickSettingsContent(
    state: SettingsState, onIntent: (SettingsIntent) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                )
            )
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Section 1: Appearance Settings
        item {
            SettingsGroup(
                title = "🎨 Appearance", content = {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Theme Mode
                        SlickThemeModeSection(
                            currentMode = state.themeSettings.themeMode,
                            onModeChange = { onIntent(SettingsIntent.UpdateThemeMode(it)) })
                    }
                })
        }

        // Section 2: Color Settings
        item {
            SettingsGroup(
                title = "🌈 Colors", content = {
                    SlickColorGridSection(
                        currentPalette = state.themeSettings.colorPalette,
                        onPaletteChange = { onIntent(SettingsIntent.UpdateColorPalette(it)) })
                })
        }

        // Section 3: Typography Settings
        item {
            SettingsGroup(
                title = "📝 Typography", content = {
                    SlickFontSection(
                        currentFont = state.themeSettings.fontFamily,
                        onFontChange = { onIntent(SettingsIntent.UpdateFontFamily(it)) })
                })
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SettingsGroup(
    title: String, content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            content()
        }
    }
}

@Composable
private fun SlickThemeModeSection(
    currentMode: ThemeMode, onModeChange: (ThemeMode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ThemeMode.entries.forEach { mode ->
            SlickThemeOption(
                mode = mode, isSelected = mode == currentMode, onSelect = { onModeChange(mode) })
        }
    }
}

@Composable
private fun SlickThemeOption(
    mode: ThemeMode, isSelected: Boolean, onSelect: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f, // Reduced from 1.03f
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f), label = "themeScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .shadow(
                elevation = if (isSelected) 6.dp else 1.dp, // Reduced from 8dp
                shape = RoundedCornerShape(16.dp), // Reduced from 20dp
                spotColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            )
            .selectable(
                selected = isSelected, onClick = onSelect, role = Role.RadioButton
            ), colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            }
        ), shape = RoundedCornerShape(16.dp) // Reduced from 20dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Reduced from 24dp
            horizontalArrangement = Arrangement.spacedBy(14.dp), // Reduced from 20dp
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Glassmorphic icon container
            Box(
                modifier = Modifier
                    .size(44.dp) // Reduced from 56dp
                    .background(
                        brush = if (isSelected) {
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                )
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.05f)
                                )
                            )
                        }, shape = CircleShape
                    )
                    .border(
                        width = 1.dp, color = if (isSelected) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        }, shape = CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (mode) {
                        ThemeMode.LIGHT -> Icons.Default.LightMode
                        ThemeMode.DARK -> Icons.Default.DarkMode
                        ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
                    }, contentDescription = null, tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }, modifier = Modifier.size(22.dp) // Reduced from 28dp
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (mode) {
                        ThemeMode.LIGHT -> "Light Theme"
                        ThemeMode.DARK -> "Dark Theme"
                        ThemeMode.SYSTEM -> "System Auto"
                    }, style = MaterialTheme.typography.titleMedium, // Reduced from titleLarge
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = when (mode) {
                        ThemeMode.LIGHT -> "Bright and clean interface"
                        ThemeMode.DARK -> "Easy on the eyes in low light"
                        ThemeMode.SYSTEM -> "Follows your device setting"
                    }, style = MaterialTheme.typography.bodySmall, // Reduced from bodyMedium
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            // Custom animated radio button
            Box(
                modifier = Modifier
                    .size(20.dp) // Reduced from 24dp
                    .background(
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        }, shape = CircleShape
                    )
                    .border(
                        width = 2.dp, color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline
                        }, shape = CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isSelected,
                    enter = fadeIn(tween(200)) + slideInVertically(),
                    exit = fadeOut(tween(150))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(12.dp) // Reduced from 14dp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SlickColorGridSection(
    currentPalette: ColorPalette, onPaletteChange: (ColorPalette) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        maxItemsInEachRow = 4
    ) {
        ColorPalette.entries.forEach { palette ->
            SlickColorOption(
                palette = palette,
                isSelected = palette == currentPalette,
                onSelect = { onPaletteChange(palette) })
        }
    }
}

@Composable
private fun SlickColorOption(
    palette: ColorPalette, isSelected: Boolean, onSelect: () -> Unit
) {
    val colors = getColorPaletteColors(palette)
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f, // Slightly reduced from 1.2f
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f), label = "colorScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .clickable { onSelect() }
            .padding(4.dp)) {
        Box(
            modifier = Modifier
                .size(48.dp) // Reduced from 72dp
                .scale(animatedScale)
                .shadow(
                    elevation = if (isSelected) 4.dp else 2.dp, // Reduced from 12dp/4dp
                    shape = CircleShape, spotColor = colors.first().copy(alpha = 0.6f)
                )
                .clip(CircleShape)
                .border(
                    width = if (isSelected) 2.dp else 1.dp, // Reduced from 4dp/2dp
                    brush = if (isSelected) {
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent, Color.Transparent
                            )
                        )
                    }, shape = CircleShape
                )
                .background(
                    brush = Brush.sweepGradient(
                        colors = colors + colors.first(), // Complete the circle
                        center = androidx.compose.ui.geometry.Offset(0.5f, 0.5f)
                    ), shape = CircleShape
                ), contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(tween(300)) + slideInVertically { -it / 2 },
                exit = fadeOut(tween(200)) + slideOutVertically { it / 2 }) {
                Box(
                    modifier = Modifier
                        .size(24.dp) // Reduced from 32dp
                        .background(
                            color = Color.Black.copy(alpha = 0.4f), shape = CircleShape
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp) // Reduced from 20dp
                    )
                }
            }
        }

        Text(
            text = palette.name.replace("_", " ").lowercase().split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
            .replace("Theme", ""),
            style = MaterialTheme.typography.labelMedium, // Reduced from labelLarge
            textAlign = TextAlign.Center,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            maxLines = 2)
    }
}

@Composable
private fun SlickFontSection(
    currentFont: FontFamily, onFontChange: (FontFamily) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        FontFamily.entries.forEach { font ->
            SlickFontOption(
                font = font, isSelected = font == currentFont, onSelect = { onFontChange(font) })
        }
    }
}

@Composable
private fun SlickFontOption(
    font: FontFamily, isSelected: Boolean, onSelect: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.01f else 1f, // Reduced from 1.02f
        animationSpec = spring(dampingRatio = 0.7f), label = "fontScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(animatedScale)
            .shadow(
                elevation = if (isSelected) 4.dp else 1.dp, // Reduced from 6dp
                shape = RoundedCornerShape(12.dp), // Reduced from 16dp
                spotColor = if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
            )
            .clickable { onSelect() }, colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
            }
        ), shape = RoundedCornerShape(12.dp) // Reduced from 16dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp), // Reduced from 20dp
            horizontalArrangement = Arrangement.spacedBy(14.dp), // Reduced from 20dp
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Typography preview with gradient background
            Box(
                modifier = Modifier
                    .size(44.dp) // Reduced from 56dp
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (isSelected) {
                                listOf(
                                    MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                                    MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f)
                                )
                            } else {
                                listOf(
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.05f)
                                )
                            }
                        ), shape = RoundedCornerShape(10.dp) // Reduced from 12dp
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aa",
                    style = MaterialTheme.typography.headlineMedium, // Reduced from headlineLarge
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = font.name.replace("_", " ").lowercase().split(" ")
                        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } },
                    style = MaterialTheme.typography.titleMedium, // Reduced from titleLarge
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "The quick brown fox jumps over the lazy dog",
                    style = MaterialTheme.typography.bodySmall, // Reduced from bodyMedium
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 1
                )
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(20.dp) // Reduced from 24dp
                        .background(
                            color = MaterialTheme.colorScheme.secondary, shape = CircleShape
                        ), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(14.dp) // Reduced from 16dp
                    )
                }
            }
        }
    }
}

@Composable
private fun SlickSection(
    title: String, icon: ImageVector, content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) { // Reduced from 20dp
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp), // Reduced from 16dp
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp) // Reduced from 40dp
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                            )
                        ), shape = CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp) // Reduced from 24dp
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge, // Reduced from headlineSmall
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        content()
    }
}

// Helper function for color palette colors
@Composable
private fun getColorPaletteColors(palette: ColorPalette): List<Color> {
    return when (palette) {
        ColorPalette.BLUE_THEME -> listOf(
            Color(0xFF1976D2), Color(0xFF42A5F5), Color(0xFF90CAF9), Color(0xFFE3F2FD)
        )

        ColorPalette.GREEN_THEME -> listOf(
            Color(0xFF388E3C), Color(0xFF66BB6A), Color(0xFFA5D6A7), Color(0xFFE8F5E8)
        )

        ColorPalette.PURPLE_THEME -> listOf(
            Color(0xFF7B1FA2), Color(0xFFAB47BC), Color(0xFFCE93D8), Color(0xFFF3E5F5)
        )

        ColorPalette.ORANGE_THEME -> listOf(
            Color(0xFFF57C00), Color(0xFFFF9800), Color(0xFFFFCC02), Color(0xFFFFF3E0)
        )

        ColorPalette.PINK_THEME -> listOf(
            Color(0xFFD32F2F), Color(0xFFEF5350), Color(0xFFEF9A9A), Color(0xFFFFEBEE)
        )

        ColorPalette.TEAL_THEME -> listOf(
            Color(0xFF00796B), Color(0xFF26A69A), Color(0xFF80CBC4), Color(0xFFE0F2F1)
        )

        ColorPalette.INDIGO_THEME -> listOf(
            Color(0xFF3F51B5), Color(0xFF5C6BC0), Color(0xFF9FA8DA), Color(0xFFE8EAF6)
        )

        ColorPalette.CUSTOM -> listOf(
            Color(0xFF6200EE), Color(0xFFBB86FC), Color(0xFF3700B3), Color(0xFFF5F0FF)
        )
    }
}

@Preview(showBackground = true, name = "Slick Settings Screen")
@Composable
private fun SettingsScreenPreview() {
    JetCodeTheme {
        SlickSettingsContent(
            state = SettingsState(
                themeSettings = AppThemeSettings(
                    themeMode = ThemeMode.SYSTEM,
                    colorPalette = ColorPalette.BLUE_THEME,
                    fontFamily = FontFamily.DEFAULT
                )
            ), onIntent = {}, modifier = Modifier.fillMaxSize()
        )
    }
}
