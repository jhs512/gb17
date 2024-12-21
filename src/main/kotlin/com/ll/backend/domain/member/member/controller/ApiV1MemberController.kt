package com.ll.backend.domain.member.member.controller

import com.ll.backend.domain.member.member.dto.MemberDto
import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/members")
@Validated
class ApiV1MemberController(
    private val memberService: MemberService,
    private val rq: Rq
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
            "${member.name}님 환영합니다.",
            MemberDto(member)
        )
    }


    data class MemberLoginReqBody(
        @NotBlank
        val username: String,
        @NotBlank
        val password: String,
    )

    data class MemberLoginResBody(
        val item: MemberDto,
        val accessToken: String,
        val refreshToken: String,
    )

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid reqBody: MemberLoginReqBody
    ): RsData<MemberLoginResBody> {
        val member = memberService
            .findByUsername(reqBody.username)
            ?: throw ServiceException("401-1", "해당 회원은 존재하지 않습니다.")

        memberService.checkPasswordValidation(reqBody.password, member.password)

        val accessToken = memberService.genAccessToken(member)
        val refreshToken = member.refreshToken

        rq.makeAuthCookies(accessToken, refreshToken)

        return RsData(
            "201-1",
            "${member.name}님 환영합니다.",
            MemberLoginResBody(
                item = MemberDto(member),
                accessToken = memberService.genAccessToken(member),
                refreshToken = member.refreshToken
            )
        )
    }

    @DeleteMapping("/logout")
    fun logout(req: HttpServletRequest): RsData<Void> {
        rq.removeAuthCookies()
        return RsData("200-1", "로그아웃 되었습니다.")
    }
}