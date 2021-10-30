package de.ndhbr.mytank.uitilities

import de.ndhbr.mytank.data.Database
import de.ndhbr.mytank.repositories.AuthRepository
import de.ndhbr.mytank.repositories.TanksRepository
import de.ndhbr.mytank.viewmodels.AuthViewModelFactory
import de.ndhbr.mytank.viewmodels.TanksViewModelFactory

object InjectorUtils {
    fun provideAuthViewModelFactory(): AuthViewModelFactory {
        val authRepository = AuthRepository.getInstance(Database.getInstance().authDao)
        return AuthViewModelFactory(authRepository)
    }

    fun provideTanksViewModelFactory(): TanksViewModelFactory {
        val tankRepository = TanksRepository.getInstance(Database.getInstance().tankDao)
        return TanksViewModelFactory(tankRepository)
    }
}