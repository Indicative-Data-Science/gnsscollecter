package uk.co.dawg.gnss.collector.data.storage

import android.location.GnssMeasurement
import android.location.Location
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gnss_data")
@Keep
data class GnssEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "accumulated_delta_range_meters") val accumulatedDeltaRangeMeters: Double?,
    @ColumnInfo(name = "accumulatedDeltaRangeState") val accumulatedDeltaRangeState: Int?,
    @ColumnInfo(name = "accumulated_delta_range_uncertainty_meters") val accumulatedDeltaRangeUncertaintyMeters: Double?,
    @ColumnInfo(name = "automaticGainControlLevelDb") val automaticGainControlLevelDb: Double?,
    @ColumnInfo(name = "basebandCn0DbHz") val basebandCn0DbHz: Double?,
    @ColumnInfo(name = "carrierFrequencyHz") val carrierFrequencyHz: Float?,
    @ColumnInfo(name = "cn0DbHz") val cn0DbHz: Double?,
    @ColumnInfo(name = "constellationType") val constellationType: Int?,
    @ColumnInfo(name = "fullInterSignalBiasNanos") val fullInterSignalBiasNanos: Double?,
    @ColumnInfo(name = "fullInterSignalBiasUncertaintyNanos") val fullInterSignalBiasUncertaintyNanos: Double?,
    @ColumnInfo(name = "multipathIndicator") val multipathIndicator: Int?,
    @ColumnInfo(name = "pseudorangeRateMetersPerSecond") val pseudorangeRateMetersPerSecond: Double?,
    @ColumnInfo(name = "pseudorangeRateUncertaintyMetersPerSecond") val pseudorangeRateUncertaintyMetersPerSecond: Double?,
    @ColumnInfo(name = "receivedSvTimeNanos") val receivedSvTimeNanos: Long?,
    @ColumnInfo(name = "receivedSvTimeUncertaintyNanos") val receivedSvTimeUncertaintyNanos: Long?,
    @ColumnInfo(name = "satelliteInterSignalBiasNanos") val satelliteInterSignalBiasNanos: Double?,
    @ColumnInfo(name = "satelliteInterSignalBiasUncertaintyNanos") val satelliteInterSignalBiasUncertaintyNanos: Double?,
    @ColumnInfo(name = "snrInDb") val snrInDb: Double?,
    @ColumnInfo(name = "state") val state: Int?,
    @ColumnInfo(name = "svid") val svid: Int?,
    @ColumnInfo(name = "timeOffsetNanos") val timeOffsetNanos: Double?,
    @ColumnInfo(name = "codeType") val codeType: String?,
    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "lng") val lng: Double?,
) {

    companion object {

        fun GnssMeasurement.toEntity(location: Location? = null): GnssEntity {
            return GnssEntity(
                id = null,
                accumulatedDeltaRangeMeters = this.accumulatedDeltaRangeMeters,
                accumulatedDeltaRangeState = this.accumulatedDeltaRangeState,
                accumulatedDeltaRangeUncertaintyMeters = this.accumulatedDeltaRangeUncertaintyMeters,
                automaticGainControlLevelDb = this.automaticGainControlLevelDb,
                basebandCn0DbHz = this.basebandCn0DbHz,
                carrierFrequencyHz = this.carrierFrequencyHz,
                cn0DbHz = this.cn0DbHz,
                constellationType = this.constellationType,
                fullInterSignalBiasNanos = this.fullInterSignalBiasNanos,
                fullInterSignalBiasUncertaintyNanos = this.fullInterSignalBiasUncertaintyNanos,
                multipathIndicator = this.multipathIndicator,
                pseudorangeRateMetersPerSecond = this.pseudorangeRateMetersPerSecond,
                pseudorangeRateUncertaintyMetersPerSecond = this.pseudorangeRateUncertaintyMetersPerSecond,
                receivedSvTimeNanos = this.receivedSvTimeNanos,
                receivedSvTimeUncertaintyNanos = this.receivedSvTimeUncertaintyNanos,
                satelliteInterSignalBiasNanos = this.satelliteInterSignalBiasNanos,
                satelliteInterSignalBiasUncertaintyNanos = this.satelliteInterSignalBiasUncertaintyNanos,
                snrInDb = this.snrInDb,
                state = this.state,
                svid = this.svid,
                timeOffsetNanos = this.timeOffsetNanos,
                codeType = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    this.codeType
                } else {
                    null
                },
                lat = location?.latitude,
                lng = location?.longitude
            )
        }
    }
}