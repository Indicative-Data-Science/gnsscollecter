package uk.co.dawg.gnss.collector

import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar

class SnackbarExtensions

fun View.showErrorSnackbar(message: String?) {
    Snackbar
        .make(
            this,
            message ?: this.context.getString(R.string.default_error),
            Snackbar.LENGTH_SHORT
        )
        .setBackgroundTint(ContextCompat.getColor(this.context, R.color.colorError))
        .setTextColor(ContextCompat.getColor(this.context, R.color.colorOnError))
        .show()
}

fun ViewBinding.showErrorSnackbar(message: String?) {
    this.root.showErrorSnackbar(message)
}