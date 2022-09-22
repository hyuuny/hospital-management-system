package com.hyuuny.hospitalmanagementsystem.visits

import com.hyuuny.hospitalmanagementsystem.exception.NotFountException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class VisitReaderImpl(
    private val visitRepository: VisitRepository,
) : VisitReader {

    override fun getVisit(id: Long): Visit =
        visitRepository.findByIdOrNull(id) ?: throw NotFountException("환자 방문 이력을 찾을 수 없습니다.")

}