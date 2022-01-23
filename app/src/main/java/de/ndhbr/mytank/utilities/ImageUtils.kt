package de.ndhbr.mytank.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import de.ndhbr.mytank.R

class ImageUtils {
    companion object {
        // Launch image picker intent
        fun showImagePicker(context: Context, intentLauncher: ActivityResultLauncher<Intent>) {
            var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
            chooseFile.type = "image/*"
            chooseFile = Intent.createChooser(
                chooseFile,
                context.getString(R.string.choose_a_picture)
            )
            intentLauncher.launch(chooseFile)
        }

        // Load online image uri into given imageview
        fun loadOnlineImageSource(context: Context, into: android.widget.ImageView, source: Uri) {
            Glide
                .with(context)
                .load(source)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .centerCrop()
                .into(into)
        }
    }
}