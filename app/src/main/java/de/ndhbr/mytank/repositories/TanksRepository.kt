package de.ndhbr.mytank.repositories

import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.data.TanksDao
import kotlinx.coroutines.runBlocking

class TanksRepository private constructor(private val tanksDao: TanksDao) {

    // Add
    fun addTank(tank: Tank) = tanksDao.addTank(tank)

    // Update
    fun updateTank(tank: Tank) = tanksDao.updateTank(tank)

    // Remove
    fun removeTankById(tankId: String) = tanksDao.removeTankById(tankId)

    // Live List
    fun getTanks() = tanksDao.getTanks()

    // List Tanks
    suspend fun getTanksList() = tanksDao.getTanksList()

    companion object {
        @Volatile
        private var instance: TanksRepository? = null

        fun getInstance(tanksDao: TanksDao) =
            instance ?: synchronized(this) {
                instance ?: TanksRepository(tanksDao).also { instance = it }
            }
    }
}