package uk.co.dawg.gnss.collector.location.gnss

import android.location.GnssStatus
import javax.inject.Inject

class GnssStatusCallback @Inject constructor(): GnssStatus.Callback() {
    override fun onStarted() {
        super.onStarted()
    }

    override fun onStopped() {
        super.onStopped()
    }

    override fun onFirstFix(ttffMillis: Int) {
        super.onFirstFix(ttffMillis)
    }

    override fun onSatelliteStatusChanged(status: GnssStatus) {
        super.onSatelliteStatusChanged(status)
    }
}