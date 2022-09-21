package com.hyuuny.hospitalmanagementsystem.visits

enum class VisitStatus(private val title: String) {

    VISITING("방문중"),
    END("종료"),
    CANCELLATION("취소");

    companion object {
        operator fun invoke(status: String) = valueOf(status.uppercase())
    }

}