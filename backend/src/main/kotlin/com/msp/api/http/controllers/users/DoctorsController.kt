package com.msp.api.http.controllers.users

import com.msp.api.domain.User
import com.msp.api.http.Uris
import com.msp.api.http.controllers.users.models.CreateDoctorInput
import com.msp.api.http.controllers.users.models.CreateUserOutput
import com.msp.api.http.controllers.users.models.DoctorOutput
import com.msp.api.http.controllers.users.models.DoctorsOutput
import com.msp.api.http.controllers.users.models.UpdateDoctorInput
import com.msp.api.http.pipeline.authentication.Authentication
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotYourAccount
import com.msp.api.services.DoctorsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class DoctorsController(private val doctorsService: DoctorsService) {

    @PostMapping(Uris.DOCTORS)
    fun createDoctor(
        @RequestBody createDoctorInput: CreateDoctorInput
    ): ResponseEntity<CreateUserOutput> {
        val userOutput = doctorsService.createDoctor(createDoctorInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(userOutput)
    }

    @Authentication
    @GetMapping(Uris.DOCTOR_BY_ID)
    fun getDoctor(
        @PathVariable dID: String
    ): ResponseEntity<DoctorOutput> {
        return ResponseEntity.ok(doctorsService.getDoctorById(dID))
    }

    @GetMapping(Uris.DOCTORS)
    fun getDoctors(
        @RequestParam(required = false, defaultValue = DEFAULT_PAGE) page: Int,
        @RequestParam(required = false, defaultValue = DEFAULT_SIZE) size: Int,
        @RequestParam(required = false, defaultValue = "") text: String
    ): ResponseEntity<DoctorsOutput> {
        return ResponseEntity.ok(doctorsService.getDoctors(text, page, size))
    }

    @Authentication
    @PutMapping(Uris.DOCTOR_BY_ID)
    fun updateDoctor(
        @PathVariable dID: String,
        @RequestBody updateDoctorInput: UpdateDoctorInput,
        user: User
    ): ResponseEntity<DoctorOutput> {
        if (dID != user.uId) throw NotYourAccount()

        return ResponseEntity.ok(doctorsService.updateDoctor(dID, updateDoctorInput))
    }

    @Authentication
    @DeleteMapping(Uris.DOCTOR_BY_ID)
    fun deleteDoctor(
        @PathVariable dID: String,
        user: User
    ): ResponseEntity<Unit> {
        if (dID != user.uId) throw NotYourAccount()
        doctorsService.deleteDoctor(dID)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    companion object {
        const val DEFAULT_PAGE = "0"
        const val DEFAULT_SIZE = "10"
    }
}
