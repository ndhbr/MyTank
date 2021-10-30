package de.ndhbr.mytank.repositories

import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.data.TanksDao

class TanksRepository private constructor(private val tanksDao: TanksDao) {

    fun addTank(tank: Tank) {
        tanksDao.addTank(tank)
    }

    fun getTanks() = tanksDao.getQuotes()

    companion object {
        @Volatile
        private var instance: TanksRepository? = null

        fun getInstance(tanksDao: TanksDao) =
            instance ?: synchronized(this) {
                instance ?: TanksRepository(tanksDao).also { instance = it }
            }
    }
}