package com.msp.api.services.utils

import com.msp.api.domain.CheckIn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service

@Service
class SequenceGeneratorService {

    @Autowired
    private val mongoOperations: MongoOperations? = null

    fun getCurrentSequenceNumber(sequenceName: String): Int {
        val query = Query(Criteria.where("id").`is`(sequenceName))
        val dbSequence: CheckIn? = mongoOperations?.findOne(query, CheckIn::class.java)
        return dbSequence?.value ?: 0
    }
}
