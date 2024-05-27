package com.msp.api.http.controllers.exams.models

import java.time.LocalDateTime

data class ExamScheduleOutput(
    val id: String,
    val eID: String,
    val pID: String,
    val timeOfExam: LocalDateTime,
    val timeCreated: LocalDateTime,
    val timeStarted: LocalDateTime?,
    val timeEnded: LocalDateTime?,
    val timeCheckIn: LocalDateTime?,
    val canceled: Boolean = false,
    val cancellationReason: String
)

data class ExamsScheduleOutput(
    val pageCount: Int,
    val list: List<ExamScheduleOutput>
)
