package de.ndhbr.mytank.ui.home

import androidx.lifecycle.ViewModel
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.repositories.TankRepository

class TanksViewModel(private val tankRepository: TankRepository)
    : ViewModel() {

    fun addTank(tank: Tank) = tankRepository.addTank(tank)

    fun getTanks() = tankRepository.getTanks()
}