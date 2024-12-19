package com.ll.backend.global.rq

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.security.SecurityUser
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseCookie
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope


@RequestScope
@Component
class Rq(
    val req: HttpServletRequest,
    val res: HttpServletResponse,
    private val memberService: MemberService
) {
    val isLogin: Boolean by lazy {
        SecurityContextHolder.getContext()?.authentication?.isAuthenticated ?: false
    }

    val user: SecurityUser by lazy {
        SecurityContextHolder.getContext()?.authentication?.principal as? SecurityUser
            ?: throw ServiceException("403-1", "securityUser 객체 취득실패, 로그인이 필요합니다.")
    }

    val actor: Member by lazy {
        memberService.findByUsername(user.username).orElseThrow {
            ServiceException("403-1", "actor 객체 취득실패, 로그인이 필요합니다.")
        }
    }


    fun setLogin(securityUser: SecurityUser) {
        SecurityContextHolder.getContext().authentication = securityUser.genAuthentication()
    }

    fun setLogout() {
        removeCrossDomainCookie("accessToken")
        removeCrossDomainCookie("refreshToken")
        SecurityContextHolder.getContext().authentication = null
    }

    fun setCookie(name: String?, value: String?) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.domain = AppConfig.siteCookieDomain
        res.addCookie(cookie)
    }

    fun setCookie(name: String?, value: String?, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.maxAge = maxAge
        res.addCookie(cookie)
    }

    private fun getSiteCookieDomain(): String? {
        var cookieDomain: String = AppConfig.siteCookieDomain

        if (cookieDomain != "localhost") {
            return (".$cookieDomain").also { cookieDomain = it }
        }

        return null
    }

    fun setCrossDomainCookie(name: String, value: String) {
        val cookie = ResponseCookie.from(name, value)
            .path("/")
            .domain(getSiteCookieDomain())
            .sameSite("Strict")
            .secure(true)
            .httpOnly(true)
            .build()

        res.addHeader("Set-Cookie", cookie.toString())
    }

    fun removeCrossDomainCookie(name: String) {
        removeCookie(name)

        val cookie = ResponseCookie.from(name, "")
            .path("/")
            .maxAge(0)
            .domain(getSiteCookieDomain())
            .secure(true)
            .httpOnly(true)
            .build()

        res.addHeader("Set-Cookie", cookie.toString())
    }

    fun getCookie(name: String): Cookie? {
        val cookies = req.cookies ?: return null

        for (cookie in cookies) {
            if (cookie.name.equals(name)) {
                return cookie
            }
        }

        return null
    }

    fun getCookieValue(name: String, defaultValue: String?): String? {
        val cookie = getCookie(name) ?: return defaultValue

        return cookie.value
    }

    private fun getCookieAsLong(name: String, defaultValue: Int): Long {
        val value = getCookieValue(name, null) ?: return defaultValue.toLong()

        return value.toLong()
    }

    fun removeCookie(name: String) {
        val cookie = getCookie(name) ?: return

        cookie.path = "/"
        cookie.maxAge = 0
        res.addCookie(cookie)
    }

}
