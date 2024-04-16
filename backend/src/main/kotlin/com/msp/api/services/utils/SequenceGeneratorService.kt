package com.msp.api.services.utils

import com.msp.api.domain.DBSequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class SequenceGeneratorService {

    @Autowired
    private val mongoOperations: MongoOperations? = null

    fun getSequenceNumber(sequenceName: String): Int {
        val query = Query(Criteria.where("id").`is`(sequenceName))
        val update = Update().inc("seq", 1)
        val counter: DBSequence? = mongoOperations
            ?.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DBSequence::class.java
            )

        return counter?.seq ?: 1
    }
}
