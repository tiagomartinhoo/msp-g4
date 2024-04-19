package com.msp.api.http.controllers.users.models

import java.time.LocalDate

data class UpdateUserInput(
    val name: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null
)

data class UpdatePatientInput(
    val name: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val birthDate: LocalDate? = null,
    val address: String? = null,
    val insurance: String? = null
)

data class UpdateDoctorInput(
    val name: String? = null,
    val phoneNumber: String? = null,
    val password: String? = null,
    val specialty: String? = null
)
