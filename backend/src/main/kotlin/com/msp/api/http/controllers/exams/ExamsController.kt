package com.msp.api.http.controllers.exams

import com.msp.api.domain.User
import com.msp.api.http.Uris
import com.msp.api.http.controllers.appointments.models.EntityCreationOutput
import com.msp.api.http.controllers.exams.domain.ExamCreation
import com.msp.api.http.controllers.exams.domain.ExamSchedule
import com.msp.api.http.controllers.exams.domain.ExamScheduledUpdate
import com.msp.api.http.controllers.exams.domain.ExamsScheduleOutput
import com.msp.api.http.controllers.services.domain.Exam
import com.msp.api.http.controllers.services.domain.ListOfExams
import com.msp.api.http.pipeline.authentication.Authentication
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotYourAccount
import com.msp.api.services.ExamsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ExamsController(private val service: ExamsService) {

    @GetMapping(Uris.EXAMS_AVAILABLE)
    fun getExamsAvailable(): ResponseEntity<ListOfExams> {
        val exams = service.getExamsAvailable()

        return ResponseEntity.ok(ListOfExams(exams))
    }

    @GetMapping(Uris.EXAM_BY_ID)
    fun getExam(@PathVariable id: String): ResponseEntity<Exam> {
        return ResponseEntity.ok(service.examById(id))
    }

    @Authentication
    @PostMapping(Uris.SCHEDULE_EXAM)
    fun scheduleExam(@PathVariable pID: String, @RequestBody examCreation: ExamCreation, user: User): ResponseEntity<EntityCreationOutput> {
        if (pID != user.uId) throw NotYourAccount()

        val id = service.scheduleExam(pID, examCreation)

        return ResponseEntity.status(HttpStatus.CREATED).body(EntityCreationOutput(id))
    }

    @Authentication
    @GetMapping(Uris.SCHEDULED_EXAM_BY_ID)
    fun scheduleExamById(@PathVariable eID: String, @PathVariable pID: String, user: User): ResponseEntity<ExamSchedule> {
        if (pID != user.uId) throw NotYourAccount()

        return ResponseEntity.ok(service.getExamScheduled(eID))
    }

    @GetMapping(Uris.SCHEDULE_EXAM)
    fun getExamsOfPatient(
        @RequestParam(required = false, defaultValue = DEFAULT_PAGE) page: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_SIZE) size: Int,
        @PathVariable pID: String
    ): ResponseEntity<ExamsScheduleOutput> {
        return ResponseEntity.ok(service.getExamsOfPatient(pID, page, size))
    }

    @Authentication
    @PutMapping(Uris.SCHEDULED_EXAM_BY_ID)
    fun updateExam(@PathVariable eID: String, @PathVariable pID: String, @RequestBody scheduledUpdate: ExamScheduledUpdate, user: User): ResponseEntity<Nothing> {
        if (pID != user.uId) throw NotYourAccount()

        service.updateExamScheduled(eID, pID, scheduledUpdate)

        return ResponseEntity.ok().build()
    }

    @Authentication
    @DeleteMapping(Uris.SCHEDULED_EXAM_BY_ID)
    fun deleteExam(@PathVariable eID: String, @PathVariable pID: String, user: User): ResponseEntity<Nothing> {
        if (pID != user.uId) throw NotYourAccount()

        service.deleteExam(eID, pID)

        return ResponseEntity.ok().build()
    }

    companion object {
        const val DEFAULT_PAGE = "0"
        const val DEFAULT_SIZE = "10"
    }
}
