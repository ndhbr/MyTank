package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageException
import de.ndhbr.mytank.data.ImageStorage
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.repositories.ItemAlarmRepository
import de.ndhbr.mytank.repositories.TanksRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class TanksViewModel(
    private val tanksRepository: TanksRepository,
    private val alarmRepository: ItemAlarmRepository
) : ViewModel() {

    // Add
    fun addTank(tank: Tank) = tanksRepository.addTank(tank)

    // Update
    fun updateTank(tank: Tank) = tanksRepository.updateTank(tank)

    // Remove
    fun removeTank(tank: Tank, onFailure: CoroutineExceptionHandler) {
        viewModelScope.launch(onFailure) {
            tanksRepository.removeTankById(tank.tankId!!)

            // The proper way would be of course to cascade with
            // cloud functions, but i think this would not
            // fit into a Kotlin course.
            try {
                if (tank.hasImage == true) {
                    ImageStorage.getInstance().removeImage("${tank.tankId}.jpg")
                        .await()
                }
            } catch (e: StorageException) {
            }
            alarmRepository.removeAlarmsByTankId(tank.tankId!!)
        }
    }

    // List
    fun getTanks() = tanksRepository.getTanks()
}