package com.msp.api.storage.repo

import com.msp.api.http.controllers.appointments.models.Appointment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AppointmentsRepository : MongoRepository<Appointment, String> {

    fun findByaID(aID: String): Appointment?

    fun findBypID(pID: String): List<Appointment>

    @Query(
        "{ '\$or': [ " +
            "{ 'aID': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'dID': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'pID': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'serviceID': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'timeOfAppointment': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'timeCreated': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'timeStarted': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'timeEnded': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'timeCheckIn': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'canceled': { '\$regex': ?0, '\$options': 'i' } }, " +
            "{ 'cancellationReason': { '\$regex': ?0, '\$options': 'i' } } " +
            "] }"
    )
    fun searchAllFields(text: String, pageable: Pageable): Page<Appointment>

    @Query("{ 'pID': ?0 }")
    fun searchAllByPID(pID: String, pageable: Pageable): Page<Appointment>
}
