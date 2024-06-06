package com.msp.api.http.controllers.appointments.models

import java.time.LocalDateTime

data class AppointmentOutput(
    val aID: String,
    val doctorName: String,
    val pID: String,
    val serviceName: String,
    val timeOfAppointment: LocalDateTime,
    val timeCreated: LocalDateTime,
    val timeStarted: LocalDateTime?,
    val timeEnded: LocalDateTime?,
    val timeCheckIn: LocalDateTime?,
    val canceled: Boolean,
    val cancellationReason: String
)

data class AppointmentsOutput(
    val pageCount: Int,
    val list: List<AppointmentOutput>
)
