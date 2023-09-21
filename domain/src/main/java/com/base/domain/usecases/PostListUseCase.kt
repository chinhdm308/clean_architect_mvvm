package com.base.domain.usecases

import com.base.domain.models.Post
import com.base.domain.repositories.JsonPlaceHolderRemoteRepository
import com.base.domain.utils.ApiUseCaseNonParams
import com.base.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostListUseCase @Inject constructor(
    private val jsonPlaceHolderRemoteRepository: JsonPlaceHolderRemoteRepository
): ApiUseCaseNonParams<List<Post>> {

    override suspend fun execute(): Flow<Resource<List<Post>>> {
        return jsonPlaceHolderRemoteRepository.fetchPostList()
    }
}