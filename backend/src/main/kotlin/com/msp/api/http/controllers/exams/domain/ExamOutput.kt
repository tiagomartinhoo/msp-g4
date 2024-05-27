package com.msp.api.http.controllers.exams.domain

import java.time.LocalDateTime

data class ExamScheduleOutput(
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

data class ExamsScheduleOutput(
    val pageCount: Int,
    val list: List<ExamScheduleOutput>
)
