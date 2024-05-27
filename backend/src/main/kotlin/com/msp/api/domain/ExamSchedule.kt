package com.msp.api.domain

import com.msp.api.http.controllers.exams.models.ExamScheduleOutput
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

fun ExamSchedule.toOutput() = ExamScheduleOutput(
    id = id,
    eID = eID,
    pID = pID,
    timeOfExam = timeOfExam,
    timeCreated = timeCreated,
    timeStarted = timeStarted,
    timeEnded = timeEnded,
    timeCheckIn = timeCheckIn,
    canceled = canceled,
    cancellationReason = cancellationReason
)
