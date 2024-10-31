package org.comon.moviefriends.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.screen.NAV_ROUTE

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MFTopAppBar(
    navigateToSearch: () -> Unit,
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = colorResource(R.color.friends_black),
            titleContentColor = colorResource(R.color.friends_black),
        ),
        title = {
            Image(
                modifier = Modifier
                    .size(width = 50.dp, height = 50.dp),
                contentDescription = null,
                painter = painterResource(R.drawable.logo)
            )
        },
        actions = {
            /** 검색 버튼 */
            IconButton(
                onClick = navigateToSearch
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    tint = colorResource(R.color.friends_white)
                )
            }
        }
    )
}