package com.msp.api.services

import com.msp.api.domain.Service
import com.msp.api.http.pipeline.exceptionHandler.exceptions.ServiceNotFound
import com.msp.api.storage.repo.ExamsRepo
import com.msp.api.storage.repo.ServicesRepo
import org.springframework.data.repository.findByIdOrNull

@org.springframework.stereotype.Service
class ServicesAvailableService(private val servicesRepo: ServicesRepo, private val examsRepo: ExamsRepo) {

    fun servicesAvailable(): List<Service> {
        return servicesRepo.findAll()
    }

    fun serviceById(serviceId: String): Service {
        return servicesRepo.findByIdOrNull(serviceId) ?: throw ServiceNotFound()
    }
}
