package uk.co.dawg.gnss.collector.location.service

import android.Manifest
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.IBinder
import androidx.core.app.ActivityCompat
import dagger.android.AndroidInjection
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.dawg.gnss.collector.domain.UploadAndClearGnssMeasurementsUseCase
import uk.co.dawg.gnss.collector.location.gnss.GnssAntennaInfoListener
import uk.co.dawg.gnss.collector.location.gnss.GnssMeasurementsCallback
import uk.co.dawg.gnss.collector.location.gnss.GnssNavigationMessageCallback
import uk.co.dawg.gnss.collector.location.gnss.GnssStatusCallback
import java.util.concurrent.Executors
import javax.inject.Inject

class GnssCollectorService : Service(), ForegroundServiceHelper {

    @Inject
    lateinit var gnssMeasurementsCallback: GnssMeasurementsCallback

    @Inject
    lateinit var gnssNavigationMessageCallback: GnssNavigationMessageCallback

    @Inject
    lateinit var gnssStatusCallback: GnssStatusCallback

    @Inject
    lateinit var gnssAntennaInfoListener: GnssAntennaInfoListener

    lateinit var locationManager: LocationManager

    @Inject
    lateinit var uploadAndClearGnssMeasurementsUseCase: UploadAndClearGnssMeasurementsUseCase

    val exec = Executors.newFixedThreadPool(4)


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        when (intent?.action) {
            ForegroundServiceHelper.ACTION_STOP_SERVICE -> {

                GlobalScope.launch {
                    uploadAndClearGnssMeasurementsUseCase.run()
                }

                getNotificationManager().cancel(NOTIFICATION_ID)
                stopSelf()
            }
            else -> {
                // NOP
            }
        }

        return START_NOT_STICKY
    }

    override fun getNotificationManager(): NotificationManager {
        return getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)

        createNotificationChannel(this)
        startForeground(NOTIFICATION_ID, createNotification(this))

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        registerGnssListener()
    }

    override fun onDestroy() {
        unregisterGnssListeners()
        super.onDestroy()
    }

    private fun registerGnssListener() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            error("Should have permission granted before starting the service.")
        }
        locationManager.registerGnssMeasurementsCallback(exec, gnssMeasurementsCallback)
        locationManager.registerGnssNavigationMessageCallback(exec, gnssNavigationMessageCallback)
        locationManager.registerGnssStatusCallback(exec, gnssStatusCallback)
        locationManager.registerAntennaInfoListener(exec, gnssAntennaInfoListener)
    }

    private fun unregisterGnssListeners() {
        locationManager.unregisterAntennaInfoListener(gnssAntennaInfoListener)
        locationManager.unregisterGnssMeasurementsCallback(gnssMeasurementsCallback)
        locationManager.unregisterGnssNavigationMessageCallback(gnssNavigationMessageCallback)
        locationManager.unregisterGnssStatusCallback(gnssStatusCallback)
    }

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun contributeService(): GnssCollectorService
    }

    companion object {
        const val NOTIFICATION_ID = 101
    }
}