package com.msp.api.services

import com.msp.api.domain.User
import com.msp.api.domain.UserRole
import com.msp.api.domain.UserRole.Companion.toRole
import com.msp.api.http.controllers.users.models.CreateUserInput
import com.msp.api.http.controllers.users.models.CreateUserOutput
import com.msp.api.http.controllers.users.models.LoginOutput
import com.msp.api.http.controllers.users.models.UpdateUserInput
import com.msp.api.http.controllers.users.models.UserOutput
import com.msp.api.http.controllers.users.models.UsersOutput
import com.msp.api.http.controllers.users.models.toUserOutput
import com.msp.api.http.pipeline.exceptionHandler.exceptions.EmailAlreadyExists
import com.msp.api.http.pipeline.exceptionHandler.exceptions.LoginFailed
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NifAlreadyExists
import com.msp.api.http.pipeline.exceptionHandler.exceptions.UserNotFound
import com.msp.api.services.utils.ServiceUtils
import com.msp.api.storage.cloud.CloudStorageUtils
import com.msp.api.storage.repo.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import com.msp.api.services.utils.Hash
import java.util.*

@Service
class UsersService(
    private val serviceUtils: ServiceUtils,
    private val cloudStorageUtils: CloudStorageUtils,
    @Autowired val repo: UsersRepository
) {

    fun login(email: String, password: String): LoginOutput {
        val user = repo.findByEmail(email) ?: throw UserNotFound()

        if (user.password != Hash.of(password)) throw LoginFailed()

        val token = serviceUtils.createToken(UUID.fromString(user.uId), user.role)

        return LoginOutput(token)
    }

    fun createUser(input: CreateUserInput): CreateUserOutput {
        serviceUtils.checkDetails(input.email, input.phoneNumber, input.password)

        if (repo.findByEmail(input.email) != null) throw EmailAlreadyExists()
        if (repo.findByNif(input.nif) != null) throw NifAlreadyExists()

        val (userID, accessToken) = serviceUtils.createCredentials(input.role.toRole())

        repo.insert(
            User(
                uId = userID.toString(),
                name = input.name,
                email = input.email,
                phoneNumber = input.phoneNumber,
                password = Hash.of(input.password),
                nif = input.nif,
                role = input.role.toRole()
            )
        )

        return CreateUserOutput(userID, accessToken)
    }

    fun uploadProfilePicture(uid: String, profilePicture: ByteArray) {
        cloudStorageUtils.uploadProfilePicture(uid, profilePicture)
    }

    fun getProfilePicture(uid: String): ByteArray =
        cloudStorageUtils.downloadProfilePicture(uid)

    fun getUserById(uid: String): UserOutput {
        val user = repo.findByuId(uid) ?: throw UserNotFound()
        return user.toUserOutput()
    }

    fun getUsers(text: String, role: UserRole, page: Int, size: Int): UsersOutput {
        val pageable = serviceUtils.requestPagination(page, size)

        val users: Page<User> = repo.searchAllFieldsWithRole(text, role, pageable)

        return UsersOutput(users.totalPages, users.toList().map { it.toUserOutput() })
    }

    fun updateUser(uId: String, input: UpdateUserInput): UserOutput {
        input.phoneNumber?.let { serviceUtils.isValidPhoneNumber(it) }
        input.password?.let { serviceUtils.isPasswordSafe(it) }

        val user = repo.findByuId(uId) ?: throw UserNotFound()
        return repo.save(
            user.copy(
                name = input.name ?: user.name,
                phoneNumber = input.phoneNumber.also { it?.let { serviceUtils.isValidPhoneNumber(it) } } ?: user.phoneNumber,
                password = input.password?.let { Hash.of(it) } ?: user.password
            )
        ).toUserOutput()
    }

    fun deleteUser(uId: String) {
        val user = repo.findByuId(uId) ?: throw UserNotFound()
        repo.delete(user)
    }
}
