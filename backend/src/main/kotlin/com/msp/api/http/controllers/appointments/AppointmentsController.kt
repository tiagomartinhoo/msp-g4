package com.msp.api.http.controllers.appointments

import com.msp.api.domain.User
import com.msp.api.http.Uris
import com.msp.api.http.controllers.appointments.models.Appointment
import com.msp.api.http.controllers.appointments.models.AppointmentCreation
import com.msp.api.http.controllers.appointments.models.EntityCreationOutput
import com.msp.api.http.controllers.appointments.models.AppointmentUpdate
import com.msp.api.http.pipeline.authentication.Authentication
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotYourAccount
import com.msp.api.services.AppointmentsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AppointmentsController(private val service : AppointmentsService) {


    @Authentication
    @PostMapping(Uris.APPOINTMENTS)
    fun createAppointment(@PathVariable pID: String, @RequestBody appointmentCreation: AppointmentCreation, user : User) : ResponseEntity<EntityCreationOutput>{

        if(pID != user.uId) throw NotYourAccount()

        val aiD = service.createAppointment(pID,appointmentCreation)

        return ResponseEntity.status(HttpStatus.CREATED).body(EntityCreationOutput(aiD))

    }
    @Authentication
    @GetMapping(Uris.APPOINTMENT_BY_ID)
    fun getAppointment(@PathVariable aID: String, @PathVariable pID: String, user : User) : ResponseEntity<Appointment>{

        if(pID != user.uId) throw NotYourAccount()

        return ResponseEntity.ok(service.getAppointment(aID))
    }
    @Authentication
    @PutMapping(Uris.APPOINTMENT_BY_ID)
    fun updateAppointment(@RequestBody appointmentUpdate: AppointmentUpdate, @PathVariable aID: String,
                          @PathVariable pID: String, user : User
    ) : ResponseEntity<Nothing>{

        if(pID != user.uId) throw NotYourAccount()

        service.updateAppointment(aID, appointmentUpdate)

        return ResponseEntity.ok().build()
    }
    @Authentication
    @DeleteMapping(Uris.APPOINTMENT_BY_ID)
    fun deleteAppointment( @PathVariable pID: String, @PathVariable aID: String, user : User) : ResponseEntity<Nothing>{

        if(pID != user.uId) throw NotYourAccount()

        service.deleteAppointment(aID,pID)


        return ResponseEntity.ok().build()
    }

}