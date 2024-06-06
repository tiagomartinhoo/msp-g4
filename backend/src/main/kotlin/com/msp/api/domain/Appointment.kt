package com.msp.api.domain

import com.msp.api.http.controllers.appointments.models.AppointmentOutput
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("appointments")
data class Appointment(
    @Id
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

fun Appointment.toOutput(doctorName: String, serviceName: String) = AppointmentOutput(
    aID = aID,
    doctorName = doctorName,
    pID = pID,
    serviceName = serviceName,
    timeOfAppointment = timeOfAppointment,
    timeCreated = timeCreated,
    timeStarted = timeStarted,
    timeEnded = timeEnded,
    timeCheckIn = timeCheckIn,
    canceled = canceled,
    cancellationReason = cancellationReason
)
