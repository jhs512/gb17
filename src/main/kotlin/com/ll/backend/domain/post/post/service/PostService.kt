package com.ll.backend.domain.post.post.service

import com.ll.backend.domain.post.post.entity.Post
import com.ll.backend.domain.post.post.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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

    fun write(title: String, body: String, published: Boolean): Post {
        return postRepository.save(Post(title = title, body = body, published = published))
    }

    fun findByPublishedPaged(published: Boolean, page: Int, pageSize: Int): Page<Post> {
        val pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")))

        return postRepository.findByPublished(published, pageable)
    }

    fun delete(id: Long) {
        postRepository.deleteById(id)
    }
}
