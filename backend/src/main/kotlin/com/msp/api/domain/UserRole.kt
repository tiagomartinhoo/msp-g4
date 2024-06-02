package com.msp.api.domain

import com.msp.api.http.pipeline.exceptionHandler.exceptions.UserRoleNotFound

enum class UserRole {
    PATIENT,
    DOCTOR,
    ADMIN;

    companion object {
        fun String?.toRole(): UserRole =
            UserRole.entries.firstOrNull { this == it.name } ?: throw UserRoleNotFound()
    }
}
