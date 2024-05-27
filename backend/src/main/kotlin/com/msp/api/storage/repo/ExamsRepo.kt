package com.msp.api.storage.repo

import com.msp.api.domain.Exam
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamsRepo : MongoRepository<Exam, String>
