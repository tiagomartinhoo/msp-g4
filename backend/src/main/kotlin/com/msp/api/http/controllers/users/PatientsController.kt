package com.msp.api.http.controllers.users

import com.msp.api.http.Uris
import com.msp.api.http.controllers.users.models.CreatePatientInput
import com.msp.api.http.controllers.users.models.CreateUserOutput
import com.msp.api.http.controllers.users.models.PatientOutput
import com.msp.api.http.pipeline.authentication.Authentication
import com.msp.api.services.PatientsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class PatientsController(private val patientsService: PatientsService) {

    @PostMapping(Uris.PATIENTS)
    fun createPatient(
        @RequestBody createPatientInput: CreatePatientInput
    ): ResponseEntity<CreateUserOutput> {
        val userOutput = patientsService.createPatient(createPatientInput)

        return ResponseEntity.status(HttpStatus.CREATED).body(userOutput)
    }

    @Authentication
    @GetMapping(Uris.PATIENT_BY_ID)
    fun getPatient(
        @PathVariable pID: String
    ): ResponseEntity<PatientOutput> {
        return ResponseEntity.ok(patientsService.getPatientById(pID))
    }

    companion object {
        const val DEFAULT_PAGE = "0"
        const val DEFAULT_SIZE = "10"
    }
}
