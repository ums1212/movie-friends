package org.comon.moviefriends.ui.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import org.comon.moviefriends.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailTopAppBar(navigatePop: () -> Unit, navigateToPostDetail: () -> Unit = {}, writePost: Boolean = false) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(
                onClick = navigatePop
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기 버튼",
                    tint = colorResource(R.color.friends_white)
                )
            }
        },
        actions = {
            if(writePost){
                IconButton(
                    onClick = navigateToPostDetail
                ) {
                    Icon(
                        Icons.Filled.Create,
                        contentDescription = "작성 버튼",
                        tint = colorResource(R.color.friends_white)
                    )
                }
            }
        }
    )
}