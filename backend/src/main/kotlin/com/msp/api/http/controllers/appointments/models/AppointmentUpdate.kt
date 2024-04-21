package com.msp.api.http.controllers.appointments.models

import java.time.LocalDateTime

data class AppointmentUpdate(
    val dId : String? = null,
    val timeOfAppointment: LocalDateTime? = null
){
    init {
        require(dId != null || timeOfAppointment != null)
    }
}
