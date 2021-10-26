package de.ndhbr.mytank.uitilities

import de.ndhbr.mytank.data.Database
import de.ndhbr.mytank.data.TankRepository
import de.ndhbr.mytank.ui.home.TanksViewModelFactory

object InjectorUtils {
    fun provideQuotesViewModelFactory(): TanksViewModelFactory {
        val tankRepository = TankRepository.getInstance(Database.getInstance().tankDao)
        return TanksViewModelFactory(tankRepository)
    }
}