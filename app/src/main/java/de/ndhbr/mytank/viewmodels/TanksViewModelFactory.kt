package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TanksRepository

class TanksViewModelFactory(
    private val tanksRepository: TanksRepository,
    private val tankAlarmRepository: ItemAlarmRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TanksViewModel(tanksRepository, tankAlarmRepository) as T
    }
}