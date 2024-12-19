package com.ll.backend.domain.post.post.dto

import com.ll.backend.domain.post.post.entity.Post

data class PostDto(
    val id: Long,
    val title: String,
    val body: String
) {
    constructor(post: Post) : this(
        id = post.id,
        title = post.title,
        body = post.body
    )
}
