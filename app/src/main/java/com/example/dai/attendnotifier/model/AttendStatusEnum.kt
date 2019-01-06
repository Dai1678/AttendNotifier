package com.example.dai.attendnotifier.model

import com.example.dai.attendnotifier.R

enum class AttendStatusEnum(val num: Int, val statusNameResId: Int) {
    NOT_ATTEND(0, R.string.common_not_attend),
    ATTENDED(1, R.string.common_attended),
    LATE(2, R.string.common_late),
    ABSENT(3, R.string.common_absent),
    OTHER(4, R.string.common_other_status)
}