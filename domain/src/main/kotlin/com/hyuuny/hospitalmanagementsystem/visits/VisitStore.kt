package com.hyuuny.hospitalmanagementsystem.visits

interface VisitStore {

    fun store(visit: Visit): Visit

    fun delete(id: Long)

}