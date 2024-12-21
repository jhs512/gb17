package com.ll.backend.domain.member.member.controller

import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.extensions.getOrThrow
import com.ll.backend.standard.util.Ut
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiV1MemberControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val memberService: MemberService
) {
    private fun bodyToRsData(resultActions: ResultActions): RsData<Map<String, *>> {
        val contentAsString = resultActions.andReturn().response.contentAsString
        return Ut.json.toObj(contentAsString)
    }

    @Test
    @DisplayName("POST /api/v1/members/join")
    fun t1() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "newuser",
                            "password": "1234",
                            "nickname": "new유저"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        val rsData = bodyToRsData(resultActions)
        val newPostId = rsData.data["id"] as Int
        assertThat(newPostId).isGreaterThan(4)

        // THEN
        resultActions
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.resultCode").value("201-1"))
            .andExpect(jsonPath("$.msg").value("new유저님 환영합니다."))
            .andExpect(jsonPath("$.data.id").value(newPostId))
            .andExpect(jsonPath("$.data.createDate").exists())
            .andExpect(jsonPath("$.data.modifyDate").exists())
            .andExpect(jsonPath("$.data.name").value("new유저"))
            .andExpect(jsonPath("$.data.nickname").value("new유저"))
    }


    @Test
    @DisplayName("POST /api/v1/members/join conflict")
    fun t2() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": "1234",
                            "nickname": "new유저"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.resultCode").value("409-1"))
            .andExpect(jsonPath("$.msg").value("이미 존재하는 아이디입니다."))
    }

    @Test
    @DisplayName("POST /api/v1/members/login")
    fun t3() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": "1234"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        val memberUser = memberService.findByUsername("user1").getOrThrow()

        // THEN
        resultActions
            .andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.resultCode").value("201-1"))
            .andExpect(jsonPath("$.msg").value("${memberUser.name}님 환영합니다."))
            .andExpect(jsonPath("$.data.id").value(memberUser.id))
            .andExpect(jsonPath("$.data.createDate").exists())
            .andExpect(jsonPath("$.data.modifyDate").exists())
            .andExpect(jsonPath("$.data.name").value(memberUser.name))
            .andExpect(jsonPath("$.data.accessToken").exists())
            .andExpect(jsonPath("$.data.refreshToken").value(memberUser.refreshToken))
    }

    @Test
    @DisplayName("POST /api/v1/members/login, with wrong username, 401")
    fun t4() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user0",
                            "password": "1234"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.resultCode").value("401-1"))
            .andExpect(jsonPath("$.msg").value("해당 회원은 존재하지 않습니다."))
    }

    @Test
    @DisplayName("POST /api/v1/members/login, with wrong password, 401")
    fun t5() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                post("/api/v1/members/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        """
                        {
                            "username": "user1",
                            "password": "12345"
                        }
                        """.trimIndent()
                    )
            )
            .andDo(print())

        // THEN
        resultActions
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.resultCode").value("401-1"))
            .andExpect(jsonPath("$.msg").value("비밀번호가 일치하지 않습니다."))
    }

//    @Test
//    @DisplayName("GET /api/v1/posts/2")
//    fun t2() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                get("/api/v1/posts/2")
//                    .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andDo(print())
//
//        // THEN
//        resultActions
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.id").value(2))
//            .andExpect(jsonPath("$.title").value("Hello."))
//            .andExpect(jsonPath("$.body").value("Nice to meet you."))
//    }
//
//    @Test
//    @DisplayName("GET /api/v1/posts")
//    fun t3() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                get("/api/v1/posts")
//                    .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andDo(print())
//
//        val postPage: Page<Post> = postService
//            .findByPublishedPaged(true, 1, AppConfig.basePageSize)
//
//        // THEN
//        resultActions
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.items.length()").value(postPage.numberOfElements))
//            .andExpect(jsonPath("$.totalItems").value(postPage.totalElements))
//            .andExpect(jsonPath("$.totalPages").value(postPage.totalPages))
//            .andExpect(jsonPath("$.currentPageNumber").value(postPage.number + 1))
//            .andExpect(jsonPath("$.pageSize").value(postPage.size))
//
//        val posts = postPage.content
//
//        for (i in posts.indices) {
//            resultActions
//                .andExpect(jsonPath("$.items[$i].id").value(posts[i].id))
//                .andExpect(jsonPath("$.items[$i].title").value(posts[i].title))
//                .andExpect(jsonPath("$.items[$i].body").value(posts[i].body))
//        }
//    }
//
//    @Test
//    @DisplayName("GET /api/v1/posts?page=2&pageSize=1")
//    fun t4() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                get("/api/v1/posts")
//                    .param("page", "2")
//                    .param("pageSize", "1")
//                    .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andDo(print())
//
//        val postPage = postService
//            .findByPublishedPaged(true, 2, 1)
//
//        // THEN
//        resultActions
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.items.length()").value(postPage.numberOfElements))
//            .andExpect(jsonPath("$.totalItems").value(postPage.totalElements))
//            .andExpect(jsonPath("$.totalPages").value(postPage.totalPages))
//            .andExpect(jsonPath("$.currentPageNumber").value(postPage.number + 1))
//            .andExpect(jsonPath("$.pageSize").value(postPage.size))
//
//        val posts = postPage.content
//
//        for (i in posts.indices) {
//            resultActions
//                .andExpect(jsonPath("$.items[$i].id").value(posts[i].id))
//                .andExpect(jsonPath("$.items[$i].title").value(posts[i].title))
//                .andExpect(jsonPath("$.items[$i].body").value(posts[i].body))
//        }
//    }
//
//    @Test
//    @DisplayName("POST /api/v1/posts, with user1, 200")
//    @WithUserDetails("user1")
//    fun t5() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                post("/api/v1/posts")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(
//                        """
//                        {
//                            "title": "글 제목",
//                            "body": "글 내용"
//                        }
//                        """.trimIndent()
//                    )
//            )
//            .andDo(print())
//
//        val body = bodyMap(resultActions)
//        val newPostId = (body["data"] as Map<String, *>)["id"] as Int
//
//        assertThat(newPostId).isGreaterThan(2)
//
//        // THEN
//        resultActions
//            .andExpect(status().isCreated)
//            .andExpect(jsonPath("$.resultCode").value("201-1"))
//            .andExpect(jsonPath("$.msg").value("${newPostId}번 글이 작성되었습니다."))
//            .andExpect(jsonPath("$.data.id").value(newPostId))
//            .andExpect(jsonPath("$.data.title").value("글 제목"))
//            .andExpect(jsonPath("$.data.body").value("글 내용"))
//    }
//
//    @Test
//    @DisplayName("DELETE /api/v1/posts/1, with user1, 200")
//    @WithUserDetails("user1")
//    fun t6() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                delete("/api/v1/posts/1")
//                    .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andDo(print())
//
//        // THEN
//        resultActions
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.resultCode").value("200-1"))
//            .andExpect(jsonPath("$.msg").value("1번 글이 삭제되었습니다."))
//
//        assertThat(postService.findById(1).isEmpty).isTrue()
//    }
//
//    @Test
//    @DisplayName("PUT /api/v1/posts/1, with user1, 200")
//    @WithUserDetails("user1")
//    fun t7() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                put("/api/v1/posts/1")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(
//                        """
//                        {
//                            "title": "제목 수정",
//                            "body": "내용 수정"
//                        }
//                        """.trimIndent()
//                    )
//            )
//            .andDo(print())
//
//        // THEN
//        resultActions
//            .andExpect(status().isOk)
//            .andExpect(jsonPath("$.resultCode").value("200-1"))
//            .andExpect(jsonPath("$.msg").value("1번 글이 수정되었습니다."))
//            .andExpect(jsonPath("$.data.id").value(1))
//            .andExpect(jsonPath("$.data.title").value("제목 수정"))
//            .andExpect(jsonPath("$.data.body").value("내용 수정"))
//
//        val post = postService.findById(1).get()
//        assertThat(post.title).isEqualTo("제목 수정")
//        assertThat(post.body).isEqualTo("내용 수정")
//    }
//
//    @Test
//    @DisplayName("POST /api/v1/posts/1, without user, 403")
//    fun t8() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                post("/api/v1/posts")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(
//                        """
//                        {
//                            "title": "글 제목",
//                            "body": "글 내용"
//                        }
//                        """.trimIndent()
//                    )
//            )
//            .andDo(print())
//
//        // THEN
//        resultActions
//            .andExpect(status().isForbidden)
//            .andExpect(jsonPath("$.resultCode").value("403-1"))
//            .andExpect(jsonPath("$.msg").value("로그인 후 이용해주세요."))
//    }
//
//    @Test
//    @DisplayName("DELETE /api/v1/posts/1 with user2, 403")
//    @WithUserDetails("user2")
//    fun t9() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                delete("/api/v1/posts/1")
//                    .contentType(MediaType.APPLICATION_JSON)
//            )
//            .andDo(print())
//
//        // THEN
//        resultActions
//            .andExpect(status().isForbidden)
//            .andExpect(jsonPath("$.resultCode").value("403-1"))
//            .andExpect(jsonPath("$.msg").value("글의 작성자만 삭제할 수 있습니다."))
//
//        assertThat(postService.findById(1)).isPresent
//    }
//
//    @Test
//    @DisplayName("PUT /api/v1/posts/1 with user2, 403")
//    @WithUserDetails("user2")
//    fun t10() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                put("/api/v1/posts/1")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(
//                        """
//                        {
//                            "title": "제목 수정",
//                            "body": "내용 수정"
//                        }
//                        """.trimIndent()
//                    )
//            )
//            .andDo(print())
//
//        // THEN
//        resultActions
//            .andExpect(status().isForbidden)
//            .andExpect(jsonPath("$.resultCode").value("403-1"))
//            .andExpect(jsonPath("$.msg").value("글의 작성자만 수정할 수 있습니다."))
//    }
//
//    @Test
//    @DisplayName("POST /api/v1/posts, with Authorization header(refreshToken + ' ' + accessToken), 200")
//    fun t11() {
//        // WHEN
//        val resultActions = mockMvc
//            .perform(
//                post("/api/v1/posts")
//                    .header("Authorization", "Bearer user1 EMPTY")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(
//                        """
//                        {
//                            "title": "글 제목",
//                            "body": "글 내용"
//                        }
//                        """.trimIndent()
//                    )
//            )
//            .andDo(print())
//
//        val body = bodyMap(resultActions)
//        val newPostId = (body["data"] as Map<String, *>)["id"] as Int
//
//        assertThat(newPostId).isGreaterThan(2)
//
//        // THEN
//        resultActions
//            .andExpect(status().isCreated)
//            .andExpect(jsonPath("$.resultCode").value("201-1"))
//            .andExpect(jsonPath("$.msg").value("${newPostId}번 글이 작성되었습니다."))
//            .andExpect(jsonPath("$.data.id").value(newPostId))
//            .andExpect(jsonPath("$.data.title").value("글 제목"))
//            .andExpect(jsonPath("$.data.body").value("글 내용"))
//    }
}