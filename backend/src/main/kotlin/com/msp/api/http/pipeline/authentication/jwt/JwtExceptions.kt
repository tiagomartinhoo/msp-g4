package com.msp.api.http.pipeline.authentication.jwt

import com.msp.api.http.pipeline.exceptionHandler.exceptions.Unauthenticated
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException

fun JwtException.message(): String =
    when (this) {
        is UnsupportedJwtException -> "the claimsJws argument does not represent an Claims JWS"
        is MalformedJwtException -> "the claimsJws string is not a valid JWS"
        is SignatureException -> "Validation Failed"
        is ExpiredJwtException -> Unauthenticated().message ?: "This token has expired"
        else -> "Error with the Token "
    }
