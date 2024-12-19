package com.ll.backend.domain.member.member.entity

import com.ll.backend.global.jpa.entity.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import org.springframework.security.core.GrantedAuthority

@Entity
class Member(
    @Column(unique = true, length = 30)
    var username: String,

    @Column(length = 100)
    var password: String,

    @Column(length = 50)
    var nickname: String,

    @Column(unique = true, length = 30)
    var refreshToken: String
) : BaseTime() {
    val authorities: Collection<GrantedAuthority>
        get() {
            if (username == "admin") {
                return listOf(GrantedAuthority { "ROLE_ADMIN" })
            }

            return listOf()
        }
}
