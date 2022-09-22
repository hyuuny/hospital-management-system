package com.hyuuny.hospitalmanagementsystem.visits

import org.springframework.stereotype.Component

@Component
class VisitStoreImpl(
    private val visitRepository: VisitRepository,
) : VisitStore {

    override fun store(visit: Visit): Visit = visitRepository.save(visit)

    override fun delete(id: Long) = visitRepository.deleteById(id)

}