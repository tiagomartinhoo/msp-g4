package com.msp.api.services

import com.msp.api.http.controllers.appointments.models.Appointment
import com.msp.api.http.controllers.appointments.models.AppointmentCreation
import com.msp.api.http.controllers.appointments.models.AppointmentUpdate
import com.msp.api.http.controllers.appointments.models.AppointmentsOutput
import com.msp.api.http.controllers.appointments.models.toOutput
import com.msp.api.http.pipeline.exceptionHandler.exceptions.AppointmentNotFound
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotYourAppointment
import com.msp.api.http.pipeline.exceptionHandler.exceptions.ServiceNotFound
import com.msp.api.services.utils.ServiceUtils
import com.msp.api.storage.repo.AppointmentsRepository
import com.msp.api.storage.repo.ServicesRepo
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class AppointmentsService(
    private val serviceUtils: ServiceUtils,
    private val repo: AppointmentsRepository,
    val servicesRepo: ServicesRepo
) {

    fun createAppointment(pID: String, appointmentCreation: AppointmentCreation): String {
        val aId = UUID.randomUUID().toString()

        servicesRepo.findByIdOrNull(appointmentCreation.serviceID) ?: throw ServiceNotFound()

        val appointment = Appointment(
            aId,
            appointmentCreation.dID,
            pID,
            appointmentCreation.serviceID,
            appointmentCreation.timeOfAppointment,
            LocalDateTime.now()
        )

        repo.insert(appointment)

        return aId
    }

    fun getAppointment(aID: String): Appointment {
        return repo.findByaID(aID) ?: throw AppointmentNotFound()
    }

    fun getAppointments(page: Int, size: Int): AppointmentsOutput {
        val pageable = serviceUtils.requestPagination(page, size)

        val appointments = repo.searchAllFields("", pageable)

        return AppointmentsOutput(appointments.totalPages, appointments.toList().map { it.toOutput() })
    }

    fun getAppointmentsOfPatient(pID: String, page: Int, size: Int): AppointmentsOutput {
        val sort = Sort.by(serviceUtils.sortDirection("DESC"), "timeOfAppointment")

        val pageable = serviceUtils.requestPagination(page, size, sort)

        val appointments = repo.searchAllByPID(pID, pageable)

        return AppointmentsOutput(appointments.totalPages, appointments.toList().map { it.toOutput() })
    }

    fun updateAppointment(aID: String, appointmentUpdate: AppointmentUpdate) {
        val appointment = getAppointment(aID)

        repo.save(
            appointment.copy(
                dID = appointmentUpdate.dId ?: appointment.dID,
                timeOfAppointment = appointmentUpdate.timeOfAppointment ?: appointment.timeOfAppointment
            )
        )
    }

    fun deleteAppointment(aID: String, pID: String) {
        val appointment = getAppointment(aID)

        if (appointment.pID != pID) throw NotYourAppointment()

        repo.deleteById(aID)
    }
}
