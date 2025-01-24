package org.comon.moviefriends.presentation.screen.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.BuildConfig
import org.comon.moviefriends.R
import org.comon.moviefriends.data.entity.worldcup.ResponseMovieWorldCupItemListDto
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.MFTitle

@Composable
fun WorldCupWinnerScreen(
    worldCupItem: ResponseMovieWorldCupItemListDto.ResponseMovieWorldCupItemDto?,
    navigateToWorldCupScreen: () -> Unit,
){
    if(worldCupItem!=null){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize()
        ) {
            MFTitle(text = "우승자!")
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${BuildConfig.BASE_WORLDCUP_URL}${worldCupItem.itemImage}")
                        .crossfade(true)
                        .error(R.drawable.logo)
                        .build(),
                    contentDescription = "월드컵 우승 항목 사진",
                    contentScale = ContentScale.Fit,
                )
                MFTitle(text = worldCupItem.description)
            }
            MFButton(
                clickEvent = { navigateToWorldCupScreen() },
                text = "메인으로 돌아가기"
            )
        }
    }else{
        AlertDialog(
            title = { Text(text = "에러") },
            text = { Text(text = "이전 화면으로 돌아갑니다.") },
            onDismissRequest = { navigateToWorldCupScreen() },
            confirmButton = {
                Button(
                    onClick = {
                        navigateToWorldCupScreen()
                    }
                ) {
                    Text(text = "확인")
                }
            },
        )
    }
}