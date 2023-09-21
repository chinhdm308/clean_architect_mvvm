package com.base.domain.repositories

import com.base.domain.models.Post
import kotlinx.coroutines.flow.Flow
import com.base.domain.utils.Resource

interface JsonPlaceHolderRemoteRepository : BaseRepository {
    suspend fun fetchPostList(): Flow<Resource<List<Post>>>
}