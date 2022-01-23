package de.ndhbr.mytank.utilities

import android.content.Context
import android.widget.Toast

class ToastUtilities {
    companion object {
        // Show short toast message
        fun showShortToast(context: Context, message: String) {
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

        // Show long toast message
        fun showLongToast(context: Context, message: String) {
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}