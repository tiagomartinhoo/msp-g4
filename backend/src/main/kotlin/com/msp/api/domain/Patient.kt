package com.msp.api.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("patients")
data class Patient(
    @Id
    val pId: String,
    val birthDate: LocalDate,
    val address: String,
    val insurance: String?
)
