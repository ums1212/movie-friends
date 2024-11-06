package org.comon.moviefriends.presenter.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey


@Composable
fun ShimmerEffect(
    modifier: Modifier,
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,

    ) {

    val shimmerColors = listOf(
        FriendsBoxGrey.copy(alpha = 0.3f),
        FriendsBoxGrey.copy(alpha = 0.5f),
        FriendsBoxGrey.copy(alpha = 1.0f),
        FriendsBoxGrey.copy(alpha = 0.5f),
        FriendsBoxGrey.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
        end = Offset(x = translateAnimation.value, y = angleOfAxisY),
    )

    Box(
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }

}

@Composable
fun MovieDetailShimmer() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShimmerEffect(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
        )
        Spacer(Modifier.padding(vertical = 12.dp))
        ShimmerEffect(modifier = Modifier
            .width(200.dp)
            .height(25.dp)
            .padding(bottom = 4.dp)
        )
        ShimmerEffect(modifier = Modifier
            .width(200.dp)
            .height(25.dp)
            .padding(bottom = 4.dp)
        )
        ShimmerEffect(modifier = Modifier
            .width(200.dp)
            .height(25.dp)
            .padding(bottom = 4.dp)
        )
    }
}

@Composable
fun MovieCreditShimmer() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShimmerEffect(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
        )
        Spacer(Modifier.padding(vertical = 12.dp))
    }
}