package org.comon.moviefriends.data.repo

import android.app.Activity
import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.collectLatest
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSource
import org.comon.moviefriends.data.datasource.firebase.AuthenticationDataSourceImpl
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.UserInfo

class LoginRepositoryImpl(
    private val authDataSource: AuthenticationDataSource = AuthenticationDataSourceImpl()
): LoginRepository {

    override suspend fun kakaoLogin(context: Context) =
        authDataSource.kakaoLogin(context)

    override suspend fun googleLogin(context: Activity) =
        authDataSource.googleLogin(context)

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) =
        authDataSource.insertUserInfoToFireStore(userInfo)

}