package org.comon.moviefriends.ui.screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.comon.moviefriends.R
import org.comon.moviefriends.ui.viewmodel.LoginViewModel
import org.comon.moviefriends.ui.widget.MFButton

@Preview
@Composable
fun SubmitNickNameScreen(

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
                value = TextFieldValue(),
                onValueChange = { },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = true,
                keyboardActions = KeyboardActions(onDone = { }),

            )
        }
        MFButton({}, stringResource(R.string.button_confirm))
    }
}
