package com.msp.api.http.controllers.users.models

data class LoginOutput(
    val id: String,
    val token: String,
    val role: String
)
