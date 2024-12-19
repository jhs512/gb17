package com.ll.backend.domain.member.member.repository

import com.ll.backend.domain.member.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByUsername(username: String): Optional<Member>
    fun findByRefreshToken(refreshToken: String): Optional<Member>
}
