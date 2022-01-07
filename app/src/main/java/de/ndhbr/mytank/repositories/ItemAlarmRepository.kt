package de.ndhbr.mytank.repositories

import de.ndhbr.mytank.data.ItemAlarmDao
import de.ndhbr.mytank.models.ItemAlarm

class ItemAlarmRepository private constructor(
    private val itemAlarmDao: ItemAlarmDao
) {

    // Add item alarm
    fun addItemAlarm(itemAlarm: ItemAlarm) =
        itemAlarmDao.addItemAlarm(itemAlarm)

    // Get item alarms by user id
    fun getItemAlarmsByUserId(userId: String) =
        itemAlarmDao.getItemAlarmsByUserId(userId)
}