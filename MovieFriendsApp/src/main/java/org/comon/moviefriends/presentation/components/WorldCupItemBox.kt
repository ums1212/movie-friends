package org.comon.moviefriends.presentation.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.BuildConfig
import org.comon.moviefriends.R
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto

@Composable
fun ColumnScope.WorldCupItemBox(
    position: WorldCupItemPosition,
    item: ResponseMovieWorldCupItemListDto.ResponseMovieWorldCupItemDto,
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
                    WorldCupItemPosition.BOTTOM -> 0.01f
                }
            }
            SelectedWorldCupItemPosition.BOTTOM -> {
                when(position){
                    WorldCupItemPosition.TOP -> 0.01f
                    WorldCupItemPosition.BOTTOM -> 1f
                }
            }
            else -> 1f
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .weight(boxWeight)
        .clickable(onClick = {
            selectPosition(
                when (position) {
                    WorldCupItemPosition.TOP -> {
                        SelectedWorldCupItemPosition.TOP
                    }

                    WorldCupItemPosition.BOTTOM -> {
                        SelectedWorldCupItemPosition.BOTTOM
                    }
                }
            )
        }),
        contentAlignment = if(position == WorldCupItemPosition.TOP) Alignment.TopCenter else Alignment.BottomCenter
    ){
        AsyncImage(
            modifier = Modifier
                .fillMaxHeight(),
            model = ImageRequest.Builder(LocalContext.current)
                .data("${BuildConfig.BASE_WORLDCUP_URL}${item.itemImage}")
                .crossfade(true)
                .error(R.drawable.logo)
                .build(),
            contentDescription = "작품 이미지",
            contentScale = ContentScale.Crop
        )
        WorldCupTitleText(text = item.description)
    }
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