package com.base.data.remote.mapper

import com.base.data.remote.dto.PostDto
import com.base.data.utils.Mapper
import com.base.domain.models.Post
import javax.inject.Inject

class PostDtoMapper @Inject constructor() : Mapper<PostDto, Post> {
    override fun mapFromApiResponse(type: PostDto): Post {
        return Post(
            id = type.id,
            userId = type.userId,
            title = type.title,
            body = type.body
        )
    }
}