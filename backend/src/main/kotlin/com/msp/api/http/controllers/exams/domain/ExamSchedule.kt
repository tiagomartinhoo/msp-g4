package com.msp.api.http.controllers.exams.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("examSchedule")
data class ExamSchedule(
    @Id
    val id: String,
    val eID: String,
    val pID: String,
    val timeOfExam: LocalDateTime,
    val timeCreated: LocalDateTime,
    val timeStarted: LocalDateTime? = null,
    val timeEnded: LocalDateTime? = null,
    val timeCheckIn: LocalDateTime? = null,
    val canceled: Boolean = false,
    val cancellationReason: String = ""
)
