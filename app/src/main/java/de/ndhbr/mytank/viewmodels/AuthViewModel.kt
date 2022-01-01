package de.ndhbr.mytank.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import de.ndhbr.mytank.repositories.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return authRepository.login(email, password)
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return authRepository.register(email, password)
    }

    fun logout() {
        return authRepository.logout()
    }

    fun sendPasswordResetMail() {}
}