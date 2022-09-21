package com.hyuuny.hospitalmanagementsystem.patients

import com.hyuuny.hospitalmanagementsystem.domain.BaseEntity
import com.hyuuny.hospitalmanagementsystem.utils.CodeGenerator
import com.hyuuny.hospitalmanagementsystem.visits.Visit
import javax.persistence.*

@Entity
class Patient private constructor(

    @Column(nullable = false, unique = true)
    val hospitalId: Long,

    name: String,

    @Column(nullable = false, length = 13)
    val registerNo: String,

    gender: Gender,

    birthDay: String,

    mobilePhoneNumber: String,

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    val visits: MutableList<Visit>? = mutableListOf(),
) : BaseEntity() {

    companion object {
        fun create(
            hospitalId: Long,
            name: String,
            gender: String,
            birthDay: String,
            mobilePhoneNumber: String,
            visits: MutableList<Visit>? = mutableListOf(),
        ): Patient = Patient(
            hospitalId = hospitalId,
            name = name,
            registerNo = CodeGenerator().generateCode(),
            gender = Gender(gender),
            birthDay = birthDay,
            mobilePhoneNumber = mobilePhoneNumber,
            visits = visits,
        )
    }

    @Column(nullable = false, length = 45)
    var name = name
        private set

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    var gender = gender
        private set

    @Column(length = 10)
    var birthDay = birthDay
        private set

    @Column(length = 20)
    var mobilePhoneNumber = mobilePhoneNumber
        private set

    fun changeName(name: String) {
        this.name = name
    }

    fun changeGender(gender: String) {
        this.gender = Gender(gender)
    }

    fun changeBirthDay(birthDay: String) {
        this.birthDay = birthDay
    }

    fun changeMobilePhoneNumber(mobilePhoneNumber: String) {
        this.mobilePhoneNumber = mobilePhoneNumber
    }

}