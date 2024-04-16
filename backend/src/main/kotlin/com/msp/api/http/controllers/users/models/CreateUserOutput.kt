package com.msp.api.http.controllers.users.models

import java.util.UUID

data class CreateUserOutput(
    val id: UUID,
    val token: String
)
