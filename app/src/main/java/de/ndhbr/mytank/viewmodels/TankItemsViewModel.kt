package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.repositories.TankItemsRepository

class TankItemsViewModel(private val tankItemsRepository: TankItemsRepository) :
    ViewModel() {

    // Add
    fun addTankItem(tankId: String, tankItem: TankItem) =
        tankItemsRepository.addTankItem(tankId, tankItem)

    // Update
    fun updateTankItem(tankId: String, tankItem: TankItem) =
        tankItemsRepository.updateTankItem(tankId, tankItem)

    // Remove
    fun removeTankItemById(tankId: String, tankItemId: String) =
        tankItemsRepository.removeTankItemById(tankId, tankItemId)

    // List
    fun getTankItems(tankId: String) = tankItemsRepository.getTankItems(tankId)
}