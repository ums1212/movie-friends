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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.comon.moviefriends.R
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.presenter.common.clickableOnce
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsRed
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.viewmodel.CommunityPostViewModel
import org.comon.moviefriends.presenter.components.CategoryModal
import org.comon.moviefriends.presenter.components.MFPostTitle
import java.util.UUID

@Composable
fun WritePostScreen(
    postId: String?,
    viewModel: CommunityPostViewModel,
) {
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val localContext = LocalContext.current

    val scrollState = rememberScrollState()

    val isCategoryMenuShown = remember { mutableStateOf(false) }
    val errorState = viewModel.errorState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        if(postId != null){
            viewModel.setPostId(postId)
            viewModel.getPost()
            viewModel.setIsUpdate(true)
        }else{
            viewModel.setPostId(UUID.randomUUID().toString())
            viewModel.setIsUpdate(false)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        if(errorState.value){
            showSnackBar(coroutineScope, snackBarHost, localContext)
        }
        if(viewModel.isLoading.value){
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = FriendsRed
            )
        }
        // 카테고리
        OutlinedTextField(
            modifier = Modifier
                .width(150.dp)
                .clickableOnce {
                    isCategoryMenuShown.value = !isCategoryMenuShown.value
                }
            ,
            value = viewModel.postUiState.postCategory.value,
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
                {viewModel.postUiState.postCategory.value = it.kor}
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
                value = viewModel.postUiState.postTitle.value,
                onValueChange = {
                    viewModel.postUiState.postTitle.value = it
                },
                maxLines = 1,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = FriendsWhite
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
            value = viewModel.postUiState.postContent.value,
            onValueChange = {
                viewModel.postUiState.postContent.value = it
            },
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
        if(viewModel.postUiState.postImageUrI.value.isNotEmpty()){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
            ) {
                Icon(
                    modifier = Modifier.clickableOnce {
                        //                            stateImageList.remove(stateImageList[0])
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
        Spacer(Modifier.padding(vertical = 8.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        IconButton(
            onClick = { viewModel.uploadImage() },
        ) {
            Icon(
                Icons.Filled.AccountBox,
                contentDescription = "이미지 추가 버튼",
                tint = colorResource(R.color.friends_white)
            )
        }
    }
    SnackbarHost(snackBarHost)
}