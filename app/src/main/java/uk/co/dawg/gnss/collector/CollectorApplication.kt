package uk.co.dawg.gnss.collector

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import uk.co.dawg.gnss.collector.di.DaggerCollectorComponent
import javax.inject.Inject

class CollectorApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerCollectorComponent.factory().create(this).inject(this)

        Timber.plant(Timber.DebugTree())

        FirebaseApp.initializeApp(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector
}