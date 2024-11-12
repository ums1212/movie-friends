package org.comon.moviefriends.presenter.widget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import org.comon.moviefriends.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailTopAppBar(
    navigatePop: () -> Unit,
    writePost: () -> Unit = {},
    isWritePost: Boolean = false,
    isMyPost: Boolean = false,
) {
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
            if(isWritePost){
                IconButton(
                    onClick = writePost
                ) {
                    Icon(
                        Icons.Filled.Create,
                        contentDescription = "작성 버튼",
                        tint = colorResource(R.color.friends_white)
                    )
                }
            }
            if(isMyPost){
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "수정 메뉴 버튼",
                        tint = colorResource(R.color.friends_white)
                    )
                }
            }
        }
    )
}