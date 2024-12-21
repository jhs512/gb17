package com.ll.backend.domain.member.member.dto
import com.ll.backend.domain.member.member.entity.Member

data class MemberDto(
    val id: Long,
    val nickname: String,
    val name:String
) {
    constructor(member: Member) : this(
        id = member.id,
        nickname = member.nickname,
        name = member.name
    )
}
