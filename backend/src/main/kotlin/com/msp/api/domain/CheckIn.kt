package com.msp.api.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("checkIn")
data class CheckIn(
    @Id
    val pID: String,
    val date: LocalDate,
    val value: Int
)
