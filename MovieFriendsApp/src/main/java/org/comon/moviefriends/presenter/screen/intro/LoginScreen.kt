package org.comon.moviefriends.presenter.screen.intro

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseUser
import org.comon.moviefriends.R
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.presenter.viewmodel.JoinType
import org.comon.moviefriends.presenter.viewmodel.LoginViewModel
import org.comon.moviefriends.presenter.widget.GoogleLoginButton
import org.comon.moviefriends.presenter.widget.KakaoLoginButton
import org.comon.moviefriends.presenter.widget.MFButton

@Composable
fun LoginScreen(
    moveToScaffoldScreen: () -> Unit,
    moveToSubmitNickNameScreen: (user: FirebaseUser?, joinType: String) -> Unit,
) {
    val viewModel: LoginViewModel = viewModel()
    val localContext = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

    val loading = viewModel.loadingState.collectAsStateWithLifecycle()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.black)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(loading.value){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Image(
                painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(320.dp)
                    .padding(bottom = 72.dp)
            )
            MFButton( moveToScaffoldScreen, stringResource(R.string.button_without_login))
            HorizontalDivider(Modifier
                .padding(horizontal = 36.dp, vertical = 24.dp)
            )
            KakaoLoginButton {
                viewModel.kakaoLogin(
                    context = localContext,
                    showError = {showSnackBar(coroutineScope, snackBarHost, localContext)},
                    moveToScaffoldScreen = moveToScaffoldScreen,
                    moveToSubmitNickNameScreen = { user ->
                        moveToSubmitNickNameScreen(user, JoinType.KAKAO.str)
                    }
                )
            }
            Spacer(Modifier.padding(vertical = 12.dp))
            GoogleLoginButton {
                viewModel.googleLogin(
                    context = localContext as Activity,
                    showError = {showSnackBar(coroutineScope, snackBarHost, localContext)},
                    moveToScaffoldScreen = moveToScaffoldScreen,
                    moveToSubmitNickNameScreen = { user ->
                        moveToSubmitNickNameScreen(user, JoinType.GOOGLE.str)
                    }
                )
            }
        }
        SnackbarHost(snackBarHost)
    }
}

