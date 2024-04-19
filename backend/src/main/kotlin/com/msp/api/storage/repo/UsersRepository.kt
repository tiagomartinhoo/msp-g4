package com.msp.api.storage.repo

import com.msp.api.domain.User
import com.msp.api.domain.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UsersRepository : MongoRepository<User, String> {

    fun findByuId(uId: String): User?
    fun findByEmail(email: String): User?
    fun findByNif(nif: String): User?

    @Query(
        "{ \$and: [ " +
            "{ \$or: [ " +
            "{ 'name': { \$regex: ?0, \$options: 'i' } }, " +
            "{ 'email': { \$regex: ?0, \$options: 'i' } }, " +
            "{ 'phoneNumber': { \$regex: ?0, \$options: 'i' } }, " +
            "{ 'nif': { \$regex: ?0, \$options: 'i' } }" +
            "] }, " +
            "{ 'role': ?1 }" +
            "] }"
    )
    fun searchAllFieldsWithRole(query: String, role: UserRole, pageable: Pageable): Page<User>
}
