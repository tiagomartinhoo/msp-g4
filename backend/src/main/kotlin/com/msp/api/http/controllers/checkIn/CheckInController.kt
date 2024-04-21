package com.msp.api.http.controllers.checkIn

import com.msp.api.domain.User
import com.msp.api.http.Uris
import com.msp.api.http.controllers.checkIn.domain.CheckInOutput
import com.msp.api.http.pipeline.authentication.Authentication
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotYourAccount
import com.msp.api.services.checkIn.CheckInService
import com.msp.api.services.utils.SequenceGeneratorService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CheckInController(private val checkInService: CheckInService) {

    @Authentication
    @PostMapping(Uris.CHECK_IN)
    fun checkIn(@PathVariable pID: String, user : User) : ResponseEntity<CheckInOutput>{

        if(pID != user.uId) throw NotYourAccount()

        return ResponseEntity.status(HttpStatus.CREATED).body(CheckInOutput(checkInService.checkIn(pID)))

    }

    @Authentication
    @GetMapping(Uris.CHECK_IN)
    fun checkInState(@PathVariable pID: String, user : User) : ResponseEntity<CheckInOutput>{

        if(pID != user.uId) throw NotYourAccount()

        return ResponseEntity.ok(CheckInOutput(checkInService.getCheckInState(pID)))

    }

}