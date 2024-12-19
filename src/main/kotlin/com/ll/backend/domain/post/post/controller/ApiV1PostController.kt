package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.post.dto.PostDto
import com.ll.backend.domain.post.post.dto.PostModifyRequest
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.page.dto.PageDto
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

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

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): RsData<Void> {
        val post = postService.findById(id).get()

        postService.delete(post)

        return RsData("200-1", "${id}번 글이 삭제되었습니다.")
    }

    @PutMapping("/{id}")
    @Transactional
    fun modify(
        @PathVariable id: Long,
        @RequestBody request: PostModifyRequest
    ): RsData<PostDto> {
        val post = postService.findById(id).get()
        postService.modify(post, request.title, request.body)

        return RsData(
            "200-1",
            "${id}번 글이 수정되었습니다.",
            PostDto(post)
        )
    }
}