package org.comon.moviefriends.presenter.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.common.UserGender
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.White

@Composable
fun InputNickName(
    nickNameValue: MutableState<String>,
    setUserNickName: (String) -> Unit,
){
    Text(
        stringResource(R.string.label_input_nickname),
        color = colorResource(R.color.friends_white),
        modifier = Modifier.padding(bottom = 12.dp)
    )
    OutlinedTextField(
        shape = RoundedCornerShape(12.dp) ,
        value = nickNameValue.value,
        onValueChange = {
            nickNameValue.value = it
            setUserNickName(it)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        enabled = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    )
}

@Composable
fun InputGender(
    setUserGender: (String) -> Unit,
){
    var genderSelected by remember { mutableIntStateOf(0) }

    Text(
        stringResource(R.string.label_input_gender),
        color = colorResource(R.color.friends_white),
        modifier = Modifier.padding(bottom = 12.dp)
    )

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        UserGender.entries.forEachIndexed { index, userGender ->
            if(index==0) return@forEachIndexed
            SegmentedButton(
                selected = index==genderSelected,
                onClick = {
                    genderSelected = index
                    setUserGender(userGender.str)
                },
                label = { Text(text = userGender.kor) },
                shape = SegmentedButtonDefaults.itemShape(index = index-1, count = UserGender.entries.size-1)
            )
        }
    }
}

@Composable
fun InputAgeRange(
    setUserAgeRange: (Int) -> Unit,
){
    Text(
        stringResource(R.string.label_input_age),
        color = colorResource(R.color.friends_white),
        modifier = Modifier.padding(bottom = 12.dp)
    )
    var isAgeMenuShown by remember { mutableStateOf(false) }
    var selectedAge by remember { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickableOnce {
                isAgeMenuShown = !isAgeMenuShown
            },
        value = selectedAge,
        onValueChange = {},
        placeholder = { Text("연령대") },
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, "") },
        readOnly = true,
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = White,
            focusedBorderColor = White,
            unfocusedBorderColor = White,
            cursorColor = White,
        )

    )
    if(isAgeMenuShown){
        AgeRangeModal(
            dismissModal = {isAgeMenuShown = false},
            getAge = {
                setUserAgeRange(it.num)
                selectedAge=it.kor
            }
        )
    }
}