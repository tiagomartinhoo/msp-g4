package com.msp.api.storage.repo

import com.msp.api.http.controllers.services.domain.Service
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ServicesRepo : MongoRepository<Service, String>
