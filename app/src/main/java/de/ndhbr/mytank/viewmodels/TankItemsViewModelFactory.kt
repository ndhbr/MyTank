package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TankItemsRepository

class TankItemsViewModelFactory(
    private val tankItemsRepository: TankItemsRepository,
    private val alarmRepository: ItemAlarmRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TankItemsViewModel(tankItemsRepository, alarmRepository) as T
    }
}