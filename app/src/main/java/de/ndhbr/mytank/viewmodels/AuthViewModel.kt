package de.ndhbr.mytank.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import de.ndhbr.mytank.repositories.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun isLoggedIn(): Boolean {
        return authRepository.isLoggedIn()
    }

    fun isLoggedInAs(): String {
        return authRepository.isLoggedInAs()
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return authRepository.login(email, password)
    }

    fun loginWithGoogle(idToken: String): Task<AuthResult> {
        return authRepository.loginWithGoogle(idToken)
    }

    fun loginAnonymously(): Task<AuthResult> {
        return authRepository.loginAnonymously()
    }

    fun register(email: String, password: String): Task<AuthResult> {
        return authRepository.register(email, password)
    }

    fun logout() {
        return authRepository.logout()
    }

    fun sendPasswordResetMail(email: String): Task<Void> {
        return authRepository.sendPasswordResetMail(email)
    }
}