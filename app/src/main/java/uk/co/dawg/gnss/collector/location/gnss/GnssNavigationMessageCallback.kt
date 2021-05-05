package uk.co.dawg.gnss.collector.location.gnss

import android.location.GnssNavigationMessage
import javax.inject.Inject

class GnssNavigationMessageCallback @Inject constructor() : GnssNavigationMessage.Callback() {

    override fun onGnssNavigationMessageReceived(event: GnssNavigationMessage?) {
        super.onGnssNavigationMessageReceived(event)
    }

    override fun onStatusChanged(status: Int) {
        super.onStatusChanged(status)
    }
}