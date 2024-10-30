package org.comon.moviefriends.ui.screen

import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.viewmodel.LoginViewModel
import org.comon.moviefriends.ui.widget.GoogleLoginButton
import org.comon.moviefriends.ui.widget.KakaoLoginButton
import org.comon.moviefriends.ui.widget.MFButton

@Preview
@Composable
fun LoginScreen() {
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
        MFButton({ startWithoutLogin() }, stringResource(R.string.button_without_login))
        HorizontalDivider(Modifier
            .padding(horizontal = 36.dp, vertical = 24.dp)
        )
        KakaoLoginButton { kakaoLogin() }
        Spacer(Modifier.padding(vertical = 12.dp))
        GoogleLoginButton { googleLogin() }
    }
}

fun startWithoutLogin() {
    Log.d("startWithoutLogin", "로그인 없이 시작")
}

fun kakaoLogin() {
    Log.d("kakaoLogin", "카카오 로그인")
}

fun googleLogin() {
    Log.d("googleLogin", "구글 로그인")
}
