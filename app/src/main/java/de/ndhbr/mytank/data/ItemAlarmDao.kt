package de.ndhbr.mytank.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.ndhbr.mytank.models.ItemAlarm
import de.ndhbr.mytank.models.User
import de.ndhbr.mytank.utilities.Constants
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ItemAlarmDao constructor(
    private val firestore: FirebaseFirestore
) {
    // Live data list
    private val itemAlarms = MutableLiveData<List<ItemAlarm>>()

    // Adds item alarm
    fun addItemAlarm(itemAlarm: ItemAlarm): Task<DocumentReference> {
        val user = AuthDao().user()

        itemAlarm.userId = user.userId
        return firestore
            .collection(Constants.COL_ALARMS)
            .add(itemAlarm)
    }

    // Updates item alarm
    fun updateItemAlarm(itemAlarm: ItemAlarm): Task<Void> {
        if (itemAlarm.itemAlarmId == null) {
            throw Exception("Item Alarm ID is null")
        }

        return firestore
            .collection(Constants.COL_ALARMS)
            .document(itemAlarm.itemAlarmId!!)
            .set(itemAlarm)
    }

    // Remove item alarm by id
    fun removeItemAlarmById(itemAlarmId: String): Task<Void> {
        return firestore
            .collection(Constants.COL_ALARMS)
            .document(itemAlarmId)
            .delete()
    }

    // Remove item alarms by tank id
    suspend fun removeAlarmsByTankId(tankId: String) {
        val snapshot = firestore
            .collection(Constants.COL_ALARMS)
            .whereEqualTo("tankId", tankId)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            for (doc in snapshot.documents) {
                doc.reference.delete().await()
            }
        }
    }

    // Remove item alarms by tank item id
    suspend fun removeAlarmsByTankItemId(tankId: String, tankItemId: String) {
        val snapshot = firestore
            .collection(Constants.COL_ALARMS)
            .whereEqualTo("tankId", tankId)
            .whereEqualTo("tankItemId", tankItemId)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            for (doc in snapshot.documents) {
                doc.reference.delete().await()
            }
        }
    }

    // Searches for live item alarms by user
    fun getItemAlarms(): LiveData<List<ItemAlarm>> {
        val user = AuthDao().user()

        firestore
            .collection(Constants.COL_ALARMS)
            .whereEqualTo("userId", user.userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(Constants.MAX_ALARMS)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    itemAlarms.value = ArrayList()
                }

                val savedItemAlarmList: MutableList<ItemAlarm> = mutableListOf()

                if (value != null) {
                    for (doc in value) {
                        val itemAlarm = doc.toObject(ItemAlarm::class.java)
                        itemAlarm.itemAlarmId = doc.id
                        savedItemAlarmList.add(itemAlarm)
                    }
                }

                itemAlarms.value = savedItemAlarmList
            }

        return itemAlarms
    }

    // Searches for item alarms by user
    suspend fun getItemAlarmsList(): List<ItemAlarm> {
        val result = ArrayList<ItemAlarm>()
        val user = AuthDao().user()

        val snapshot = firestore
            .collection(Constants.COL_ALARMS)
            .whereEqualTo("userId", user.userId)
            .limit(Constants.MAX_ALARMS)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            for (doc in snapshot.documents) {
                val itemAlarm = doc.toObject(ItemAlarm::class.java)

                if (itemAlarm != null) {
                    itemAlarm.itemAlarmId = doc.id
                    result.add(itemAlarm)
                }
            }
        }

        return result
    }

    // Returns a list of current alarms
    suspend fun getCurrentAlarmsList(): List<ItemAlarm> {
        val calendar = Calendar.getInstance()
        val result = ArrayList<ItemAlarm>()
        val user = AuthDao().user()

        val snapshot = firestore
            .collection(Constants.COL_ALARMS)
            .whereEqualTo("userId", user.userId)
            .whereEqualTo("hour", calendar.get(Calendar.HOUR_OF_DAY))
            .whereArrayContains("days", calendar.get(Calendar.DAY_OF_WEEK))
            .limit(Constants.MAX_ALARMS)
            .get()
            .await()

        if (!snapshot.isEmpty) {
            for (doc in snapshot.documents) {
                val itemAlarm = doc.toObject(ItemAlarm::class.java)

                if (itemAlarm != null) {
                    itemAlarm.itemAlarmId = doc.id
                    result.add(itemAlarm)
                }
            }
        }

        return result
    }
}