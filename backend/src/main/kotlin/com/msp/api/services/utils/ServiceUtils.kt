package com.msp.api.services.utils

import com.msp.api.domain.UserRole
import com.msp.api.http.pipeline.authentication.jwt.JwtUtils
import com.msp.api.http.pipeline.exceptionHandler.exceptions.BadEmail
import com.msp.api.http.pipeline.exceptionHandler.exceptions.BadPhoneNumber
import com.msp.api.http.pipeline.exceptionHandler.exceptions.InvalidBirthDate
import com.msp.api.http.pipeline.exceptionHandler.exceptions.InvalidPage
import com.msp.api.http.pipeline.exceptionHandler.exceptions.InvalidSize
import com.msp.api.http.pipeline.exceptionHandler.exceptions.WeakPassword
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.time.LocalDate
import java.time.Period
import java.util.*
import kotlin.random.asKotlinRandom

@Component
class ServiceUtils(
    private val jwtUtils: JwtUtils
) {
    companion object {
        private const val EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
        private const val PHONE_NUMBER_REGEX = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}\$"
        private const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}\$"
        private const val MINIMUM_YEARS = 16
    }

    fun isValidEmail(email: String) = email.matches(Regex(EMAIL_REGEX))
    fun isValidPhoneNumber(phoneNumber: String) = phoneNumber.matches(Regex(PHONE_NUMBER_REGEX))
    fun isPasswordSafe(password: String) = password.matches(Regex(PASSWORD_REGEX))
    fun isValidBirthDate(birthDate: LocalDate) {
        if (Period.between(birthDate, LocalDate.now()).years < MINIMUM_YEARS) throw InvalidBirthDate()
    }

    fun checkDetails(email: String, phoneNumber: String, password: String) {
        if (!isValidEmail(email)) throw BadEmail()
        if (!isValidPhoneNumber(phoneNumber)) throw BadPhoneNumber()
        if (!isPasswordSafe(password)) throw WeakPassword()
    }

    fun createCredentials(role: UserRole): Pair<UUID, String> {
        val userID = UUID.randomUUID()
        val accessToken = createToken(userID, role)

        return Pair(userID, accessToken)
    }

    fun createToken(id: UUID, role: UserRole): String {
        return jwtUtils.createToken(id, role)
    }

    fun requestPagination(page: Int, size: Int, sort: Sort? = null): PageRequest {
        if (page < 0) throw InvalidPage()
        if (size < 1) throw InvalidSize()

        return if (sort != null) PageRequest.of(page, size, sort) else PageRequest.of(page, size)
    }

    fun sortDirection(direction: String) = Sort.Direction.entries.find { direction == it.name } ?: Sort.DEFAULT_DIRECTION

    fun generateSecurePassword(length: Int, requireUppercase: Boolean = true, requireDigits: Boolean = true, requireSpecialChars: Boolean = true): String {
        val secureRandom = SecureRandom().asKotlinRandom()
        val password = StringBuilder()

        val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
        val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val digitChars = "0123456789"
        val specialChars = "@#$%^&+="

        val allChars = StringBuilder(lowercaseChars)

        if (requireUppercase) {
            password.append(uppercaseChars.random(secureRandom))
            allChars.append(uppercaseChars)
        }
        if (requireDigits) {
            password.append(digitChars.random(secureRandom))
            allChars.append(digitChars)
        }
        if (requireSpecialChars) {
            password.append(specialChars.random(secureRandom))
            allChars.append(specialChars)
        }

        while (password.length < length) {
            password.append(allChars.random(secureRandom))
        }

        return password.toList().shuffled(secureRandom).joinToString("")
    }
}
