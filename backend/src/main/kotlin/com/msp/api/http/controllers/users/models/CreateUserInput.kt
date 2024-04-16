package com.msp.api.http.controllers.users.models

import java.time.LocalDate

data class CreateUserInput(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val nif: String,
    val role: String
)

data class CreatePatientInput(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val nif: String,
    val birthDate: LocalDate,
    val address: String,
    val insurance: String?
)

data class CreateDoctorInput(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val nif: String,
    val specialty: String
)
