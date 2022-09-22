package com.hyuuny.hospitalmanagementsystem.patients

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/api/v1/patients"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
class PatientController(
    private val patientService: PatientService,
    private val patientResourceAssembler: PatientResourceAssembler,
    private val patientListingResourceAssembler: PatientListingResourceAssembler,
) {

    @PostMapping
    fun createPatient(@RequestBody request: PatientCreateRequest): EntityModel<PatientResponse> {
        val savedPatient = patientService.createPatient(request)
        return patientResourceAssembler.toModel(savedPatient)
    }

    @PutMapping("/{id}")
    fun updatePatient(
        @PathVariable id: Long,
        @RequestBody request: PatientUpdateRequest
    ): EntityModel<PatientResponse> {
        val updatedPatient = patientService.updatePatient(id, request)
        return patientResourceAssembler.toModel(updatedPatient)
    }

    @DeleteMapping("/{id}")
    fun deletePatient(@PathVariable id: Long): ResponseEntity<Any> {
        patientService.deletePatient(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getPatient(@PathVariable id: Long): EntityModel<PatientResponse> {
        val foundPatient = patientService.getPatient(id)
        return patientResourceAssembler.toModel(foundPatient)
    }

    @GetMapping
    fun retrievePatients(
        searchCondition: PatientSearchCondition,
        @PageableDefault(sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable,
        pagedResourcesAssembler: PagedResourcesAssembler<PatientListingResponse>,
    ): ResponseEntity<PagedModel<EntityModel<PatientListingResponse>>> {
        val page = patientService.retrievePatients(searchCondition, pageable)
        return ResponseEntity.ok(
            pagedResourcesAssembler.toModel(page, patientListingResourceAssembler)
        )
    }

    @Component
    class PatientResourceAssembler :
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

    @Component
    class PatientListingResourceAssembler :
        RepresentationModelAssembler<PatientListingResponse, EntityModel<PatientListingResponse>> {

        override fun toModel(entity: PatientListingResponse): EntityModel<PatientListingResponse> {
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