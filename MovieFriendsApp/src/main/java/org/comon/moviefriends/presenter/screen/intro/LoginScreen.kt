package org.comon.moviefriends.presenter.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import org.comon.moviefriends.BuildConfig
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.viewmodel.LoginCategory
import org.comon.moviefriends.presenter.viewmodel.LoginViewModel
import org.comon.moviefriends.presenter.widget.GoogleLoginButton
import org.comon.moviefriends.presenter.widget.KakaoLoginButton
import org.comon.moviefriends.presenter.widget.MFButton

@Composable
fun LoginScreen(
    moveToScaffoldScreen: () -> Unit,
    moveToSubmitNickNameScreen: () -> Unit,
) {
    val viewModel: LoginViewModel = viewModel()
    val localContext = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

    val loadingUiState = remember { mutableStateOf(false) }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.friends_black)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if(loadingUiState.value){
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Image(
                painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 72.dp)
            )
            MFButton( {
                viewModel.snsLogin(LoginCategory.WITHOUT_LOGIN, moveToScaffoldScreen)
            }, stringResource(R.string.button_without_login))
            HorizontalDivider(Modifier
                .padding(horizontal = 36.dp, vertical = 24.dp)
            )
            KakaoLoginButton {
                viewModel.snsLogin(LoginCategory.KAKAO_LOGIN, moveToSubmitNickNameScreen)
            }
            Spacer(Modifier.padding(vertical = 12.dp))
            GoogleLoginButton {
                viewModel.googleLogin(
                    localContext,
                    BuildConfig.GOOGLE_OAUTH,
                    moveToSubmitNickNameScreen,
                    loadingUiState
                ) {
                    coroutineScope.launch {
                        snackBarHost.showSnackbar(
                            localContext.getString(R.string.network_error),
                            null,
                            true,
                            SnackbarDuration.Short
                        )
                    }
                }
            }
        }
        SnackbarHost(snackBarHost)
    }
}

