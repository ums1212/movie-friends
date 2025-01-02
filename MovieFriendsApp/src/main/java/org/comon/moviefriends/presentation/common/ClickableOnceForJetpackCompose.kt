package org.comon.moviefriends.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import kotlinx.coroutines.delay

@SuppressLint("SuspiciousModifierThen")
fun Modifier.clickableOnce(
    onClick: () -> Unit,
): Modifier = this.then(
    composed(
        inspectorInfo = {
            name = "clickableOnce"
            value = onClick
        }
    ) {
        var enableAgain by remember { mutableStateOf(true) }
        LaunchedEffect(
            enableAgain,
            block = {
                if (enableAgain) return@LaunchedEffect
                delay(timeMillis = 500L)
                enableAgain = true
            },
        )
        Modifier.clickable {
            if (enableAgain) {
                enableAgain = false
                onClick()
            }
        }
    }
)