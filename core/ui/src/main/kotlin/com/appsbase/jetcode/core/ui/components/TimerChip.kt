package com.appsbase.jetcode.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TimerOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * A timer chip component that shows elapsed time
 * Can display live time or static time based on parameters
 * @param timeMilliseconds Time in milliseconds - either startTime for live mode or totalTime for static mode
 * @param isCountDown If true, shows static time; if false, shows live elapsed time
 */
@Composable
fun TimerChip(
    modifier: Modifier = Modifier,
    timeMilliseconds: Long? = null,
    isCountDown: Boolean = false,
) {
    val timeText = when {
        timeMilliseconds != null && !isCountDown -> {
            // Static mode - show fixed total time
            val totalSeconds = (timeMilliseconds / 1000).toInt()
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            "%02d:%02d".format(minutes, seconds)
        }

        timeMilliseconds != null && isCountDown -> {
            // Live mode - show elapsed time from start
            var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

            LaunchedEffect(timeMilliseconds) {
                while (true) {
                    currentTime = System.currentTimeMillis()
                    delay(1000)
                }
            }

            val elapsedSeconds = ((currentTime - timeMilliseconds) / 1000).toInt()
            val minutes = elapsedSeconds / 60
            val seconds = elapsedSeconds % 60
            "%02d:%02d".format(minutes, seconds)
        }

        else -> "00:00"
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = if (isCountDown) Icons.Default.Timer else Icons.Default.TimerOff,
            contentDescription = "Timer",
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = timeText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
