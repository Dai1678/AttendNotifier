package com.example.dai.attendnotifier.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class DayOfWeekModel : RealmObject() {
    @PrimaryKey
    open var id: Int = UUID.randomUUID().mostSignificantBits.toInt()  //固有ID
    open var dayOfWeekId: Int = Calendar.MONDAY  //このモデルが何曜日のものか表す
    //FIXME open var semesterId: Int = 0  //どのセメスターかを表すId
}