package org.comon.moviefriends.presenter.screen.intro

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.common.MFPreferences
import org.comon.moviefriends.data.model.firebase.UserInfo
import org.comon.moviefriends.presenter.viewmodel.LoginViewModel
import org.comon.moviefriends.presenter.widget.MFButton

@Composable
fun SubmitNickNameScreen(
    uid: String,
    nickname: String,
    photoUrl: String,
    joinType: String,
    moveToScaffoldScreen: () -> Unit,
) {
    val viewModel = LoginViewModel()
    var textValue by remember { mutableStateOf(nickname) }
    val loadingUiState = remember { mutableStateOf(false) }
    val localContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHost = remember { SnackbarHostState() }

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 36.dp)
                .padding(bottom = 24.dp),
        ) {
            Text(
                stringResource(R.string.label_input_nickname),
                color = colorResource(R.color.friends_white),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            OutlinedTextField(
                shape = RoundedCornerShape(12.dp) ,
                value = textValue,
                onValueChange = { textValue = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = true,
                keyboardActions = KeyboardActions(onDone = { }),
            )
        }
        MFButton({
            coroutineScope.launch {
                val userInfo = UserInfo(
                    id = uid,
                    nickName = textValue,
                    profileImage = photoUrl,
                    joinType = joinType,
                )
                viewModel.completeJoinUser(
                    userInfo = userInfo,
                    loadingState = loadingUiState,
                    moveToScaffoldScreen = moveToScaffoldScreen,
                    showErrorMessage = {
                        coroutineScope.launch {
                            snackBarHost.showSnackbar(
                                localContext.getString(R.string.network_error),
                                null,
                                true,
                                SnackbarDuration.Short
                            )
                        }
                    }
                )
            }
        }, stringResource(R.string.button_confirm))
    }

}
