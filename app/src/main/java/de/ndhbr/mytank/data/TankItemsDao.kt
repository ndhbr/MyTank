package de.ndhbr.mytank.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import de.ndhbr.mytank.models.TankItem
import java.lang.Exception

class TankItemsDao constructor(private val firestore: FirebaseFirestore
) {
    private val TAG = "TANK_ITEMS_DAO"

    private val tankItemsList = mutableListOf<TankItem>()
    private val tankItems = MutableLiveData<List<TankItem>>()

    init {
        tankItems.value = tankItemsList
    }

    // Add tank item to tank
    fun addTankItem(tankId: String, tankItem: TankItem): Task<DocumentReference> {
        return firestore
            .collection("/tanks")
            .document(tankId)
            .collection("/items")
            .add(tankItem)
    }

    // Update tank item from tank
    fun updateTank(tankId: String, tankItem: TankItem): Task<Void> {
        if (!tankId.isNullOrEmpty() && !tankItem.tankItemId.isNullOrEmpty()) {
            return firestore
                .collection("/tanks")
                .document(tankId)
                .collection("/items")
                .document(tankItem.tankItemId!!)
                .set(tankItem)
        }

        throw Exception("Missing tankId or tankItemId")
    }

    // Delete tank item from tank
    fun removeTankItemById(tankId: String, tankItemId: String): Task<Void> {
        if (!tankId.isNullOrEmpty() && !tankItemId.isNullOrEmpty()) {
            return firestore
                .collection("/tanks")
                .document(tankId)
                .collection("/items")
                .document(tankItemId)
                .delete()
        }

        throw Exception("Missing tankId or tankItemId")
    }

    // Get a live list of items belonging to a tank
    fun getTankItemsByTankId(tankId: String): LiveData<List<TankItem>> {
        firestore
            .collection("/tanks")
            .document(tankId)
            .collection("/items")
            .orderBy("createdAt")
            .limit(10)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed", error)
                    tankItems.value = ArrayList()
                }

                val savedTankItemsList = mutableListOf<TankItem>()

                for (doc in value!!) {
                    val tankItem = doc.toObject(TankItem::class.java)
                    tankItem.tankItemId = doc.id
                    savedTankItemsList.add(tankItem)
                }

                tankItems.value = savedTankItemsList
            }

        return tankItems
    }
}