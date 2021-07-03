package uk.co.dawg.gnss.collector.domain

import android.content.SharedPreferences
import androidx.annotation.Keep
import androidx.core.content.edit
import uk.co.dawg.gnss.collector.di.OverrideLocationPrefs
import javax.inject.Inject

class UserOverrideLocationRepository @Inject constructor(
    @OverrideLocationPrefs private val sharedPreferences: SharedPreferences
) {

    fun saveLocation(lat: Double, lng: Double) {
        sharedPreferences.edit {
            putString(KEY_LAT, lat.toString())
            putString(KEY_LNG, lng.toString())
            putLong(KEY_TIMESTAMP, System.currentTimeMillis())
        }
    }

    fun getOverrideLocation(): OverrideLocation? {
        return try {
            if (sharedPreferences.contains(KEY_TIMESTAMP)) {
                OverrideLocation(
                    sharedPreferences.getString(KEY_LAT, "0.00")!!.toDouble(),
                    sharedPreferences.getString(KEY_LNG, "0.00")!!.toDouble(),
                    sharedPreferences.getLong(KEY_TIMESTAMP, -1),
                )
            } else {
                null
            }
        } catch (e: NullPointerException) {
            null
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun clearOverrideLocation() {
        sharedPreferences.edit { clear() }
    }

    @Keep
    data class OverrideLocation(
        val latitude: Double,
        val longitude: Double,
        val timestamp: Long
    )

    companion object {
        const val KEY_LAT = "override_location_lat"
        const val KEY_LNG = "override_location_lng"
        const val KEY_TIMESTAMP = "override_location_timestamp"
    }

}