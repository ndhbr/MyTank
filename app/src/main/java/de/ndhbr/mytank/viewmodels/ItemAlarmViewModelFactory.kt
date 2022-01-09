package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TankItemsRepository
import de.ndhbr.mytank.repositories.TanksRepository

class ItemAlarmViewModelFactory(
    private val itemAlarmRepository: ItemAlarmRepository,
    private val tanksRepository: TanksRepository,
    private val tankItemsRepository: TankItemsRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemAlarmViewModel(itemAlarmRepository, tanksRepository, tankItemsRepository) as T
    }
}