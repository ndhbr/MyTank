package de.ndhbr.mytank.data

import android.provider.ContactsContract
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.ndhbr.mytank.models.User
import java.lang.Exception

class AuthDao {
    private var auth: FirebaseAuth = Firebase.auth

    // Checks if user is logged in
    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Returns a the logged in mail address
    fun isLoggedInAs(): String? {
        return auth.currentUser?.email
    }

    // Requires signed in user
    fun user(): User {
        val authUser = auth.currentUser

        if (authUser != null) {
            return User(authUser.uid)
        }

        throw Exception("User not signed in")
    }

    // Login with email + password
    fun login(email: String, password: String): Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    // Login with credential
    fun loginWithCredential(credential: AuthCredential): Task<AuthResult> {
        return auth.signInWithCredential(credential)
    }

    // Login anonymously without email + password
    fun loginAnonymously(): Task<AuthResult> {
        return auth.signInAnonymously()
    }

    // Register with email + password
    fun register(email: String, password: String): Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    // Logout
    fun logout() {
        auth.signOut()
    }

    // Sends password reset mail to given email address
    fun sendPasswordResetMail(email: String): Task<Void> {
        return auth.sendPasswordResetEmail(email)
    }
}