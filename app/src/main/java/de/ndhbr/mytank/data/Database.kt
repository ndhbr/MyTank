package de.ndhbr.mytank.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Database private constructor() {

    init {
        FirebaseAuth.AuthStateListener {
            destroy()
        }
    }

    // Firestore instance
    private val db = Firebase.firestore

    // Database objects
    val tanksDao = TanksDao(db)
    val tankItemsDao = TankItemsDao(db)
    val itemAlarmDao = ItemAlarmDao(db)

    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Database().also { instance = it }
            }

        fun destroy() {
            synchronized(this) {
                instance = null
            }
        }
    }
}