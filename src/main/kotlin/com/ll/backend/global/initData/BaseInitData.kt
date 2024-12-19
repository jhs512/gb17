package com.ll.backend.global.initData

import com.ll.backend.domain.post.post.service.PostService
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BaseInitData(
    private val postService: PostService
) {
    @Bean
    fun initDataBaseApplicationRunner(): ApplicationRunner {
        return ApplicationRunner {
            if (postService.count() > 0) return@ApplicationRunner

            postService.write("안녕하세요.", "반갑습니다.")
            postService.write("Hello.", "Nice to meet you.")
        }
    }
}