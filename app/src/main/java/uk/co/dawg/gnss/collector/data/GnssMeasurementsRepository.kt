package uk.co.dawg.gnss.collector.data

import android.location.GnssMeasurement
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import uk.co.dawg.gnss.collector.di.FirestoreDB
import javax.inject.Inject

class GnssMeasurementsRepository @Inject constructor(
    @FirestoreDB(FirestoreDB.Type.MEASUREMENTS) val store: CollectionReference
) {
    suspend fun upload(measurements: Collection<GnssMeasurement>) {

        if (StorageFeatureFlags.DISABLE_MEASUREMENTS_STORING) return

        withContext(Dispatchers.IO) {
            measurements.forEach {
                try {
                    store.add(it.toMap()).await()
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }

            Timber.d("Submitted ${measurements.size} measurements")
        }
    }

    private fun GnssMeasurement.toMap(): Map<String, Any?> {
        return setOfNotNull(
            "accumulated_delta_range_meters" to this.accumulatedDeltaRangeMeters,
            "accumulatedDeltaRangeState" to this.accumulatedDeltaRangeState,
            "accumulated_delta_range_uncertainty_meters" to this.accumulatedDeltaRangeUncertaintyMeters,
            "automaticGainControlLevelDb" to this.automaticGainControlLevelDb,
            "basebandCn0DbHz" to this.basebandCn0DbHz,
            "carrierFrequencyHz" to this.carrierFrequencyHz,
            //"carrierFrequencyHz" to this.carrierPhase, Deprecated
            //"carrierFrequencyHz" to this.carrierPhaseUncertainty, Deprecated
            "cn0DbHz" to this.cn0DbHz,
            "constellationType" to this.constellationType,
            "fullInterSignalBiasNanos" to this.fullInterSignalBiasNanos,
            "fullInterSignalBiasUncertaintyNanos" to this.fullInterSignalBiasUncertaintyNanos,
            "multipathIndicator" to this.multipathIndicator,
            "pseudorangeRateMetersPerSecond" to this.pseudorangeRateMetersPerSecond,
            "pseudorangeRateUncertaintyMetersPerSecond" to this.pseudorangeRateUncertaintyMetersPerSecond,
            "receivedSvTimeNanos" to this.receivedSvTimeNanos,
            "receivedSvTimeUncertaintyNanos" to this.receivedSvTimeUncertaintyNanos,
            "satelliteInterSignalBiasNanos" to this.satelliteInterSignalBiasNanos,
            "satelliteInterSignalBiasUncertaintyNanos" to this.satelliteInterSignalBiasUncertaintyNanos,
            "snrInDb" to this.snrInDb,
            "state" to this.state,
            "svid" to this.svid,
            "timeOffsetNanos" to this.timeOffsetNanos,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                "codeType" to this.codeType
            } else null
        ).toMap()

    }
}