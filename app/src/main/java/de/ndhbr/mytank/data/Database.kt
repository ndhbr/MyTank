package de.ndhbr.mytank.data

class Database private constructor() {
    val authDao = AuthDao()
    val tankDao = TanksDao()

    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: Database().also { instance = it }
            }
    }
}