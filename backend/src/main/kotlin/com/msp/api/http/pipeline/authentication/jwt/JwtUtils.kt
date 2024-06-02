package com.msp.api.http.pipeline.authentication.jwt

import com.msp.api.domain.UserRole
import com.msp.api.domain.UserRole.Companion.toRole
import com.msp.api.http.pipeline.exceptionHandler.exceptions.Unauthenticated
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Component
class JwtUtils(jwtConfiguration: JwtConfiguration) {

    private val tokenExpirationDate = 30L

    private val userID = "userID"
    private val userRole = "role"

    private val tokenKey = SecretKeySpec(
        jwtConfiguration.accessTokenSecret.toByteArray(),
        SECRET_KEY_ALGORITHM
    )

    private data class JwtPayload(val claims: Claims)

    private fun createJwtPayload(id: UUID, role: UserRole): JwtPayload {
        val claims = Jwts.claims()
        claims[userID] = id
        claims[userRole] = role
        return JwtPayload(claims)
    }

    fun createToken(userID: UUID, role: UserRole): String {
        val jwtPayload = createJwtPayload(userID, role)

        return JWToken(
            Jwts.builder()
                .setClaims(jwtPayload.claims)
                .signWith(tokenKey)
                .setExpiration(Date.from(Instant.now().plus(tokenExpirationDate, ChronoUnit.DAYS)))
                .compact()
        ).token
    }

    fun getUserInfo(token: String): Pair<UUID, UserRole> {
        val claims = getClaimsOfToken(token)

        val id = claims[userID].toUUID()
        val role = claims[userRole].toString().toRole()

        return Pair(id, role)
    }

    private fun getClaimsOfToken(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(tokenKey)
            .build()
            .parseClaimsJws(token).body
    }

    private fun Any?.toUUID(): UUID {
        return try {
            UUID.fromString(this.toString())
        } catch (e: Exception) {
            throw Unauthenticated()
        }
    }

    companion object {
        private const val SECRET_KEY_ALGORITHM = "HmacSHA512"
    }
}
