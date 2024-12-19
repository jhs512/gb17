package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.post.dto.PostDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
class ApiV1PostController {
    @GetMapping("/{id}")
    fun getItem(
        @PathVariable id: Long
    ): PostDto {
        return PostDto(
            id = id,
            title = "title",
            body = "body"
        )
    }
}