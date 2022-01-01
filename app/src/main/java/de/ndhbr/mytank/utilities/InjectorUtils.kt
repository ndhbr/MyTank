package de.ndhbr.mytank.utilities

import de.ndhbr.mytank.data.Database
import de.ndhbr.mytank.repositories.AuthRepository
import de.ndhbr.mytank.repositories.TankItemsRepository
import de.ndhbr.mytank.repositories.TanksRepository
import de.ndhbr.mytank.viewmodels.AuthViewModelFactory
import de.ndhbr.mytank.viewmodels.TankItemsViewModelFactory
import de.ndhbr.mytank.viewmodels.TanksViewModelFactory

object InjectorUtils {
    fun provideAuthViewModelFactory(): AuthViewModelFactory {
        val authRepository = AuthRepository.getInstance(Database.getInstance().authDao)
        return AuthViewModelFactory(authRepository)
    }

    fun provideTanksViewModelFactory(): TanksViewModelFactory {
        val tankRepository = TanksRepository.getInstance(Database.getInstance().tanksDao)
        return TanksViewModelFactory(tankRepository)
    }

    fun provideTankItemsViewModelFactory(): TankItemsViewModelFactory {
        val tankItemsRepository = TankItemsRepository.getInstance(
            Database.getInstance().tankItemsDao)
        return TankItemsViewModelFactory(tankItemsRepository)
    }
}