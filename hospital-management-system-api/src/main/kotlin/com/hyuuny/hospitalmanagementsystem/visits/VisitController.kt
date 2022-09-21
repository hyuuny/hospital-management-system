package com.hyuuny.hospitalmanagementsystem.visits

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/api/v1/{patientId}}/visits"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class VisitController(
    private val visitService: VisitService,
) {

    @PostMapping
    fun createVisit() {

    }

    @PutMapping("/{id}")
    fun updateVisit() {

    }

    @DeleteMapping("/{id}")
    fun deleteVisit() {

    }

    @GetMapping("/{id}")
    fun getVisit() {

    }

}