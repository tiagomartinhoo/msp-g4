package com.msp.api.services.checkIn

import com.msp.api.domain.CheckIn
import com.msp.api.http.pipeline.exceptionHandler.exceptions.AlreadyCheckIn
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NoAppointmentsOrExams
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotCheckIn
import com.msp.api.storage.repo.AppointmentsRepository
import com.msp.api.storage.repo.CheckInRepository
import com.msp.api.storage.repo.ExamScheduleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CheckInService(
    private val repo: CheckInRepository,
    private val appointmentsRepository: AppointmentsRepository,
    private val examScheduleRepository: ExamScheduleRepository
) {

    fun checkIn(pID: String): Int {
        if (repo.findByIdOrNull(pID) != null) {
            throw AlreadyCheckIn()
        }

        val now = LocalDateTime.now()

        val patientAppointments = appointmentsRepository.findBypID(pID).filter { it.timeOfAppointment.dayOfYear == now.dayOfYear }

        val patientExams = examScheduleRepository.findBypID(pID).filter { it.timeOfExam.dayOfYear == now.dayOfYear }

        if (patientAppointments.isEmpty() && patientExams.isEmpty()) throw NoAppointmentsOrExams()

        patientAppointments.map { appointment -> appointmentsRepository.save(appointment.copy(timeCheckIn = now)) }

        patientExams.map { examSchedule -> examScheduleRepository.save(examSchedule.copy(timeCheckIn = now)) }

        val state = repo.count().toInt()

        repo.insert(CheckIn(pID, state + 1))

        return state + 1
    }

    fun getCheckInState(pID: String): Int = repo.findByIdOrNull(pID)?.value ?: throw NotCheckIn()
}
