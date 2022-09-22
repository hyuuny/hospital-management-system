package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.patients.QPatient.patient
import com.hyuuny.hospitalmanagementsystem.support.CustomQueryDslRepository
import com.hyuuny.hospitalmanagementsystem.visits.QVisit.visit
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections.fields
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.ObjectUtils.isEmpty

class PatientRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomQueryDslRepository(Patient::class.java), PatientRepositoryCustom {

    override fun retrievePatients(
        searchCondition: PatientSearchCondition,
        pageable: Pageable,
    ): PageImpl<SearchedPatientListing> {
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
                        ExpressionUtils.`as`(visit.receptionDateTime.max(), "receptionDateTime"),
                    )
                )
                .from(patient)
                .leftJoin(visit).on(visit.patient.id.eq(patient.id))
                .where(
                    nameEq(searchCondition.name),
                    registerNoEq(searchCondition.registerNo),
                    birthDayEq(searchCondition.birthDay),
                )
        )

    }

    private fun nameEq(name: String?) = if (isEmpty(name)) null else patient.name.eq(name)

    private fun registerNoEq(registerNo: String?) =
        if (isEmpty(registerNo)) null else patient.registerNo.eq(registerNo)

    private fun birthDayEq(birthDay: String?) =
        if (isEmpty(birthDay)) null else patient.birthDay.eq(birthDay)

}