package com.msp.api.storage.repo

import com.msp.api.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UsersRepository : MongoRepository<User, String> {

    fun findByUId(uId: String): User?
    fun findByEmail(email: String): User?
    fun findByNif(nif: String): User?
}
