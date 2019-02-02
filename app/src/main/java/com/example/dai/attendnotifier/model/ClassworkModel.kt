package com.example.dai.attendnotifier.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class ClassworkModel : RealmObject() {  //1授業あたりのモデル
    @PrimaryKey
    open var id: Int = UUID.randomUUID().mostSignificantBits.toInt() //0  //固有Id
    open var dayOfWeekId: Int = 0  //曜日あたりのId DayOfWeekEnum.MONDAY.num ~ DayOfWeekEnum.SATURDAY.num
    open var classworkNumberId: Int = 0  //時限あたりのId 0 ~ 6
    open var classworkName: String = "授業"  //授業名
    open var isNotify: Boolean = true  //通知on/off
    open var notAttendRecord: Int = 0  //未受講数
    open var attendedRecord: Int = 0  //出席数
    open var lateRecord: Int = 0  //遅刻数
    open var absentRecord: Int = 0  //欠席数
    open var otherRecord: Int = 0  //その他

    open var timeRange: String = ""  //授業の開始時刻 ~ 終了時刻
    open var startTime: Date = Date()
    open var endTime: Date = Date()
}