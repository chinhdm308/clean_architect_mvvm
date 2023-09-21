package com.base.data.repositories

import com.base.data.remote.mapper.PostListMapper
import com.base.data.remote.services.JsonPlaceHolderService
import com.base.data.utils.NetworkBoundResource
import com.base.data.utils.mapFromApiResponse
import com.base.domain.models.Post
import com.base.domain.repositories.JsonPlaceHolderRemoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.base.domain.utils.Resource

class JsonPlaceHolderRemoteRepoImpl @Inject constructor(
    private val jsonPlaceHolderService: JsonPlaceHolderService,
    private val networkBoundResources: NetworkBoundResource,
    private val postListMapper: PostListMapper
) : JsonPlaceHolderRemoteRepository {

    override suspend fun fetchPostList(): Flow<Resource<List<Post>>> {
        return mapFromApiResponse(
            resource = networkBoundResources.downloadData {
                jsonPlaceHolderService.getPosts()
            },
            mapper = postListMapper
        )
    }
}