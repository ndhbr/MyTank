package de.ndhbr.mytank.repositories

import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.data.TankDao

class TankRepository private constructor(private val tankDao: TankDao) {

    fun addTank(tank: Tank) {
        tankDao.addTank(tank)
    }

    fun getTanks() = tankDao.getQuotes()

    companion object {
        @Volatile
        private var instance: TankRepository? = null

        fun getInstance(tankDao: TankDao) =
            instance ?: synchronized(this) {
                instance ?: TankRepository(tankDao).also { instance = it }
            }
    }
}