package com.example.dai.attendnotifier.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class ClassworkModel : RealmObject() {  //1授業あたりのモデル
    @PrimaryKey
    open var id: Int = UUID.randomUUID().mostSignificantBits.toInt() //0  //固有Id
    open var dayOfWeekNumber: Int = Calendar.MONDAY  //曜日あたりのId Calendar.SUNDAY ~ Calendar.SATURDAY
    open var classworkNumber: Int =
        ClassworkNumberEnum.CLASSWORK_1.num  //時限番号 ClassworkNumberEnum.CLASSWORK_1.num ~ ClassworkNumberEnum.CLASSWORK_10.num
    open var classworkName: String = "授業"  //授業名
    open var isNotify: Boolean = true  //通知on/off
    open var notAttendRecord: Int = 0  //未受講数
    open var attendedRecord: Int = 0  //出席数
    open var lateRecord: Int = 0  //遅刻数
    open var absentRecord: Int = 0  //欠席数
    open var otherRecord: Int = 0  //その他

    open var timeRange: String = "9:00 ~ 10:50"  //授業の開始時刻 ~ 終了時刻
    open var startTime: Date = Date()
    open var endTime: Date = Date()
}
