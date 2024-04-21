package com.msp.api.storage.repo

import com.msp.api.http.controllers.appointments.models.Appointment
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface AppointmentsRepository : MongoRepository<Appointment, String>{

    fun findByaID(aID: String) : Appointment?




}