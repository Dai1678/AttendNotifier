package com.example.dai.attendnotifier.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class RecordRealmModel : RealmObject() { //1授業回ごとのモデル
    @PrimaryKey
    open var id: Int = UUID.randomUUID().mostSignificantBits.toInt()  //固有Id
    open var recordId: Int = 0  //どの授業の記録かを表すId == ClassworkModel.id
    open var classworkTimeId = 1  //何回目の授業を表すId 1 ~ 14
    open var name: String = "第1回"  //授業回数名
    open var dateStr: String = "授業日: 未設定"  //授業開講日
    open var attendStatus: Int = 0  //出席状況
}