package com.example.dai.attendnotifier.model

import com.example.dai.attendnotifier.R

enum class DayOfWeekEnum(val num: Int, val nameResId: Int) {
    MONDAY(0, R.string.common_monday),
    TUESDAY(1, R.string.common_tuesday),
    WEDNESDAY(2, R.string.common_wednesday),
    THURSDAY(3, R.string.common_thursday),
    FRIDAY(4, R.string.common_friday),
    SATURDAY(5, R.string.common_saturday),
    SUNDAY(6, R.string.common_sunday)
}