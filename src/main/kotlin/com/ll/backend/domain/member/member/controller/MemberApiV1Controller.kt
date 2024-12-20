package com.ll.backend.domain.member.member.controller

import com.ll.backend.domain.member.member.dto.MemberDto
import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.domain.post.author.entity.PostAuthor
import com.ll.backend.domain.post.post.dto.PostDto
import com.ll.backend.domain.post.post.service.PostService
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/members")
@Validated
class MemberApiV1Controller(
    private val rq: Rq,
    private val memberService: MemberService
) {

    data class MemberJoinReqBody(
        @NotBlank
        val username: String,
        @NotBlank
        val password: String,
        @NotBlank
        val nickname: String
    )

    @PostMapping("/join")
    fun join(
        @RequestBody @Valid reqBody: MemberJoinReqBody
    ): RsData<MemberDto> {

        val member = memberService.join(reqBody.username, reqBody.password, reqBody.nickname);

        return RsData(
            "201-1",
            "${member.id}번 회원이 생성되었습니다.",
            MemberDto(member)
        )
    }

}