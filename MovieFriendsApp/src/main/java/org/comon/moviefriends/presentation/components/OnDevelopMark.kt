package org.comon.moviefriends.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.comon.moviefriends.R

@Preview
@Composable
fun OnDevelopMark() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "공사 중...",
            color = Color.White,
            style = TextStyle(
                fontSize = 24.sp
            )
        )
        Image(
            modifier = Modifier
                .size(256.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            painter = painterResource(R.drawable.yoshicat),
            contentDescription = "업데이트 예정"
        )
    }
}