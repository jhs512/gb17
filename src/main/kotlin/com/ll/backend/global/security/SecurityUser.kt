package com.ll.backend.global.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User


class SecurityUser : User {
    constructor(
        id: Long,
        username: String,
        password: String,
        authorities: Collection<GrantedAuthority>
    ) : super(username, password, authorities) {
        this.id = id
    }

    val id: Long

    fun genAuthentication(): Authentication {
        return UsernamePasswordAuthenticationToken(
            this,
            this.password,
            this.authorities
        )
    }
}