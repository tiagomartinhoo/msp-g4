package com.msp.api.storage.repo

import com.msp.api.domain.Patient
import org.springframework.data.mongodb.repository.MongoRepository

interface PatientsRepository : MongoRepository<Patient, String> {

    fun findBypId(pId: String): Patient?
    fun deleteBypId(pId: String)
}
