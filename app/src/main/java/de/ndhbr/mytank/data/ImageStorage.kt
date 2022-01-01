package de.ndhbr.mytank.data

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage

class ImageStorage private constructor(){
    // Firebase storage instance
    val storage = FirebaseStorage.getInstance()
    val reference = storage.reference

    // Upload file to the default bucket
    fun uploadFile(fileUri: Uri) {

    }

    fun getImage(name: String): Task<Uri> {
        return reference.child(name).downloadUrl
    }

    companion object {
        @Volatile
        private var instance: ImageStorage? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ImageStorage().also { instance = it }
            }
    }

}