package org.comon.moviefriends.data.repo

import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSource
import org.comon.moviefriends.data.datasource.firebase.CommunityPostDataSourceImpl
import org.comon.moviefriends.data.model.firebase.PostInfo

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
}