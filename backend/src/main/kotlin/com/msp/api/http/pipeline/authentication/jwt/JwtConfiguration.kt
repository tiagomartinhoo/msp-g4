package com.msp.api.http.pipeline.authentication.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfiguration(
    @Value("\${access-token-secret}")
    val accessTokenSecret: String
)
