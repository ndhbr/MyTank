package de.ndhbr.mytank.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import de.ndhbr.mytank.models.Tank
import de.ndhbr.mytank.models.User
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class TanksDao constructor(
    private val firestore: FirebaseFirestore,
    private val user: User
) {

    private val tankList = mutableListOf<Tank>()
    private val tanks = MutableLiveData<List<Tank>>()

    init {
        tanks.value = tankList
    }

    // Add tank to database
    fun addTank(tank: Tank): Task<DocumentReference> {
        tank.userId = user.userId
        return firestore.collection("/tanks").add(tank)
    }

    // Update tank in database
    fun updateTank(tank: Tank): Task<Void> {
        if (!tank.tankId.isNullOrEmpty()) {
            tank.userId = user.userId
            return firestore.collection("/tanks").document(tank.tankId!!).set(tank)
        }

        throw Exception("Missing tankId")
    }

    // Delete tank in database
    fun removeTankById(tankId: String): Task<Void> {
        if (!tankId.isNullOrEmpty()) {
            return firestore.collection("/tanks").document(tankId).delete()
        }

        throw Exception("Missing tankId")
    }

    // Get a live tanks list
    fun getTanks(): LiveData<List<Tank>> {
        firestore
            .collection("/tanks")
            .whereEqualTo("userId", user.userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(10) // TODO: Set max or infinite scroll
            .addSnapshotListener { value, error ->
                if (error != null) {
                    tanks.value = ArrayList()
                }

                val savedTanksList: MutableList<Tank> = mutableListOf()

                if (value != null) {
                    for (doc in value) {
                        val tankItem = doc.toObject(Tank::class.java)
                        tankItem.tankId = doc.id
                        savedTanksList.add(tankItem)
                    }
                }

                tanks.value = savedTanksList
            }

        return tanks
    }

    // Get tanks list
    suspend fun getTanksList(): List<Tank> {
        val result = ArrayList<Tank>()

        val snapshot = firestore
            .collection("/tanks")
            .whereEqualTo("userId", user.userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            for (doc in snapshot.documents) {
                val tank = doc.toObject(Tank::class.java)

                if (tank != null) {
                    tank.tankId = doc.id
                    result.add(tank)
                }
            }
        }

        return result
    }
}