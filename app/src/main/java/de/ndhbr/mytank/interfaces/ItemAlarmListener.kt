package de.ndhbr.mytank.interfaces

import de.ndhbr.mytank.models.ItemAlarm

interface ItemAlarmListener {
    fun onItemAlarmClick(itemAlarm: ItemAlarm)
    fun onItemAlarmLongPress(itemAlarm: ItemAlarm): Boolean
}