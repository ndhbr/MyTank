package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import de.ndhbr.mytank.models.ItemAlarm
import de.ndhbr.mytank.repositories.ItemAlarmRepository

class ItemAlarmViewModel(private val itemAlarmRepository: ItemAlarmRepository) :
    ViewModel() {

    // Add
    fun addItemAlarm(itemAlarm: ItemAlarm) =
        itemAlarmRepository.addItemAlarm(itemAlarm)

    // Get
    fun getItemAlarmsByUserId(userId: String) =
        itemAlarmRepository.getItemAlarmsByUserId(userId)
}