package org.comon.moviefriends.presentation.screen.community

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.error
import org.comon.moviefriends.R
import org.comon.moviefriends.common.showSnackBar
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.presentation.common.clickableOnce
import org.comon.moviefriends.presentation.theme.FriendsBoxGrey
import org.comon.moviefriends.presentation.theme.FriendsRed
import org.comon.moviefriends.presentation.theme.FriendsTextGrey
import org.comon.moviefriends.presentation.theme.FriendsWhite
import org.comon.moviefriends.presentation.viewmodel.CommunityPostViewModel
import org.comon.moviefriends.presentation.components.CategoryModal
import org.comon.moviefriends.presentation.components.MFButton
import org.comon.moviefriends.presentation.components.MFPostTitle
import org.comon.moviefriends.presentation.components.ValidationText
import java.util.UUID

@Composable
fun WritePostScreen(
    postId: String?,
    navigateToPostDetail: (String) -> Unit,
    viewModel: CommunityPostViewModel = hiltViewModel(),
) {
    val snackBarHost = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val localContext = LocalContext.current

    val scrollState = rememberScrollState()

    val isCategoryMenuShown = remember { mutableStateOf(false) }
    val errorState = viewModel.errorState.collectAsStateWithLifecycle()

    // 상태를 State로 변환하여 Compose가 인식하도록 합니다.
    val postCategory by viewModel.postCategory.collectAsStateWithLifecycle()
    val postCategoryState by viewModel.postCategoryState.collectAsStateWithLifecycle()
    val postTitle by viewModel.postTitle.collectAsStateWithLifecycle()
    val postTitleState by viewModel.postTitleState.collectAsStateWithLifecycle()
    val postContent by viewModel.postContent.collectAsStateWithLifecycle()
    val postContentState by viewModel.postContentState.collectAsStateWithLifecycle()
    val imageUriFromAlbum by viewModel.imageUriFromAlbum.collectAsStateWithLifecycle()
    val uploadImageState by viewModel.uploadImageState.collectAsStateWithLifecycle()


    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        viewModel.uploadImage(uri)
                    }
                }
                Activity.RESULT_CANCELED -> Unit
            }
        }

    val imageAlbumIntent =
        Intent(Intent.ACTION_PICK).apply {
            setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.CONTENT_TYPE
            )
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
            )
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grantedPermissionMap ->
        
    }


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
            value = postCategory,
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
        if(postCategoryState){
            ValidationText(
                text = stringResource(R.string.category_empty),
            )
        }
        if(isCategoryMenuShown.value){
            CategoryModal(
                dismissModal = { isCategoryMenuShown.value = false },
                getCategory = {
                    viewModel.setPostCategory(it.kor)
                }
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
                value = postTitle,
                onValueChange = {
                    viewModel.setPostTitle(it)
                },
                maxLines = 1,
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = FriendsWhite
                ),
                cursorBrush = SolidColor(FriendsTextGrey),
//                isError = viewModel.postValidationState.postTitle.value,
//                supportingText = {
//                    if (viewModel.postValidationState.postTitle.value) {
//                        Text(stringResource(R.string.title_empty))
//                    }
//                },
            )
        }
        if(postTitleState){
            ValidationText(
                text = stringResource(R.string.title_empty),
            )
        }
        Spacer(Modifier.padding(vertical = 8.dp))
        MFPostTitle("내용")
        Spacer(Modifier.padding(vertical = 8.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            value = postContent,
            onValueChange = {
                viewModel.setPostContent(it)
            },
            singleLine = false,
            textStyle = TextStyle(
                fontSize = 16.sp
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = FriendsBoxGrey,
                unfocusedIndicatorColor = FriendsBoxGrey,
                focusedIndicatorColor = FriendsBoxGrey,
            ),
            isError = postContentState,
            supportingText = {
                if (postContentState) {
                    Text(stringResource(R.string.content_empty))
                }
            },
        )
        Spacer(Modifier.padding(vertical = 8.dp))
        if(imageUriFromAlbum != null){
            Card {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    when(uploadImageState){
                        APIResult.NoConstructor -> {}
                        APIResult.Loading -> {
                            CircularProgressIndicator(
                                color = colorResource(R.color.white)
                            )
                        }
                        is APIResult.NetworkError -> {
                            Icon(
                                modifier = Modifier.size(96.dp),
                                tint = colorResource(R.color.friends_red),
                                imageVector = Icons.Rounded.Warning,
                                contentDescription = "네트워크 에러"
                            )
                        }
                        is APIResult.Success -> {
                            AsyncImage(
                                modifier = Modifier
                                    .size(96.dp)
                                    .padding(8.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(imageUriFromAlbum)
                                    .crossfade(true)
                                    .error(R.drawable.logo)
                                    .build(),
                                contentDescription = "업로드 이미지",
                                contentScale = ContentScale.Fit
                            )
                            Icon(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .clickableOnce {
                                        viewModel.deleteImage()
                                    },
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "이미지 삭제 버튼"
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.padding(vertical = 8.dp))
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row {
            IconButton(
                onClick = {
                    checkPermission(localContext, permissionLauncher) {
                        albumLauncher.launch(imageAlbumIntent)
                    }
                }
            ) {
                Icon(
                    Icons.Filled.AccountBox,
                    contentDescription = "이미지 추가 버튼",
                    tint = colorResource(R.color.friends_white)
                )
            }
        }

        Spacer(Modifier.padding(vertical = 8.dp))
        MFButton(
            clickEvent = {
                viewModel.insertPost { postId ->
                    navigateToPostDetail(postId)
                }
            },
            text = "작성"
        )
    }
    SnackbarHost(snackBarHost)
}
fun checkPermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    startAlbumLauncher: () -> Unit
) {
    val permissions = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf( Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        arrayOf( Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val isPermissionGranted = permissions.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
    if (isPermissionGranted) {
        startAlbumLauncher()
    } else {
        launcher.launch(permissions)
    }
}