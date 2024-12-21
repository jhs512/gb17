package com.ll.backend.domain.member.member.service

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.domain.member.member.repository.MemberRepository
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.rsData.RsData
import com.ll.backend.global.security.SecurityUser
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val authTokenService: AuthTokenService,
    private val passwordEncoder: PasswordEncoder
) {
    fun count(): Long {
        return memberRepository.count()
    }

    fun join(username: String, password: String, nickname: String): Member {
        findByUsername(username)?.let {
            throw ServiceException(
                "409-1",
                "이미 존재하는 아이디입니다."
            )
        }

        val member = Member(
            username = username,
            password = passwordEncoder.encode(password),
            nickname = nickname,
            refreshToken = authTokenService.genRefreshToken()
        )

        return memberRepository.save(member)
    }

    fun findByUsername(username: String): Member? {
        return memberRepository.findByUsername(username).orElse(null)
    }

    fun validateToken(accessToken: String): Boolean {
        return authTokenService.validateToken(accessToken)
    }

    fun refreshAccessToken(refreshToken: String): RsData<String> {
        val member: Member = memberRepository.findByRefreshToken(refreshToken).orElseThrow {
            ServiceException(
                "404-1",
                "존재하지 않는 리프레시 토큰입니다."
            )
        }

        val accessToken = authTokenService.genAccessToken(member)

        return RsData("201-1", "엑세스 토큰이 생성되었습니다.", accessToken)
    }

    fun getUserFromAccessToken(accessToken: String): SecurityUser {
        val payloadBody = authTokenService.getDataFrom(accessToken)

        val id = (payloadBody["id"] as Int).toLong()
        val username = payloadBody["username"] as String
        val authorities = payloadBody["authorities"] as List<String>
        val createDate = LocalDateTime.now()
        val modifyDate = LocalDateTime.now()

        return SecurityUser(
            id,
            createDate,
            modifyDate,
            username,
            "",
            authorities.stream().map { role -> SimpleGrantedAuthority(role) }.toList()
        )
    }

    fun checkPasswordValidation(rawPassword: String, encodedPassword: String) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw ServiceException("401-1", "비밀번호가 일치하지 않습니다.")
        }
    }
}
