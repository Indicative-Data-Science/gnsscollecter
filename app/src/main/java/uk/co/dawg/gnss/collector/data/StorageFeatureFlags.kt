package uk.co.dawg.gnss.collector.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

/**
 * Fake feature flag mechanism for testing. Only works at compile time.
 */
class StorageFeatureFlags @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) {

    val enableGnnsMeasurements
        get() = remoteConfig.getBoolean(ENABLE_GNSS_STORING)

    val enableAntennaMeasurements
        get() = remoteConfig.getBoolean(ENABLE_ANTENNA_STORING)

    companion object {

        /**
         * Defines is values for GNSS data should be stored in the firebase store. If false,
         * they are never uploaded.
         */
        const val ENABLE_GNSS_STORING: String = "ENABLE_GNSS_STORING"

        /**
         * Defines is values for antenna data should be stored in the firebase store. If false,
         * they are never uploaded.
         */
        const val ENABLE_ANTENNA_STORING: String = "ENABLE_ANTENNA_STORING"
    }
}