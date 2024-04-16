package com.msp.api.http.pipeline.authentication

import com.msp.api.domain.User
import com.msp.api.http.pipeline.authentication.jwt.JwtUtils
import com.msp.api.http.pipeline.exceptionHandler.exceptions.Unauthenticated
import com.msp.api.http.pipeline.exceptionHandler.exceptions.UserNotFound
import com.msp.api.services.UsersService
import org.springframework.stereotype.Component

@Component
class AuthorizationHeaderProcessor(
    private val usersService: UsersService,
    private val jwtUtils: JwtUtils
) {

    fun process(authorizationValue: String?): User? {
        val parts = authorizationValue?.trim()?.split(" ") ?: return null
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }

        val token: String = parts[1]

        val (id, role) = jwtUtils.getUserInfo(token)

        try {
            val user = usersService.getUserById(id.toString())
            return User(user.id, user.name, user.email, user.phoneNumber, "", user.nif, user.role)
        } catch (unf: UserNotFound) {
            throw Unauthenticated()
        }
    }

    companion object {
        const val SCHEME = "bearer"
    }
}
