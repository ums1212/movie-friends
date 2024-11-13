package org.comon.moviefriends.data.repo

import android.net.Uri
import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSource
import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSourceImpl
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo
import org.comon.moviefriends.data.model.firebase.UserInfo

class CommunityPostRepositoryImpl(
    private val fs: CommunityPostDataSource = CommunityPostDataSourceImpl()
): CommunityPostRepository {
    override suspend fun insertPost(post: PostInfo) =
        fs.insertPost(post)

    override suspend fun uploadImage(imageUri: Uri, fileName: String) =
        fs.uploadImage(imageUri, fileName)

    override suspend fun updatePost(post: PostInfo) =
        fs.updatePost(post)

    override suspend fun deletePost(postId: String) =
        fs.deletePost(postId)

    override suspend fun getPost(postId: String) =
        fs.getPost(postId)

    override suspend fun getALLPost() =
        fs.getALLPost()

    override suspend fun addViewCount(postId: String) =
        fs.addViewCount(postId)

    override suspend fun getPostLikeState(
        postId: String,
        userId: String
    ) = fs.getPostLikeState(postId, userId)

    override suspend fun changePostLikeState(
        postId: String,
        userInfo: UserInfo
    ) = fs.changePostLikeState(postId, userInfo)

    override suspend fun insertReply(replyInfo: ReplyInfo) =
        fs.insertReply(replyInfo)

    override suspend fun deleteReply(replyId: String) =
        fs.deleteReply(replyId)

    override suspend fun getALLReply(postId: String) =
        fs.getALLReply(postId)
}