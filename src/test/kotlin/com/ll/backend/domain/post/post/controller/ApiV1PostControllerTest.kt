package com.ll.backend.domain.post.post.controller

import com.ll.backend.domain.post.post.entity.Post
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.app.AppConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiV1PostControllerTest @Autowired constructor(
    private val postService: PostService,
    private val mockMvc: MockMvc
) {
    @Test
    @DisplayName("GET /api/v1/posts/1")
    fun t1() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.title").value("안녕하세요."))
            .andExpect(jsonPath("$.body").value("반갑습니다."))
    }

    @Test
    @DisplayName("GET /api/v1/posts/2")
    fun t2() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts/2")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.title").value("Hello."))
            .andExpect(jsonPath("$.body").value("Nice to meet you."))
    }

    @Test
    @DisplayName("GET /api/v1/posts")
    fun t3() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        val postPage: Page<Post> = postService
            .findByPublishedPaged(true, 1, AppConfig.BASE_PAGE_SIZE)

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items.length()").value(postPage.numberOfElements))
            .andExpect(jsonPath("$.totalItems").value(postPage.totalElements))
            .andExpect(jsonPath("$.totalPages").value(postPage.totalPages))
            .andExpect(jsonPath("$.currentPageNumber").value(postPage.number + 1))
            .andExpect(jsonPath("$.pageSize").value(postPage.size))

        val posts = postPage.content

        for (i in posts.indices) {
            resultActions
                .andExpect(jsonPath("$.items[$i].id").value(posts[i].id))
                .andExpect(jsonPath("$.items[$i].title").value(posts[i].title))
                .andExpect(jsonPath("$.items[$i].body").value(posts[i].body))
        }
    }

    @Test
    @DisplayName("GET /api/v1/posts?page=2&pageSize=1")
    fun t4() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/posts")
                    .param("page", "2")
                    .param("pageSize", "1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        val postPage = postService
            .findByPublishedPaged(true, 2, 1)

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items.length()").value(postPage.numberOfElements))
            .andExpect(jsonPath("$.totalItems").value(postPage.totalElements))
            .andExpect(jsonPath("$.totalPages").value(postPage.totalPages))
            .andExpect(jsonPath("$.currentPageNumber").value(postPage.number + 1))
            .andExpect(jsonPath("$.pageSize").value(postPage.size))

        val posts = postPage.content

        for (i in posts.indices) {
            resultActions
                .andExpect(jsonPath("$.items[$i].id").value(posts[i].id))
                .andExpect(jsonPath("$.items[$i].title").value(posts[i].title))
                .andExpect(jsonPath("$.items[$i].body").value(posts[i].body))
        }
    }

    @Test
    @DisplayName("DELETE /api/v1/posts/1")
    fun t5() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                delete("/api/v1/posts/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.resultCode").value("200-1"))
            .andExpect(jsonPath("$.msg").value("1번 글이 삭제되었습니다."))

        assertThat(postService.findById(1).isEmpty).isTrue()
    }
}