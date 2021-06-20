package uk.co.dawg.gnss.collector.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            Timber.i("Remote Config Asyn performed with ${if (it.isSuccessful) "success" else "failure"}")
        }

        return remoteConfig
    }
}