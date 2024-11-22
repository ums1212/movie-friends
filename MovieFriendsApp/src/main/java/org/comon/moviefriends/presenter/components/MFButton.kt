package org.comon.moviefriends.presenter.components

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch
import org.comon.moviefriends.R
import org.comon.moviefriends.data.datasource.lbs.MFLocationManager
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.ProposalFlag
import org.comon.moviefriends.presenter.theme.Black
import org.comon.moviefriends.presenter.theme.FriendsBoxGrey
import org.comon.moviefriends.presenter.theme.FriendsTextGrey
import org.comon.moviefriends.presenter.theme.FriendsWhite
import org.comon.moviefriends.presenter.theme.KakaoColor
import org.comon.moviefriends.presenter.theme.White

@Composable
fun MFButton(clickEvent: () -> Unit, text: String) {
    Button(
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun MFButtonWantThisMovie(
    clickEvent: () -> Unit,
    text: String,
    isChecked: APIResult<Boolean>,
    showErrorMessage: () -> Unit,
) {

    val localContext = LocalContext.current

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        /** 권한 요청시 동의 했을 경우 **/
        if (areGranted) {
            clickEvent()
        }
        /** 권한 요청시 거부 했을 경우 **/
        else {
            showErrorMessage()
        }
    }

    Button(
        shape = RoundedCornerShape(25),
        onClick = {
            // 위치 권한 확인
            if(ContextCompat.checkSelfPermission(localContext,ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(localContext,ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ){
                clickEvent()
            }else{
                // 위치 권한이 없다면 요청
                launcherMultiplePermissions.launch(MFLocationManager.permissions)
            }
                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 36.dp),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        when(isChecked){
            is APIResult.Loading -> { CircularProgressIndicator(modifier = Modifier.size(24.dp)) }
            is APIResult.Success -> {
                if(isChecked.resultData){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(text)
                        Icon(imageVector = Icons.Default.Check, "체크")
                    }
                }else{
                    Text(text)
                }
            }
            else -> {
                Text(text)
            }
        }
    }
}

@Composable
fun MFButtonWatchTogether(
    clickEvent: () -> Result<Task<QuerySnapshot>>,
    requestState: MutableState<Boolean>,
    showErrorSnackBar: () -> Unit,
    proposalFlag: String = "",
) {
    val coroutineScope = rememberCoroutineScope()
    Button(
        shape = RoundedCornerShape(25),
        onClick = {
            if(proposalFlag.isNotEmpty() && proposalFlag!=ProposalFlag.WAITING.str) return@Button
            coroutineScope.launch {
                clickEvent().onSuccess { task ->
                    task.addOnSuccessListener { snapshot ->
                        requestState.value = snapshot.isEmpty
                    }.addOnFailureListener {
                        showErrorSnackBar()
                    }
                }
            }
        },
        modifier = Modifier
            .width(80.dp),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        contentPadding = PaddingValues(
            start = 2.dp,
            top = 2.dp,
            end = 2.dp,
            bottom = 2.dp,
        )
    ) {
        when(proposalFlag){
            ProposalFlag.DENIED.str -> {
                Text(stringResource(R.string.button_request_denied))
            }
            ProposalFlag.CONFIRMED.str -> {
                Text(stringResource(R.string.button_request_confirmed))
            }
            else -> {
                if(requestState.value){
                    Text(stringResource(R.string.button_cancel))
                }else {
                    Text(stringResource(R.string.button_watch_together))
                }
            }
        }
    }
}

@Composable
fun MFButtonWidthResizable(clickEvent: () -> Unit, text: String, width: Dp) {
    Button(
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .width(width),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        contentPadding = PaddingValues(
            start = 2.dp,
            top = 2.dp,
            end = 2.dp,
            bottom = 2.dp,
        )
    ) {
        Text(text)
    }
}

@Composable
fun MFButtonAddReply(insertState: State<APIResult<Boolean>>, showErrorSnackBar: () -> Unit, clickEvent: () -> Unit) {
    Button(
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .width(80.dp),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        contentPadding = PaddingValues(
            start = 2.dp,
            top = 2.dp,
            end = 2.dp,
            bottom = 2.dp,
        )
    ) {
        when(insertState.value){
            APIResult.Loading -> CircularProgressIndicator(Modifier.size(24.dp))
            is APIResult.NetworkError -> {
                showErrorSnackBar()
                Text("등록")
            }
            else -> Text("등록")
        }

    }
}

@Composable
fun MFButtonConfirm(clickEvent: () -> Unit, text: String, width: Dp) {
    OutlinedButton (
        shape = RoundedCornerShape(25),
        onClick = clickEvent,
        modifier = Modifier
            .width(width),
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsWhite,
            contentColor = Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun KakaoLoginButton(clickEvent: () -> Unit) {
    Button(
        shape = RoundedCornerShape(12.dp),
        onClick = clickEvent,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 36.dp),
        colors = ButtonColors(
            containerColor = KakaoColor,
            contentColor = Black,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.kakao_icon),
                contentDescription = stringResource(R.string.button_kakao_login),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.button_kakao_login))
        }

    }
}

@Composable
fun GoogleLoginButton(clickEvent: () -> Unit) {
    Button(
        shape = RoundedCornerShape(12.dp),
        onClick = clickEvent,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 36.dp),
        colors = ButtonColors(
            containerColor = White,
            contentColor = FriendsTextGrey,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) {
        Row {
            Image(
                painter = painterResource(R.drawable.google_icon),
                contentDescription = stringResource(R.string.button_google_login),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(stringResource(R.string.button_google_login))
        }

    }
}

@Composable
fun MFButtonReceive(onClick: () -> Unit, text: String){
    Button(
        onClick = onClick,
        border = BorderStroke(1.dp, FriendsTextGrey),
        colors = ButtonColors(
            containerColor = FriendsBoxGrey,
            contentColor = White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.LightGray
        )
    ) { Text(text) }
}