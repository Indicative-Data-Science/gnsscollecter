package uk.co.dawg.gnss.collector.extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object ActivityPermissionsExtensions

fun Activity.hasPermission(perm: String): Boolean {
    return ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED
}