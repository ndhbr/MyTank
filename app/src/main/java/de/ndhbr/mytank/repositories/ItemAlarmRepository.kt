package de.ndhbr.mytank.repositories

import de.ndhbr.mytank.data.ItemAlarmDao
import de.ndhbr.mytank.models.ItemAlarm

class ItemAlarmRepository private constructor(
    private val itemAlarmDao: ItemAlarmDao
) {
    // Add item alarm
    fun addItemAlarm(itemAlarm: ItemAlarm) =
        itemAlarmDao.addItemAlarm(itemAlarm)

    // Update item alarm
    fun updateItemAlarm(itemAlarm: ItemAlarm) =
        itemAlarmDao.updateItemAlarm(itemAlarm)

    // Remove item alarm
    fun removeItemAlarmById(itemAlarmId: String) =
        itemAlarmDao.removeItemAlarmById(itemAlarmId)

    // Remove item alarms by tank
    suspend fun removeAlarmsByTankId(tankId: String) =
        itemAlarmDao.removeAlarmsByTankId(tankId)

    // Remove item alarms by tank item
    suspend fun removeAlarmsByTankItemId(tankId: String, tankItemId: String) =
        itemAlarmDao.removeAlarmsByTankItemId(tankId, tankItemId)

    // Get live item alarms by user id
    fun getItemAlarms() = itemAlarmDao.getItemAlarms()

    // Get item alarms by user id
    suspend fun getItemAlarmsList() =
        itemAlarmDao.getItemAlarmsList()

    companion object {
        @Volatile
        private var instance: ItemAlarmRepository? = null

        fun getInstance(itemAlarmDao: ItemAlarmDao) =
            instance ?: synchronized(this) {
                instance ?: ItemAlarmRepository(itemAlarmDao).also {
                    instance = it
                }
            }
    }
}