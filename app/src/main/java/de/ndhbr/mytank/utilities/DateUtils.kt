package de.ndhbr.mytank.utilities

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {
        fun humanReadableDate(date: Date): String {
            val formatter = SimpleDateFormat("d. MMMM yyyy", Locale.getDefault())
            return formatter.format(date)
        }
    }
}