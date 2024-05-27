package com.msp.api.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("exams")
data class Exam(
    @Id
    val id: String,
    val name: String,
    val price: Float,
    val description: String
)
