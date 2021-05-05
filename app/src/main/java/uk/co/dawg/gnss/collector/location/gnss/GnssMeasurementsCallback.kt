package uk.co.dawg.gnss.collector.location.gnss

import android.location.GnssMeasurement
import android.location.GnssMeasurementsEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.dawg.gnss.collector.data.GnssMeasurementsRepository
import javax.inject.Inject

class GnssMeasurementsCallback @Inject constructor(
    val repository: GnssMeasurementsRepository
) : GnssMeasurementsEvent.Callback() {

    override fun onGnssMeasurementsReceived(eventArgs: GnssMeasurementsEvent?) {
        super.onGnssMeasurementsReceived(eventArgs)

        val measurements = eventArgs?.measurements?: emptyList()

        GlobalScope.launch { repository.upload(measurements) }
    }

    override fun onStatusChanged(status: Int) {
        super.onStatusChanged(status)
    }
}