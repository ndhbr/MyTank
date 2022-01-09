package de.ndhbr.mytank.repositories

import de.ndhbr.mytank.data.TankItemsDao
import de.ndhbr.mytank.models.TankItem

class TankItemsRepository private constructor(
    private val tankItemsDao: TankItemsDao
) {
    // Add
    fun addTankItem(tankId: String, tankItem: TankItem) =
        tankItemsDao.addTankItem(tankId, tankItem)

    // Update
    fun updateTankItem(tankId: String, tankItem: TankItem) =
        tankItemsDao.updateTank(tankId, tankItem)

    // Remove
    fun removeTankItemById(tankId: String, tankItemId: String) =
        tankItemsDao.removeTankItemById(tankId, tankItemId)

    // Live List
    fun getTankItems(tankId: String) =
        tankItemsDao.getTankItemsByTankId(tankId)

    // List Tank Items
    suspend fun getTankItemsList(tankId: String) =
        tankItemsDao.getTankItemsListByTankId(tankId)

    companion object {
        @Volatile
        private var instance: TankItemsRepository? = null

        fun getInstance(tankItemsDao: TankItemsDao) =
            instance ?: synchronized(this) {
                instance ?: TankItemsRepository(tankItemsDao).also {
                    instance = it
                }
            }
    }
}