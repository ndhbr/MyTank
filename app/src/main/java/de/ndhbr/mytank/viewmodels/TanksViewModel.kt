package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.repositories.TanksRepository

class TanksViewModel(private val tanksRepository: TanksRepository) : ViewModel() {

    // Add
    fun addTank(tank: Tank) = tanksRepository.addTank(tank)

    // Update
    fun updateTank(tank: Tank) = tanksRepository.updateTank(tank)

    // Remove
    fun removeTankById(tankId: String) = tanksRepository.removeTankById(tankId)

    // List
    fun getTanks() = tanksRepository.getTanks()
}