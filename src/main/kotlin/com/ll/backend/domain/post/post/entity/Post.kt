package com.ll.backend.domain.post.post.entity

import com.ll.backend.domain.member.member.entity.Member
import com.ll.backend.global.jpa.entity.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
class Post(
    @ManyToOne
    var author: Member,

    @Column(length = 100) var title: String,

    @Column(columnDefinition = "TEXT")
    var body: String,

    var published: Boolean = false
) : BaseTime()
