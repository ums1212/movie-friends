package org.comon.moviefriends.presentation.screen.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.presentation.viewmodel.LoginViewModel
import org.comon.moviefriends.presentation.components.InputAgeRange
import org.comon.moviefriends.presentation.components.InputGender
import org.comon.moviefriends.presentation.components.InputNickName
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.ProfileCircleImage

@Composable
fun SubmitNickNameScreen(
    uid: String,
    nickname: String,
    photoUrl: String,
    joinType: String,
    moveToScaffoldScreen: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.setUserUid(uid)
        viewModel.setUserNickName(nickname)
        viewModel.setUserPhotoUrl(photoUrl)
        viewModel.setUserJoinType(joinType)
    }

    val loadingState = viewModel.loadingState.collectAsStateWithLifecycle()
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }
    val user = viewModel.user.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val nicknameValue = remember { mutableStateOf(nickname) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.friends_black)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(loadingState.value) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        ProfileCircleImage(context, user.value?.photoUrl)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .padding(bottom = 24.dp),
        ) {
            /** 닉네임 입력 */
            InputNickName(
                nickNameValue = nicknameValue,
                setUserNickName = {
                    viewModel.setUserNickName(it)
                }
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            /** 성별 입력 */
            InputGender(
                setUserGender = {
                    viewModel.setUserGender(it)
                }
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            /** 연령대 입력 */
            InputAgeRange(
                setUserAgeRange = {
                    viewModel.setUserAgeRange(it)
                }
            )
        }
        MFButton({
            viewModel.completeJoinUser(
                moveToScaffoldScreen = moveToScaffoldScreen,
                showErrorMessage = {showSnackBar(coroutineScope, snackBarHost, localContext)}
            )
        }, stringResource(R.string.button_confirm))
    }
    SnackbarHost(snackBarHost)

}