package com.msp.api.http.controllers.services

import com.msp.api.http.Uris
import com.msp.api.http.controllers.services.domain.ListOfServices
import com.msp.api.http.controllers.services.domain.Service
import com.msp.api.services.ServicesAvailableService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ServicesController(private val service: ServicesAvailableService) {

    @GetMapping(Uris.SERVICES_AVAILABLE)
    fun getServicesAvailable(): ResponseEntity<ListOfServices> {
        val services = service.servicesAvailable()

        return ResponseEntity.ok(ListOfServices(services))
    }

    @GetMapping(Uris.SERVICE_BY_ID)
    fun getService(@PathVariable id: String): ResponseEntity<Service> {
        return ResponseEntity.ok(service.serviceById(id))
    }
}
