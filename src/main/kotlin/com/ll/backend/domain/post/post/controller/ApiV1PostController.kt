package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.post.dto.PostDto
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.standard.page.dto.PageDto
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/posts")
@Validated
class ApiV1PostController(
    private val postService: PostService
) {
    @GetMapping
    fun getItems(
        page: Int = 1,
        @Min(1) @Max(AppConfig.BASE_PAGE_SIZE.toLong()) pageSize: Int = AppConfig.BASE_PAGE_SIZE
    ): PageDto<PostDto> {
        return PageDto(
            postService
                .findByPublishedPaged(true, page, pageSize)
                .map { PostDto(it) }
        )
    }

    @GetMapping("/{id}")
    fun getItem(
        @PathVariable id: Long
    ): PostDto {
        return postService.findById(id)
            .map { PostDto(it) }
            .get()
    }
}