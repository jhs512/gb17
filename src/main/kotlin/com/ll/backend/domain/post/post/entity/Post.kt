package com.ll.backend.domain.post.post.entity

import com.ll.backend.domain.post.author.entity.Author
import com.ll.backend.global.jpa.entity.BaseTime
import jakarta.persistence.*
import jakarta.persistence.FetchType.LAZY

@Entity
class Post(
    @ManyToOne(fetch = LAZY)
    var author: Author,

    @Column(length = 100)
    var title: String,

    var published: Boolean = false
) : BaseTime() {
    @OneToOne(fetch = LAZY, mappedBy = "post", cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @PrimaryKeyJoinColumn
    lateinit var body: PostBody
}
