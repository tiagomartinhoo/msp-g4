package com.msp.api.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("db_sequence")
data class DBSequence(
    @Id
    val id: String,
    val seq: Int
)
