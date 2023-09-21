package com.base.data.remote.mapper

import com.base.data.remote.dto.PostDto
import com.base.data.utils.Mapper
import com.base.domain.models.Post
import javax.inject.Inject

class PostListMapper @Inject constructor(
    private val postDtoMapper: PostDtoMapper
) : Mapper<List<PostDto>, List<Post>> {

    override fun mapFromApiResponse(type: List<PostDto>): List<Post> {
        return type.map {
            postDtoMapper.mapFromApiResponse(it)
        }
    }
}