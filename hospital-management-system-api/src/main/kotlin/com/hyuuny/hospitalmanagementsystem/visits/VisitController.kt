package com.hyuuny.hospitalmanagementsystem.visits

import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping(
    path = ["/api/v1/{patientId}/visits"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
@RestController
class VisitController(
    private val visitService: VisitService,
    private val visitResourceAssembler: VisitResourceAssembler,
) {

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