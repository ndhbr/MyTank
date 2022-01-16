package de.ndhbr.mytank.data

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class ImageStorage private constructor() {
    // Firebase storage instance
    private val storage = FirebaseStorage.getInstance()
    private val reference = storage.reference

    // Upload file to the default bucket
    fun uploadFile(path: String, fileUri: Uri): UploadTask {
        val fileRef = reference.child(path)
        return fileRef.putFile(fileUri)
    }

    // Get image by path
    fun getImage(path: String): Task<Uri> {
        return reference.child(path).downloadUrl
    }

    // Remove image by path
    suspend fun removeImage(path: String): Void {
        return reference.child(path).delete().await()
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