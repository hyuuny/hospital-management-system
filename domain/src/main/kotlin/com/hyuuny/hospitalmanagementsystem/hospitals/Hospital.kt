package com.hyuuny.hospitalmanagementsystem.hospitals

import com.hyuuny.hospitalmanagementsystem.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Hospital private constructor(
    name: String,
    code: String,
    hospitalDirectorName: String,
) : BaseEntity() {

    companion object {
        fun create(name: String, code: String, hospitalDirectorName: String): Hospital {
            return Hospital(
                name = name,
                code = code,
                hospitalDirectorName = hospitalDirectorName
            )
        }
    }


    @Column(nullable = false, length = 45)
    var name = name
        private set

    @Column(nullable = false, length = 20)
    var code = code
        private set

    @Column(nullable = false, length = 10)
    var hospitalDirectorName = hospitalDirectorName
        private set

    fun changeName(name: String) {
        this.name = name
    }

    fun changeCode(code: String) {
        this.code = code
    }

    fun changeHospitalDirectorName(hospitalDirectorName: String) {
        this.hospitalDirectorName = hospitalDirectorName
    }

}