package com.msp.api.services

import com.msp.api.domain.Doctor
import com.msp.api.domain.UserRole
import com.msp.api.http.controllers.users.models.CreateDoctorInput
import com.msp.api.http.controllers.users.models.CreateUserInput
import com.msp.api.http.controllers.users.models.CreateUserOutput
import com.msp.api.http.controllers.users.models.DoctorOutput
import com.msp.api.http.controllers.users.models.toDoctorOutput
import com.msp.api.http.pipeline.exceptionHandler.exceptions.UserNotFound
import com.msp.api.services.utils.ServiceUtils
import com.msp.api.storage.repo.DoctorsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DoctorsService(
    private val serviceUtils: ServiceUtils,
    private val usersService: UsersService,
    @Autowired val repo: DoctorsRepository
) {

    fun createDoctor(input: CreateDoctorInput): CreateUserOutput {
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
            Doctor(
                dId = user.id.toString(),
                specialty = input.specialty
            )
        )

        return CreateUserOutput(user.id, user.token)
    }

    fun getDoctorById(dID: String): DoctorOutput {
        val user = usersService.getUserById(dID)
        val doctor = repo.findByDId(dID) ?: throw UserNotFound()
        return user.toDoctorOutput(doctor)
    }
}
