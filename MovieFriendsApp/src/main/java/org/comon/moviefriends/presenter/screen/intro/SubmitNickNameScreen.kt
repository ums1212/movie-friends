package org.comon.moviefriends.presenter.screen.intro

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.viewmodel.LoginViewModel
import org.comon.moviefriends.presenter.widget.MFButton

@Composable
fun SubmitNickNameScreen(
    uid: String,
    nickname: String,
    photoUrl: String,
    moveToScaffoldScreen: () -> Unit,
) {
    val viewModel = LoginViewModel()
    val user = viewModel.user.collectAsStateWithLifecycle()
    var textValue by remember { mutableStateOf(nickname) }
    LaunchedEffect(key1 = Unit) {
        user.value?.reload()?.addOnSuccessListener {
            Log.d("test1234", "user: ${user.value?.displayName}")
        }
    }

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
        MFButton(moveToScaffoldScreen, stringResource(R.string.button_confirm))
    }
}
