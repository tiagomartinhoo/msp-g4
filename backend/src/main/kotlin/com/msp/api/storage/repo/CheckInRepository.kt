package com.msp.api.storage.repo

import com.msp.api.domain.CheckIn
import org.springframework.data.mongodb.repository.MongoRepository

interface CheckInRepository : MongoRepository<CheckIn,String> {
}