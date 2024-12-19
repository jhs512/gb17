package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.author.entity.PostAuthor
import com.ll.backend.domain.post.post.dto.PostDto
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.page.dto.PageDto
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
@Validated
class ApiV1PostController(
    private val rq: Rq,
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


    data class PostWriteReqBody(
        @NotBlank
        val title: String,
        @NotBlank
        val body: String
    )

    @PostMapping
    fun write(
        @RequestBody @Valid reqBody: PostWriteReqBody
    ): RsData<PostDto> {
        postService.checkPermissionToWrite(PostAuthor(rq.actor))

        val post = postService.write(PostAuthor(rq.actor), reqBody.title, reqBody.body, true)

        return RsData(
            "200-1",
            "${post.id}번 글이 작성되었습니다.",
            PostDto(post)
        )
    }


    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): RsData<Void> {
        val post = postService.findById(id).get()

        postService.checkPermissionToDelete(PostAuthor(rq.actor), post)

        postService.delete(post)

        return RsData("200-1", "${id}번 글이 삭제되었습니다.")
    }


    data class PostModifyReqBody(
        @NotBlank
        val title: String,
        @NotBlank
        val body: String
    )

    @PutMapping("/{id}")
    @Transactional
    fun modify(
        @PathVariable id: Long,
        @RequestBody @Valid reqBody: PostModifyReqBody
    ): RsData<PostDto> {
        val post = postService.findById(id).get()

        postService.checkPermissionToModify(PostAuthor(rq.actor), post)

        postService.modify(post, reqBody.title, reqBody.body)

        return RsData(
            "200-1",
            "${id}번 글이 수정되었습니다.",
            PostDto(post)
        )
    }
}