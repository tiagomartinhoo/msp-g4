package com.msp.api.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("checkIn")
data class CheckIn(
    @Id
    val pID: String,
    val value: Int
)
