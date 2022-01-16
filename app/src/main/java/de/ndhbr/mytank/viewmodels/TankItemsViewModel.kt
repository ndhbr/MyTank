package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TankItemsRepository
import kotlinx.coroutines.launch

class TankItemsViewModel(
    private val tankItemsRepository: TankItemsRepository,
    private val alarmRepository: ItemAlarmRepository
) :
    ViewModel() {

    // Add
    fun addTankItem(tankId: String, tankItem: TankItem) =
        tankItemsRepository.addTankItem(tankId, tankItem)

    // Update
    fun updateTankItem(tankId: String, tankItem: TankItem) =
        tankItemsRepository.updateTankItem(tankId, tankItem)

    // Remove
    fun removeTankItemById(tankId: String, tankItemId: String) {
        viewModelScope.launch {
            tankItemsRepository.removeTankItemById(tankId, tankItemId)

            // The proper way would be of course to cascade with
            // cloud functions, but i think this would not
            // fit into a Kotlin course.
            alarmRepository.removeAlarmsByTankItemId(tankId, tankItemId)
        }
    }

    // List
    fun getTankItems(tankId: String) = tankItemsRepository.getTankItems(tankId)
}