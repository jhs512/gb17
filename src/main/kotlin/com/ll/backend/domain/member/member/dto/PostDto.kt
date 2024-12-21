package com.ll.backend.domain.member.member.dto

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.domain.post.post.entity.Post

data class MemberDto(
    val id: Long,
    val nickname: String
) {
    constructor(member: Member) : this(
        id = member.id,
        nickname = member.nickname
    )
}
