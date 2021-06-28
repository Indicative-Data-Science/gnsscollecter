package uk.co.dawg.gnss.collector.data

import android.location.GnssAntennaInfo
import com.google.firebase.firestore.CollectionReference
import uk.co.dawg.gnss.collector.di.FirestoreDB
import javax.inject.Inject

class GnssAntennaRepository @Inject constructor(
    @FirestoreDB(FirestoreDB.Type.ANTENNA) val store: CollectionReference,
    private val storageFeatureFlags: StorageFeatureFlags
) {

    fun upload() {

        if (!storageFeatureFlags.enableAntennaMeasurements) return

    }

    private fun GnssAntennaInfo.toMap(): Map<String, Any?> {
        return mapOf("carrierFrequencyMHz" to this.carrierFrequencyMHz)
            .plus(this.phaseCenterOffset.toMap())
            .plus(this.phaseCenterVariationCorrections.toMap("phaseCenterVariationCorrections"))
            .plus(this.signalGainCorrections.toMap("signalGainCorrections"))
    }

    private fun GnssAntennaInfo.PhaseCenterOffset.toMap(): Map<String, Any?> {
        val prefix = "phaseCenterOffset"

        return listOfNotNull(
            "${prefix}_xOffsetMm" to this.xOffsetMm,
            "${prefix}_xOffsetUncertaintyMm" to this.xOffsetUncertaintyMm,
            "${prefix}_yOffsetMm" to this.yOffsetMm,
            "${prefix}_yOffsetUncertaintyMm" to this.yOffsetUncertaintyMm,
            "${prefix}_zOffsetMm" to this.zOffsetMm,
            "${prefix}_zOffsetUncertaintyMm" to this.zOffsetUncertaintyMm,

            ).toMap()
    }

    private fun GnssAntennaInfo.SphericalCorrections?.toMap(prefix: String): Map<String, Any?> {

        return this?.let {
            listOfNotNull(
                "${prefix}_deltaPhi" to this.deltaPhi,
                "${prefix}_deltaTheta" to this.deltaTheta
            ).toMap()
        } ?: emptyMap()

    }
}


