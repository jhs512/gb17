package com.ll.backend.global.rq

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.domain.member.member.service.MemberService
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.security.SecurityUser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@RequestScope
@Component
class Rq(
    private val memberService: MemberService
) {
    val user: SecurityUser by lazy {
        SecurityContextHolder.getContext()?.authentication?.principal as? SecurityUser
            ?: throw ServiceException("403-1", "securityUser 객체 취득실패, 로그인이 필요합니다.")
    }

    val actor: Member by lazy {
        memberService.findByUsername(user.username).orElseThrow {
            ServiceException("403-1", "actor 객체 취득실패, 로그인이 필요합니다.")
        }
    }
}
