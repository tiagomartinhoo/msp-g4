package com.msp.api.services.checkIn

import com.msp.api.domain.CheckIn
import com.msp.api.http.pipeline.exceptionHandler.exceptions.NotCheckIn
import com.msp.api.storage.repo.CheckInRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class CheckInService( private val repo : CheckInRepository) {

    fun checkIn(pID : String) : Int {

        val state =  repo.count().toInt()

        repo.insert(CheckIn(pID,state + 1))

        return state + 1
    }

    fun getCheckInState(pID : String) : Int = repo.findByIdOrNull(pID)?.value ?: throw NotCheckIn()


}