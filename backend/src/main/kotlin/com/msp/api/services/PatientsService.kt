package com.msp.api.services

import com.msp.api.domain.Patient
import com.msp.api.domain.UserRole
import com.msp.api.http.controllers.users.models.CreatePatientInput
import com.msp.api.http.controllers.users.models.CreateUserInput
import com.msp.api.http.controllers.users.models.CreateUserOutput
import com.msp.api.http.controllers.users.models.PatientOutput
import com.msp.api.http.controllers.users.models.PatientsOutput
import com.msp.api.http.controllers.users.models.UpdatePatientInput
import com.msp.api.http.controllers.users.models.UpdateUserInput
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
        val patient = repo.findBypId(pID) ?: throw UserNotFound()
        return user.toPatientOutput(patient)
    }

    fun getPatients(text: String, page: Int, size: Int): PatientsOutput {
        val users = usersService.getUsers(text, UserRole.PATIENT, page, size)
        val patients = users.list.map { getPatientById(it.id) }

        return PatientsOutput(users.pageCount, patients)
    }

    fun updatePatient(pId: String, input: UpdatePatientInput): PatientOutput {
        input.birthDate?.let { serviceUtils.isValidBirthDate(it) }

        val updatedUser = usersService.updateUser(
            uId = pId,
            input = UpdateUserInput(input.name, input.phoneNumber, input.password)
        )

        val patient = repo.findBypId(pId) ?: throw UserNotFound()
        val updatedPatient = repo.save(
            patient.copy(
                birthDate = input.birthDate ?: patient.birthDate,
                address = input.address ?: patient.address,
                insurance = input.insurance ?: patient.insurance
            )
        )

        return updatedUser.toPatientOutput(updatedPatient)
    }

    fun deletePatient(pId: String) {
        usersService.deleteUser(pId)
        repo.deleteBypId(pId)
    }
}
