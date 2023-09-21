package com.base.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.base.data.local.dao.base.BaseDao
import com.base.data.local.entities.PostEntity

@Dao
interface PostDao : BaseDao<PostEntity> {

    @Query("SELECT * FROM post_table")
    suspend fun getPostAll(): List<PostEntity>

    @Query("SELECT * FROM post_table WHERE id=:id")
    suspend fun getPostDetail(id: String): PostEntity
}