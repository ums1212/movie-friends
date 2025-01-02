package org.comon.moviefriends.data.repo

import android.net.Uri
import org.comon.moviefriends.data.datasource.firebase.PostDataSource
import org.comon.moviefriends.data.entity.firebase.PostInfo
import org.comon.moviefriends.data.entity.firebase.ReplyInfo
import org.comon.moviefriends.data.entity.firebase.UserInfo
import org.comon.moviefriends.domain.repo.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor (
    private val fs: PostDataSource
): PostRepository {
    override suspend fun insertPost(post: PostInfo) =
        fs.insertPost(post)

    override suspend fun uploadImage(imageUri: Uri, fileName: String) =
        fs.uploadImage(imageUri, fileName)

    override suspend fun getImageUrl(fileName: String) =
        fs.getImageUrl(fileName)

    override suspend fun deleteImage(fileName: String) =
        fs.deleteImage(fileName)

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

    override suspend fun insertReply(postId: String, replyInfo: ReplyInfo) =
        fs.insertReply(postId, replyInfo)

    override suspend fun deleteReply(postId: String, replyId: String) =
        fs.deleteReply(postId, replyId)

    override suspend fun getReplyList(postId: String) =
        fs.getReplyList(postId)

    override suspend fun getALLReply(userId: String) =
        fs.getALLReply(userId)
}