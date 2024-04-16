package com.msp.api.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
    @Id
    val uId: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val nif: String,
    val role: UserRole
)
