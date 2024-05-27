package com.msp.api.storage.repo

import com.msp.api.domain.Service
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ServicesRepo : MongoRepository<Service, String>
