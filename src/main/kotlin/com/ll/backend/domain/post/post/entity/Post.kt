package com.ll.backend.domain.post.post.entity

import com.ll.gb.global.jpa.entity.BaseTime
import jakarta.persistence.Entity

@Entity
class Post(
    var title: String = "",
    var body: String = "",
    var published: Boolean = false
) : BaseTime()
