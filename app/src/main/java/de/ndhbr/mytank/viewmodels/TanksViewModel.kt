package de.ndhbr.mytank.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.StorageException
import de.ndhbr.mytank.R
import de.ndhbr.mytank.data.ImageStorage
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TanksRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TanksViewModel(
    private val tanksRepository: TanksRepository,
    private val alarmRepository: ItemAlarmRepository
) : ViewModel() {

    // Add
    fun addTank(tank: Tank) = tanksRepository.addTank(tank)

    // Update
    fun updateTank(tank: Tank) = tanksRepository.updateTank(tank)

    // Remove
    fun removeTank(
        tank: Tank, context: Context,
        onFailure: CoroutineExceptionHandler
    ) {
        viewModelScope.launch(onFailure) {
            tanksRepository.removeTankById(tank.tankId!!)

            // The proper way would be of course to cascade with
            // cloud functions, but i think this would not
            // fit into a Kotlin course.
            try {
                if (tank.hasImage == true) {
                    ImageStorage.getInstance().removeImage(
                        String.format(
                            context.resources.getString(
                                R.string.fb_storage_tanks
                            ),
                            tank.tankId
                        )
                    ).await()
                }
            } catch (e: StorageException) {
            }
            alarmRepository.removeAlarmsByTankId(tank.tankId!!)
        }
    }

    // List
    fun getTanks() = tanksRepository.getTanks()
}