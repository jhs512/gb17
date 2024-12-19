package com.ll.backend.global.security

import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.rq.Rq
import com.ll.backend.global.rsData.RsData
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthenticationFilter(
    private val rq: Rq,
    private val memberService: MemberService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!request.requestURI.startsWith("/api/")) {
            filterChain.doFilter(request, response)
            return
        }

        if (
            listOf(
                "/api/v1/members/login",
                "/api/v1/members/join",
                "/api/v1/members/logout"
            ).contains(request.requestURI)
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val bearerToken: String = rq.req.getHeader("Authorization") ?: ""

        if (bearerToken.isNotBlank() && bearerToken.startsWith("Bearer ")) {
            val tokensStr = bearerToken.substring("Bearer ".length)
            val tokens = tokensStr.split(" ".toRegex(), limit = 2).toTypedArray()
            val refreshToken = tokens[0]
            var accessToken = if (tokens.size == 2) tokens[1] else ""

            // 엑세스 토큰이 존재하면
            if (accessToken.isNotBlank()) {
                // 유효성 체크하여 만료되었으면 리프레시 토큰으로 새로운 엑세스 토큰을 발급받고 응답헤더에 추가
                if (!memberService.validateToken(accessToken)) {
                    val rs = memberService.refreshAccessToken(refreshToken)
                    accessToken = rs.data
                    rq.res.setHeader("accessToken", accessToken)
                }

                val securityUser: SecurityUser = memberService.getUserFromAccessToken(accessToken)
                // 세션에 로그인하는 것이 아닌 1회성(이번 요청/응답 생명주기에서만 인정됨)으로 로그인 처리
                // API 요청은, 로그인이 필요하다면 이렇게 매번 요청마다 로그인 처리가 되어야 하는게 맞다.
                rq.setLogin(securityUser)
            }
        } else {
            // 토큰이 쿠키로 들어온 경우
            var accessToken: String = rq.getCookieValue("accessToken", "") ?: ""

            // 엑세스 토큰이 존재하면
            if (accessToken.isNotBlank()) {
                // 유효성 체크하여 만료되었으면 리프레시 토큰으로 새로운 엑세스 토큰을 발급받고 응답쿠키에 추가
                if (!memberService.validateToken(accessToken)) {
                    val refreshToken: String = rq.getCookieValue("refreshToken", "") ?: ""

                    val rs: RsData<String> = memberService.refreshAccessToken(refreshToken)
                    accessToken = rs.data
                    rq.setCrossDomainCookie("accessToken", accessToken)
                }

                val securityUser: SecurityUser = memberService.getUserFromAccessToken(accessToken)
                // 세션에 로그인하는 것이 아닌 1회성(이번 요청/응답 생명주기에서만 인정됨)으로 로그인 처리
                // API 요청은, 로그인이 필요하다면 이렇게 매번 요청마다 로그인 처리가 되어야 하는게 맞다.
                rq.setLogin(securityUser)
            }
        }

        filterChain.doFilter(request, response)
    }
}