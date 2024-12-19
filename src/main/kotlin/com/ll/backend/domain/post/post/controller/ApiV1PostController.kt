package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.post.dto.PostDto
import com.ll.backend.domain.post.post.service.PostService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class ApiV1PostController(
    private val postService: PostService
) {
    @GetMapping("/{id}")
    fun getItem(
        @PathVariable id: Long
    ): PostDto {
        return postService.findById(id)
            .map { PostDto(it) }
            .get()
    }
}