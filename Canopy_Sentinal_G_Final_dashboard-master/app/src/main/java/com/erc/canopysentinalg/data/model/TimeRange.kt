package com.erc.canopysentinalg.data.model

enum class TimeRange(val months: Int, val displayName: String) {
    MONTH_1(1, "1 Month"),
    MONTH_3(3, "3 Months"),
    MONTH_6(6, "6 Months"),
    YEAR_1(12, "1 Year"),
    YEAR_2(24, "2 Years"),
    YEAR_5(60, "5 Years"),
    ALL_TIME(0, "All Time")
}
