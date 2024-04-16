package com.msp.api.storage.repo

import com.msp.api.domain.Doctor
import org.springframework.data.mongodb.repository.MongoRepository

interface DoctorsRepository : MongoRepository<Doctor, String> {

    fun findByDId(dId: String): Doctor?
}
