package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/api/v1/patients"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class PatientController(
    private val patientService: PatientService,
    private val patientResourceAssembler: PatientResourceAssembler,
) {

    @PostMapping
    fun createPatient(@RequestBody request: PatientCreateRequest): EntityModel<PatientResponse> {
        val savedPatient = patientService.createPatient(request)
        return patientResourceAssembler.toModel(savedPatient)
    }

    @GetMapping("/{id}")
    fun getPatient(@PathVariable id: Long): EntityModel<PatientResponse> {
        val foundPatient = patientService.getPatient(id)
        return patientResourceAssembler.toModel(foundPatient)
    }

    @GetMapping
    fun retrievePatients() {

    }

    @Component
    companion object PatientResourceAssembler :
        RepresentationModelAssembler<PatientResponse, EntityModel<PatientResponse>> {

        override fun toModel(entity: PatientResponse): EntityModel<PatientResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(PatientController::class.java)
                        .getPatient(entity.id)
                )
                    .withSelfRel()
            )
        }
    }


}