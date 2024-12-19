package com.ll.backend.global.security

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
}