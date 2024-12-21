package com.ll.backend.global.security

import com.ll.backend.domain.member.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val member = memberRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("사용자를 찾을 수 없습니다.") }

        return SecurityUser(
            member.id,
            member.createDate,
            member.modifyDate,
            member.username,
            member.password,
            member.authorities
        )
    }
}