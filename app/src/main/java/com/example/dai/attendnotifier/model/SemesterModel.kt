package com.example.dai.attendnotifier.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class SemesterModel : RealmObject() {
    @PrimaryKey
    open var id: Int = UUID.randomUUID().mostSignificantBits.toInt()
    open var semesterName: String = ""
    open var sundayId: Int = UUID.randomUUID().mostSignificantBits.toInt()
    open var mondayId: Int = UUID.randomUUID().mostSignificantBits.toInt()
    open var tuesdayId: Int = UUID.randomUUID().mostSignificantBits.toInt()
    open var wednesdayId: Int = UUID.randomUUID().mostSignificantBits.toInt()
    open var thursdayId: Int = UUID.randomUUID().mostSignificantBits.toInt()
    open var fridayId: Int = UUID.randomUUID().mostSignificantBits.toInt()
    open var saturdayId: Int = UUID.randomUUID().mostSignificantBits.toInt()
}