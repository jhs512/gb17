package com.ll.backend.domain.post.post.service

import com.ll.backend.domain.post.post.entity.Post
import com.ll.backend.domain.post.post.repository.PostRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun findById(id: Long): Optional<Post> {
        return postRepository.findById(id)
    }

    fun count(): Long {
        return postRepository.count()
    }

    fun write(title: String, body: String): Post {
        return postRepository.save(Post(title = title, body = body))
    }
}
