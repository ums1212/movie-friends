package org.comon.moviefriends.presentation.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ColumnScope.WorldCupItemBox(
    position: WorldCupItemPosition,
    selectedPosition: SelectedWorldCupItemPosition,
    selectPosition: (SelectedWorldCupItemPosition) -> Unit,
){
    val transition = updateTransition(targetState = selectedPosition, label = "Transition")

    val boxWeight by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500) },
        label = "boxWeight"
    ) { state ->
        when(state){
            SelectedWorldCupItemPosition.TOP -> {
                when(position){
                    WorldCupItemPosition.TOP -> 1f
                    WorldCupItemPosition.BOTTOM -> 0.0f
                }
            }
            SelectedWorldCupItemPosition.BOTTOM -> {
                when(position){
                    WorldCupItemPosition.TOP -> 0.0f
                    WorldCupItemPosition.BOTTOM -> 1f
                }
            }
            else -> 1f
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .weight(boxWeight)
        .background(
            when (position) {
                WorldCupItemPosition.TOP -> Color.Red
                WorldCupItemPosition.BOTTOM -> Color.Blue
            }
        )
        .clickable(onClick = {
            selectPosition(when (position) {
                WorldCupItemPosition.TOP -> {
                    SelectedWorldCupItemPosition.TOP
                }

                WorldCupItemPosition.BOTTOM -> {
                    SelectedWorldCupItemPosition.BOTTOM
                }
            })
        })
    )
}

enum class WorldCupItemPosition {
    TOP,
    BOTTOM,
}

enum class SelectedWorldCupItemPosition {
    TOP,
    BOTTOM,
    NONE,
}