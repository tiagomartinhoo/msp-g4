package com.msp.api.storage.repo

import com.msp.api.http.controllers.exams.domain.ExamSchedule
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamScheduleRepository : MongoRepository<ExamSchedule, String> {
    fun findBypID(pID: String): List<ExamSchedule>
}
