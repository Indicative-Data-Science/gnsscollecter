package uk.co.dawg.gnss.collector

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import uk.co.dawg.gnss.collector.di.DaggerCollectorComponent
import javax.inject.Inject

class CollectorApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()
        DaggerCollectorComponent.factory().create(this).inject(this)

        Timber.plant(Timber.DebugTree())

        FirebaseApp.initializeApp(this)

        remoteConfig.fetch().addOnCompleteListener { }
    }

    override fun androidInjector(): AndroidInjector<Any> = injector
}