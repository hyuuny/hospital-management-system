package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.patients.QPatient.patient
import com.hyuuny.hospitalmanagementsystem.support.CustomQueryDslRepository
import com.hyuuny.hospitalmanagementsystem.visits.QVisit.visit
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class PatientRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(Patient::class.java), PatientRepositoryCustom {

    override fun retrievePatients(pageable: Pageable): PageImpl<SearchedPatientListing> {
        return applyPageImpl(
            pageable, queryFactory
                .select(
                    fields(
                        SearchedPatientListing::class.java,
                        patient.id,
                        patient.hospitalId,
                        patient.name,
                        patient.registerNo,
                        patient.gender,
                        patient.birthDay,
                        patient.mobilePhoneNumber,
                        visit.receptionDateTime,
                    )
                )
                .from(patient)
                .leftJoin(visit).on(visit.patient.id.eq(patient.id))
        )

    }
}