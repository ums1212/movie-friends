package org.comon.moviefriends.data.datasource.firebase

import org.comon.moviefriends.data.model.firebase.UserInfo

interface UserDataSource {

    suspend fun insertUserInfoToFireStore(
        userInfo: UserInfo
    ): Result<Boolean>

    suspend fun getUserInfoFromFireStore(
        uid: String
    ): Result<UserInfo>

    suspend fun updateFcmToken(userId: String, token: String): Result<Boolean>
}