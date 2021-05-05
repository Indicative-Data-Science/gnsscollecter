package uk.co.dawg.gnss.collector.location.gnss

import android.location.GnssAntennaInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import uk.co.dawg.gnss.collector.data.GnssAntennaRepository
import javax.inject.Inject

class GnssAntennaInfoListener @Inject constructor(
    val repository: GnssAntennaRepository
) : GnssAntennaInfo.Listener {

    override fun onGnssAntennaInfoReceived(gnssAntennaInfos: MutableList<GnssAntennaInfo>) {
        GlobalScope.launch { repository.upload(gnssAntennaInfos) }
    }
}