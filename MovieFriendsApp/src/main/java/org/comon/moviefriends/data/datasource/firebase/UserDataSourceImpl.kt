package org.comon.moviefriends.data.datasource.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import org.comon.moviefriends.data.model.firebase.UserInfo
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor (
    private val fs: FirebaseFirestore,
): UserDataSource {

    override suspend fun insertUserInfoToFireStore(userInfo: UserInfo) = runCatching {
        fs.collection("user").add(userInfo).await()
        true
    }

    override suspend fun getUserInfoFromFireStore(uid: String) = runCatching {
        fs.collection("user").whereEqualTo("id", uid).get().await()
            .documents.first().toObject(UserInfo::class.java)
            ?: throw FirebaseFirestoreException("해당 유저가 없습니다.", FirebaseFirestoreException.Code.NOT_FOUND)
    }

    override suspend fun updateFcmToken(userId: String, token: String) = runCatching {
        fs.collection("user")
            .whereEqualTo("id", userId).get().await()
            .documents.first().reference.update("fcmToken", token).await()
        true
    }
}