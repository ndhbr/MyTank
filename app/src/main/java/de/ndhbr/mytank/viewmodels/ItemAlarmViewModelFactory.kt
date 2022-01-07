package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.repositories.ItemAlarmRepository

class ItemAlarmViewModelFactory(private val itemAlarmRepository: ItemAlarmRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ItemAlarmViewModelFactory(itemAlarmRepository) as T
    }
}