package com.msp.api.http.controllers.appointments.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

data class AppointmentCreation(
    val dID : String,
    val pID : String,
    val serviceID : String,
    val timeOfAppointment : LocalDateTime
)

@Document("appointments")
data class Appointment(
    @Id
    val aID : String,
    val dID : String,
    val pID : String,
    val serviceID : String,
    val timeOfAppointment : LocalDateTime,
    val timeCreated : LocalDateTime,
    val timeStarted : LocalDateTime?= null,
    val timeEnded : LocalDateTime?= null,
    val timeCheckIn : LocalDateTime? = null,
    val canceled : Boolean = false,
    val cancellationReason : String = ""
)