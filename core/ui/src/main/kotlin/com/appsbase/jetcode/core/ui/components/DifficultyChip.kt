package com.appsbase.jetcode.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.appsbase.jetcode.core.domain.model.Difficulty
import com.appsbase.jetcode.core.designsystem.theme.JetCodeTheme

@Composable
fun DifficultyChip(
    difficulty: Difficulty,
    modifier: Modifier = Modifier
) {
    val (color, text) = when (difficulty) {
        Difficulty.BEGINNER -> JetCodeTheme.extendedColors.difficultyBeginner to "Beginner"
        Difficulty.INTERMEDIATE -> JetCodeTheme.extendedColors.difficultyIntermediate to "Intermediate"
        Difficulty.ADVANCED -> JetCodeTheme.extendedColors.difficultyAdvanced to "Advanced"
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}
