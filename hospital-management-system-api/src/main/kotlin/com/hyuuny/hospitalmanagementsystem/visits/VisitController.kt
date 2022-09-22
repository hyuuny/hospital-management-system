package com.hyuuny.hospitalmanagementsystem.visits

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RequestMapping(
    path = ["/api/v1/{patientId}/visits"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@RestController
class VisitController(
    private val visitService: VisitService,
    private val visitResourceAssembler: VisitResourceAssembler,
) {

    @PostMapping
    fun createVisit(
        @PathVariable patientId: Long,
        @RequestBody request: VisitCreateRequest
    ): EntityModel<VisitResponse> {
        val savedVisit = visitService.createVisit(patientId, request)
        return visitResourceAssembler.toModel(savedVisit)
    }

    @PutMapping("/{id}")
    fun updateVisit(
        @PathVariable patientId: Long,
        @PathVariable id: Long,
        @RequestBody request: VisitUpdateRequest
    ): EntityModel<VisitResponse> {
        val updatedVisit = visitService.updateVisit(id, request)
        return visitResourceAssembler.toModel(updatedVisit)
    }

    @GetMapping("/{id}")
    fun getVisit(
        @PathVariable patientId: Long,
        @PathVariable id: Long,
    ): EntityModel<VisitResponse> {
        val foundVisit = visitService.getVisit(id)
        return visitResourceAssembler.toModel(foundVisit)
    }

    @Component
    class VisitResourceAssembler :
        RepresentationModelAssembler<VisitResponse, EntityModel<VisitResponse>> {

        override fun toModel(entity: VisitResponse): EntityModel<VisitResponse> {
            return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(VisitController::class.java)
                        .getVisit(entity.patientId, entity.id)
                )
                    .withSelfRel()
            )
        }
    }

}