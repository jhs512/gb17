package com.ll.backend.domain.member.member.service

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.domain.member.member.repository.MemberRepository
import com.ll.backend.global.exceptions.ServiceException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun count(): Long {
        return memberRepository.count()
    }

    fun join(username: String, password: String, nickname: String): Member {
        findByUsername(username).ifPresent { throw ServiceException("400-1", "이미 존재하는 회원입니다.") }

        val member = Member(username, passwordEncoder.encode(password), nickname)

        return memberRepository.save(member)
    }

    fun findByUsername(username: String): Optional<Member> {
        return memberRepository.findByUsername(username)
    }
}
