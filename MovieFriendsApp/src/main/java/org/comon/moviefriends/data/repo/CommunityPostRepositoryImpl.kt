package org.comon.moviefriends.data.repo

import kotlinx.coroutines.flow.Flow
import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSource
import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSourceImpl
import org.comon.moviefriends.data.datasource.tmdb.APIResult
import org.comon.moviefriends.data.model.firebase.PostInfo
import org.comon.moviefriends.data.model.firebase.ReplyInfo

class CommunityPostRepositoryImpl(
    private val fs: CommunityPostDataSource = CommunityPostDataSourceImpl()
): CommunityPostRepository {
    override suspend fun insertPost(post: PostInfo) =
        fs.insertPost(post)

    override suspend fun updatePost(post: PostInfo) =
        fs.updatePost(post)

    override suspend fun deletePost(postId: String) =
        fs.deletePost(postId)

    override suspend fun getPost(postId: String) =
        fs.getPost(postId)

    override suspend fun getALLPost() =
        fs.getALLPost()

    override suspend fun insertReply(replyInfo: ReplyInfo) =
        fs.insertReply(replyInfo)

    override suspend fun deleteReply(replyId: String) =
        fs.deleteReply(replyId)

    override suspend fun getALLReply(postId: String) =
        fs.getALLReply(postId)
}