package com.ll.backend.global.app

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AppConfig(
    @Value("\${spring.profiles.active}") private val activeProfileValue: String,
    @Value("\${custom.jwt.secretKey}") private val jwtSecretKeyValue: String,
    @Value("\${custom.accessToken.expirationSec}") private val accessTokenExpirationSecValue: Long,
    @Value("\${custom.site.frontUrl}") private val siteFrontUrlValue: String,
    @Value("\${custom.site.backUrl}") private val siteBackUrlValue: String,
    @Value("\${custom.site.cookieDomain}") private val siteCookieDomainValue: String,
    @Value("\${custom.temp.dirPath}") private val tempDirPathValue: String,
    @Value("\${custom.genFile.dirPath}") private val genFileDirPathValue: String,
    @Value("\${custom.site.name}") private val siteNameValue: String,
    private val objectMapperValue: ObjectMapper
) {
    companion object {
        lateinit var activeProfile: String
        lateinit var jwtSecretKey: String
        var accessTokenExpirationSec: Long = 0
        lateinit var siteFrontUrl: String
        lateinit var siteBackUrl: String
        lateinit var siteCookieDomain: String
        lateinit var tempDirPath: String
        lateinit var genFileDirPath: String
        lateinit var siteName: String
        lateinit var objectMapper: ObjectMapper
        const val basePageSize = 10
    }

    @PostConstruct
    fun initStaticFields() {
        activeProfile = activeProfileValue
        jwtSecretKey = jwtSecretKeyValue
        accessTokenExpirationSec = accessTokenExpirationSecValue
        siteFrontUrl = siteFrontUrlValue
        siteBackUrl = siteBackUrlValue
        siteCookieDomain = siteCookieDomainValue
        tempDirPath = tempDirPathValue
        genFileDirPath = genFileDirPathValue
        siteName = siteNameValue
        objectMapper = objectMapperValue
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}