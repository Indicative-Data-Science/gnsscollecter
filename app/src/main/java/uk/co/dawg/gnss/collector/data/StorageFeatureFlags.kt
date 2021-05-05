package uk.co.dawg.gnss.collector.data

/**
 * Fake feature flag mechanism for testing. Only works at compile time.
 */
object StorageFeatureFlags {

    private const val DISABLE_ALL = false

    @Suppress("SimplifyBooleanWithConstants")
    const val DISABLE_MEASUREMENTS_STORING = DISABLE_ALL || false

    @Suppress("SimplifyBooleanWithConstants")
    const val DISABLE_ANTENNA_STORING = DISABLE_ALL || false
}