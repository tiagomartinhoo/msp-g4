package com.msp.api.services

import com.msp.api.domain.Patient
import com.msp.api.domain.UserRole
import com.msp.api.http.controllers.users.models.CreatePatientInput
import com.msp.api.http.controllers.users.models.CreateUserInput
import com.msp.api.http.controllers.users.models.CreateUserOutput
import com.msp.api.http.controllers.users.models.PatientOutput
import com.msp.api.http.controllers.users.models.toPatientOutput
import com.msp.api.http.pipeline.exceptionHandler.exceptions.UserNotFound
import com.msp.api.services.utils.ServiceUtils
import com.msp.api.storage.repo.PatientsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PatientsService(
    private val serviceUtils: ServiceUtils,
    private val usersService: UsersService,
    @Autowired val repo: PatientsRepository
) {

    fun createPatient(input: CreatePatientInput): CreateUserOutput {
        serviceUtils.isValidBirthDate(input.birthDate)

        val user = usersService.createUser(
            CreateUserInput(
                name = input.name,
                email = input.email,
                phoneNumber = input.phoneNumber,
                password = input.password,
                nif = input.nif,
                role = UserRole.PATIENT.toString()
            )
        )

        repo.insert(
            Patient(
                pId = user.id.toString(),
                birthDate = input.birthDate,
                address = input.address,
                insurance = input.insurance
            )
        )

        return CreateUserOutput(user.id, user.token)
    }

    fun getPatientById(pID: String): PatientOutput {
        val user = usersService.getUserById(pID)
        val patient = repo.findByPId(pID) ?: throw UserNotFound()
        return user.toPatientOutput(patient)
    }
}
