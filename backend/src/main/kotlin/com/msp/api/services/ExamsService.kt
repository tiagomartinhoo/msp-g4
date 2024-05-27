package com.msp.api.services

import com.msp.api.http.controllers.exams.domain.ExamCreation
import com.msp.api.http.controllers.exams.domain.ExamSchedule
import com.msp.api.http.controllers.exams.domain.ExamScheduledUpdate
import com.msp.api.http.controllers.exams.domain.ExamsScheduleOutput
import com.msp.api.http.controllers.exams.domain.toOutput
import com.msp.api.http.controllers.services.domain.Exam
import com.msp.api.http.pipeline.exceptionHandler.exceptions.ExamNotFound
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotYourExam
import com.msp.api.services.utils.ServiceUtils
import com.msp.api.storage.repo.ExamScheduleRepository
import com.msp.api.storage.repo.ExamsRepo
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class ExamsService(
    private val serviceUtils: ServiceUtils,
    private val examsRepo: ExamsRepo,
    private val examScheduleRepo: ExamScheduleRepository
) {

    fun getExamsAvailable(): List<Exam> {
        return examsRepo.findAll()
    }

    fun examById(examId: String): Exam {
        return examsRepo.findByIdOrNull(examId) ?: throw ExamNotFound()
    }

    fun scheduleExam(pID: String, examCreation: ExamCreation): String {
        val id = UUID.randomUUID().toString()

        val examSchedule = ExamSchedule(id, examCreation.examID, pID, examCreation.examDateTime, LocalDateTime.now())

        examScheduleRepo.insert(examSchedule)

        return id
    }

    fun getExamScheduled(examId: String): ExamSchedule {
        return examScheduleRepo.findByIdOrNull(examId) ?: throw ExamNotFound()
    }

    fun getExamsOfPatient(pID: String, page: Int, size: Int): ExamsScheduleOutput {
        val sort = Sort.by(serviceUtils.sortDirection("DESC"), "timeOfExam")

        val pageable = serviceUtils.requestPagination(page, size, sort)

        val exams = examScheduleRepo.searchAllByPID(pID, pageable)

        return ExamsScheduleOutput(exams.totalPages, exams.toList().map { it.toOutput() })
    }

    fun updateExamScheduled(eID: String, pID: String, examScheduledUpdate: ExamScheduledUpdate) {
        val examScheduled = getExamScheduled(eID)

        if (examScheduled.pID != pID) throw NotYourExam()

        examScheduleRepo.save(examScheduled.copy(timeOfExam = examScheduledUpdate.date))
    }

    fun deleteExam(eID: String, pID: String) {
        val examScheduled = getExamScheduled(eID)

        if (examScheduled.pID != pID) throw NotYourExam()

        examScheduleRepo.deleteById(eID)
    }
}
