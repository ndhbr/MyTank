package de.ndhbr.mytank.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import de.ndhbr.mytank.models.ItemAlarm

class ItemAlarmDao constructor(private val firestore: FirebaseFirestore) {
    private val TAG = "ITEM_ALARM_DAO"

    // Adds item alarm
    fun addItemAlarm(itemAlarm: ItemAlarm): Task<DocumentReference> {
        return firestore
            .collection("/alarms")
            .add(itemAlarm)
    }

    // Searches for item alarms by user
    fun getItemAlarmsByUserId(userId: String): Task<QuerySnapshot> {
        return firestore.collection("/alarms")
            .whereEqualTo("userId", userId)
            .limit(1000)
            .get()
    }
}