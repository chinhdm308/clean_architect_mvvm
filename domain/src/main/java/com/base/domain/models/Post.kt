package com.base.domain.models

data class Post(
    val id: String,
    val userId: String,
    val title: String,
    val body: String
)