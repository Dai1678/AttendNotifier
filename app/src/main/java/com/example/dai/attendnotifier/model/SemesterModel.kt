package com.example.dai.attendnotifier.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class SemesterModel : RealmObject() {
    @PrimaryKey open var id: Int = 0
    open var semesterName: String = ""
    open var mondayId: Int = 0
    open var tuesdayId: Int = 0
    open var wednesdayId: Int = 0
    open var thursdayId: Int = 0
    open var fridayId: Int = 0
    open var saturdayId: Int = 0
    //FIXME open var sundayId: Int = 0
}