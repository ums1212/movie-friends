package org.comon.moviefriends.presenter.screen.community

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.comon.moviefriends.R
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsRed
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.widget.CategoryModal
import org.comon.moviefriends.presenter.widget.DetailTopAppBar
import org.comon.moviefriends.presenter.widget.MFPostTitle

@Composable
fun WritePostScreen(
    navigateToPostDetail: (Int) -> Unit,
    navigatePop: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val isCategoryMenuShown = remember { mutableStateOf(false) }
    val categoryValue = remember { mutableStateOf("") }

    val isImageUploaded = remember { mutableStateOf(false) }
    val stateImageList = remember { mutableStateListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { DetailTopAppBar(navigatePop, { navigateToPostDetail(0) }, true) },
    ) { innerPadding ->
        if(true){
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(innerPadding),
                color = FriendsRed
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
        ) {
            Spacer(Modifier.padding(vertical = 8.dp))
            // 카테고리
            OutlinedTextField(
                modifier = Modifier
                    .width(150.dp)
                    .clickableOnce {
                        isCategoryMenuShown.value = !isCategoryMenuShown.value
                    }
                ,
                value = categoryValue.value,
                onValueChange = {},
                placeholder = { Text("카테고리") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, "") },
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = FriendsWhite,
                    focusedBorderColor = FriendsTextGrey,
                    unfocusedBorderColor = FriendsTextGrey,
                    cursorColor = FriendsTextGrey,
                )

            )
            if(isCategoryMenuShown.value){
                CategoryModal(
                    {isCategoryMenuShown.value = false},
                    {categoryValue.value = it.kor}
                )
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                MFPostTitle(
                    "제목",
                    Modifier.padding(end = 8.dp)
                )
                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            drawLine(
                                color = FriendsTextGrey,
                                Offset(0f, size.height),
                                Offset(size.width, size.height),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                        .padding(6.dp)
                    ,
                    value = TextFieldValue(""),
                    onValueChange = {},
                    maxLines = 1,
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    ),
                    cursorBrush = SolidColor(FriendsTextGrey)
                )
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            MFPostTitle("내용")
            Spacer(Modifier.padding(vertical = 8.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                value = TextFieldValue(""),
                onValueChange = {},
                singleLine = false,
                textStyle = TextStyle(
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = FriendsBoxGrey,
                    unfocusedIndicatorColor = FriendsBoxGrey,
                    focusedIndicatorColor = FriendsBoxGrey,
                )
            )
            Spacer(Modifier.padding(vertical = 8.dp))
            if(isImageUploaded.value){
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(stateImageList) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                modifier = Modifier.clickableOnce {
                                    stateImageList.remove(item)
                                },
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "이미지 삭제 버튼"
                            )
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "업로드 이미지",
                                contentScale = ContentScale.FillWidth
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.padding(vertical = 8.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            IconButton(
                onClick = {
                    isImageUploaded.value = !isImageUploaded.value
                    if(stateImageList.isEmpty()){
                        stateImageList.addAll(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
                    }
                },
            ) {
                Icon(
                    Icons.Filled.AccountBox,
                    contentDescription = "이미지 추가 버튼",
                    tint = colorResource(R.color.friends_white)
                )
            }
        }
    }
}