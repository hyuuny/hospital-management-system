package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/api/v1/patients"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class PatientController(
    private val patientService: PatientService,
) {
가
    @PostMapping
    fun createPatient() {

    }

    @PutMapping("/{id}")
    fun updatePatient() {

    }

    @DeleteMapping("/{id}")
    fun deletePatient() {

    }

    @GetMapping("/{id}")
    fun getPatient() {

    }

    @GetMapping
    fun retrievePatients() {

    }


}