package de.ndhbr.mytank.data

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Database private constructor() {

    // Firestore instance
    private val db = Firebase.firestore

    // Database objects
    val authDao = AuthDao()
    val tanksDao = TanksDao(db)
    val tankItemsDao = TankItemsDao(db)

    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Database().also { instance = it }
            }
    }
}