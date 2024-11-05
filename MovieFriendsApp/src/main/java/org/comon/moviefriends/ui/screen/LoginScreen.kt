package org.comon.moviefriends.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.viewmodel.LoginViewModel
import org.comon.moviefriends.ui.widget.GoogleLoginButton
import org.comon.moviefriends.ui.widget.KakaoLoginButton
import org.comon.moviefriends.ui.widget.MFButton

@Composable
fun LoginScreen(
    moveToScaffoldScreen: () -> Unit,
    moveToSubmitNickNameScreen: () -> Unit,
) {
    val viewModel: LoginViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.friends_black)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 72.dp)
        )
        MFButton( moveToScaffoldScreen, stringResource(R.string.button_without_login))
        HorizontalDivider(Modifier
            .padding(horizontal = 36.dp, vertical = 24.dp)
        )
        KakaoLoginButton { moveToSubmitNickNameScreen() }
        Spacer(Modifier.padding(vertical = 12.dp))
        GoogleLoginButton { moveToSubmitNickNameScreen() }
    }

}

