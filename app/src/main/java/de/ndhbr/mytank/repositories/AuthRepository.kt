package de.ndhbr.mytank.repositories

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import de.ndhbr.mytank.data.AuthDao

class AuthRepository private constructor(private val authDao: AuthDao) {

    fun isLoggedIn(): Boolean {
        return authDao.isLoggedIn()
    }

    fun isLoggedInAs(): String {
        return authDao.isLoggedInAs() ?: "Anonymous"
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return authDao.login(email, password)
    }

    fun loginAnonymously(): Task<AuthResult> {
        return authDao.loginAnonymously()
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return authDao.register(email, password)
    }

    fun logout() {
        return authDao.logout()
    }

    fun sendPasswordResetMail(email: String): Task<Void> {
        return authDao.sendPasswordResetMail(email)
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(authDao: AuthDao) =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(authDao).also { instance = it }
            }
    }
}