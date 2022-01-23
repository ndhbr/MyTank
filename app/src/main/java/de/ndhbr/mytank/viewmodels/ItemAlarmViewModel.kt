package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ndhbr.mytank.models.ItemAlarm
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TankItemsRepository
import de.ndhbr.mytank.repositories.TanksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ItemAlarmViewModel(
    private val itemAlarmRepository: ItemAlarmRepository,
    private val tanksRepository: TanksRepository,
    private val tankItemsRepository: TankItemsRepository
) :
    ViewModel() {

    // Add
    fun addItemAlarm(itemAlarm: ItemAlarm) =
        itemAlarmRepository.addItemAlarm(itemAlarm)

    // Update
    fun updateItemAlarm(itemAlarm: ItemAlarm) =
        itemAlarmRepository.updateItemAlarm(itemAlarm)

    // Remove
    fun removeItemAlarmById(itemAlarmId: String) =
        itemAlarmRepository.removeItemAlarmById(itemAlarmId)

    // Get
    fun getItemAlarms() = itemAlarmRepository.getItemAlarms()

    // Get Tanks
    fun getTanksList(onResultReceive: (result: List<Tank>) -> Unit) {
        viewModelScope.launch {
            val tanksList = tanksRepository.getTanksList()
            onResultReceive(tanksList)
        }
    }

    // Get Tank Items
    fun getTankItemsList(tankId: String,
                         onResultReceive: (result: List<TankItem>) -> Unit) {
        viewModelScope.launch {
            val tankItemsList = tankItemsRepository.getTankItemsList(tankId)
            onResultReceive(tankItemsList)
        }
    }
}