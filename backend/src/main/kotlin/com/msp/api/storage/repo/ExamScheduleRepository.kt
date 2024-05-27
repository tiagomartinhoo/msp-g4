package com.msp.api.storage.repo

import com.msp.api.domain.ExamSchedule
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ExamScheduleRepository : MongoRepository<ExamSchedule, String> {
    fun findBypID(pID: String): List<ExamSchedule>

    @Query("{ 'pID': ?0 }")
    fun searchAllByPID(pID: String, pageable: Pageable): Page<ExamSchedule>
}
