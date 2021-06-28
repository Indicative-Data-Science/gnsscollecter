package uk.co.dawg.gnss.collector.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.GnssMeasurement
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import uk.co.dawg.gnss.collector.data.storage.GnssDao
import uk.co.dawg.gnss.collector.data.storage.GnssEntity.Companion.toEntity
import javax.inject.Inject

class GnssMeasurementsRepository @Inject constructor(
    private val context: Context,
    private val storageFeatureFlags: StorageFeatureFlags,
    private val dao: GnssDao,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    suspend fun upload(measurements: Collection<GnssMeasurement>) {

        if (!storageFeatureFlags.enableGnnsMeasurements) return

        withContext(Dispatchers.IO) {
            measurements.forEach {
                try {
                    val lastLocation = getLastLocationSafe()
                    dao.insertAll(it.toEntity(lastLocation))
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            Timber.d("Stored ${measurements.size} measurements")
        }
    }


    private suspend fun getLastLocationSafe(): Location? {
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            null
        } else {
            fusedLocationProviderClient.lastLocation.await()
        }
    }
}