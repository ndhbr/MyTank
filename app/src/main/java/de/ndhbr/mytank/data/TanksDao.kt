package de.ndhbr.mytank.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import de.ndhbr.mytank.models.Tank
import java.lang.Exception

class TanksDao constructor(private val firestore: FirebaseFirestore) {
    private val TAG = "TANKS_DAO"

    private val tankList = mutableListOf<Tank>()
    private val tanks = MutableLiveData<List<Tank>>()

    init {
        tanks.value = tankList
    }

    // Add tank to database
    fun addTank(tank: Tank): Task<DocumentReference> {
        return firestore.collection("/tanks").add(tank)
    }

    // Update tank in database
    fun updateTank(tank: Tank): Task<Void> {
        if (!tank.tankId.isNullOrEmpty()) {
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
        firestore.collection("/tanks").orderBy("createdAt").limit(10)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed", error)
                    tanks.value = ArrayList()
                }

                val savedTanksList: MutableList<Tank> = mutableListOf()

                for (doc in value!!) {
                    val tankItem = doc.toObject(Tank::class.java)
                    tankItem.tankId = doc.id
                    savedTanksList.add(tankItem)
                }

                tanks.value = savedTanksList
            }

        return tanks
    }
}