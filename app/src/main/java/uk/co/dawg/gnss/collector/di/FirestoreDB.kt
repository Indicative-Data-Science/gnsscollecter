package uk.co.dawg.gnss.collector.di

import androidx.annotation.StringDef
import uk.co.dawg.gnss.collector.di.FirestoreDB.Type.Companion.ANTENNA
import uk.co.dawg.gnss.collector.di.FirestoreDB.Type.Companion.MEASUREMENTS
import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class FirestoreDB(@Type val value: String) {


    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(MEASUREMENTS, ANTENNA)
    annotation class Type {
        companion object {
            const val MEASUREMENTS = "measurements"
            const val ANTENNA = "antenna"
        }
    }
}