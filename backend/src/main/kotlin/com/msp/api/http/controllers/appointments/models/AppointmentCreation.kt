package com.msp.api.http.controllers.appointments.models

import java.time.LocalDateTime

data class AppointmentCreation(
    val dID: String,
    val serviceID: String,
    val timeOfAppointment: LocalDateTime
)
