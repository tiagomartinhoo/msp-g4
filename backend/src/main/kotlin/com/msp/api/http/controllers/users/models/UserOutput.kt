package com.msp.api.http.controllers.users.models

import com.msp.api.domain.Doctor
import com.msp.api.domain.Patient
import com.msp.api.domain.User
import com.msp.api.domain.UserRole
import java.time.LocalDate

data class UserOutput(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val nif: String,
    val role: UserRole
)

data class PatientOutput(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val nif: String,
    val birthDate: LocalDate,
    val address: String,
    val insurance: String?
)

data class DoctorOutput(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val nif: String,
    val specialty: String
)

fun User.toUserOutput() =
    UserOutput(uId, name, email, phoneNumber, nif, role)

fun UserOutput.toPatientOutput(patient: Patient) =
    PatientOutput(id, name, email, phoneNumber, nif, patient.birthDate, patient.address, patient.insurance)

fun UserOutput.toDoctorOutput(doctor: Doctor) =
    DoctorOutput(id, name, email, phoneNumber, nif, doctor.specialty)

data class UsersOutput(
    val pageCount: Int,
    val list: List<UserOutput>
)

data class PatientsOutput(
    val pageCount: Int,
    val list: List<PatientOutput>
)

data class DoctorsOutput(
    val pageCount: Int,
    val list: List<DoctorOutput>
)
