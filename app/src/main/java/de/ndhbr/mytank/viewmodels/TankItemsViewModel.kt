package de.ndhbr.mytank.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageException
import de.ndhbr.mytank.R
import de.ndhbr.mytank.data.ImageStorage
import de.ndhbr.mytank.models.TankItem
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TankItemsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TankItemsViewModel(
    private val tankItemsRepository: TankItemsRepository,
    private val alarmRepository: ItemAlarmRepository
) :
    ViewModel() {

    // Add
    fun addTankItem(tankId: String, tankItem: TankItem) =
        tankItemsRepository.addTankItem(tankId, tankItem)

    // Update
    fun updateTankItem(tankId: String, tankItem: TankItem) =
        tankItemsRepository.updateTankItem(tankId, tankItem)

    // Remove
    fun removeTankItemById(
        tankId: String, tankItem: TankItem,
        context: Context,
        onFailure: CoroutineExceptionHandler
    ) {
        viewModelScope.launch(onFailure) {
            tankItemsRepository.removeTankItemById(tankId, tankItem.tankItemId!!)

            // The proper way would be of course to cascade with
            // cloud functions, but i think this would not
            // fit into a Kotlin course.
            try {
                if (tankItem.hasImage == true) {
                    ImageStorage.getInstance().removeImage(
                        String.format(
                            context.resources.getString(
                                R.string.fb_storage_tank_items
                            ),
                            "${tankId}_${tankItem.tankItemId}"
                        )
                    ).await()
                }
            } catch (e: StorageException) {
            }
            alarmRepository.removeAlarmsByTankItemId(tankId, tankItem.tankItemId!!)
        }
    }

    // List
    fun getTankItems(tankId: String) = tankItemsRepository.getTankItems(tankId)
}