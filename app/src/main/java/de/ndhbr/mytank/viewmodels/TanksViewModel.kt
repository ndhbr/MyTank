package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.repositories.TanksRepository

class TanksViewModel(private val tanksRepository: TanksRepository)
    : ViewModel() {

    fun addTank(tank: Tank) = tanksRepository.addTank(tank)

    fun getTanks() = tanksRepository.getTanks()
}