package com.msp.api.services.checkIn

import com.msp.api.storage.repo.CheckInRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ResetCheckIn(private val repo : CheckInRepository) {

    @Scheduled(cron = "0 0 2 * * ?", zone = "Europe/Lisbon")
    fun resetCheckIn(){
        repo.deleteAll()
    }


}