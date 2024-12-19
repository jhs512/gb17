package com.ll.backend.global.security

import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.util.Ut
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val rsData: RsData<Void> = RsData("403-1", "로그인 후 이용해주세요.")

        response.contentType = "application/json;charset=UTF-8"
        response.status = rsData.statusCode
        response.writer.write(Ut.json.toString(rsData))
    }
}