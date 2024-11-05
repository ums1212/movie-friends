package org.comon.moviefriends.ui.screen.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.comon.moviefriends.ui.theme.FriendsBlack
import org.comon.moviefriends.ui.theme.FriendsWhite

@Composable
fun SearchScreen(
    moveToScaffoldScreen: () -> Unit
){
    val searchValue by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().background(FriendsBlack)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).padding(top = 8.dp)
        ) {
            IconButton(
                onClick = moveToScaffoldScreen
            ) {
                Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, "취소", tint = FriendsWhite)
            }
            OutlinedTextField(
                placeholder = { Text("검색어를 입력하세요") },
                shape = RoundedCornerShape(12.dp) ,
                singleLine = true,
                enabled = true,
                trailingIcon = { Icon(imageVector = Icons.Filled.Search, "검색") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier.fillMaxWidth(),
                value = searchValue,
                onValueChange = { },
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        HorizontalDivider()
        Column {

        }
    }
}