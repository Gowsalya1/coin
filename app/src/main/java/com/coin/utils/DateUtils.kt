package com.coin.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val FORMATTED_DATE = "EEEE, MMMM dd, yyyy"
    private const val SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    private const val YEAR = "yyyy"
    private const val MONTH = "MM"
    private const val DATE = "dd"
    private const val HOUR = "HH"
    private const val MINUTES = "mm"

    fun getFormattedDate(serverDate: String): String {
        try {
            var simpleDateFormat = SimpleDateFormat(SERVER_DATE_FORMAT, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = simpleDateFormat.parse(serverDate)
            simpleDateFormat = SimpleDateFormat(FORMATTED_DATE, Locale.getDefault())
            simpleDateFormat.timeZone = TimeZone.getDefault()
            return simpleDateFormat.format(date ?: "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return serverDate
    }

}