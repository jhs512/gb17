package com.ll.backend.domain.post.post.entity

import com.ll.backend.domain.post.author.entity.PostAuthor
import com.ll.backend.global.jpa.entity.BaseTime
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType.LAZY
import jakarta.persistence.ManyToOne

@Entity
class Post(
    @ManyToOne(fetch = LAZY)
    var author: PostAuthor,

    @Column(length = 100)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var body: String,

    var published: Boolean = false
) : BaseTime()
