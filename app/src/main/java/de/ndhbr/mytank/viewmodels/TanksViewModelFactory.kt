package de.ndhbr.mytank.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.ndhbr.mytank.repositories.TankRepository

class TanksViewModelFactory(private val tankRepository: TankRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TanksViewModel(tankRepository) as T
    }
}