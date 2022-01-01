package de.ndhbr.mytank.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.data.TanksDao

class TanksRepository private constructor(private val tanksDao: TanksDao) {

    // Add
    fun addTank(tank: Tank) = tanksDao.addTank(tank)

    // Update
    fun updateTank(tank: Tank) = tanksDao.updateTank(tank)

    // Remove
    fun removeTankById(tankId: String) = tanksDao.removeTankById(tankId)

    // List
    fun getTanks() = tanksDao.getTanks()

    companion object {
        @Volatile
        private var instance: TanksRepository? = null

        fun getInstance(tanksDao: TanksDao) =
            instance ?: synchronized(this) {
                instance ?: TanksRepository(tanksDao).also { instance = it }
            }
    }
}