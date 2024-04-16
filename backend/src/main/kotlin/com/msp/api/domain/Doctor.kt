package com.msp.api.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("doctors")
data class Doctor(
    @Id
    val dId: String,
    val specialty: String
)
