package com.msp.api.http.controllers.appointments.models

import java.time.LocalDateTime

data class AppointmentOutput(
    val aID: String,
    val dID: String,
    val pID: String,
    val serviceID: String,
    val timeOfAppointment: LocalDateTime,
    val timeCreated: LocalDateTime,
    val timeStarted: LocalDateTime? = null,
    val timeEnded: LocalDateTime? = null,
    val timeCheckIn: LocalDateTime? = null,
    val canceled: Boolean = false,
    val cancellationReason: String = ""
)

data class AppointmentsOutput(
    val pageCount: Int,
    val list: List<AppointmentOutput>
)
