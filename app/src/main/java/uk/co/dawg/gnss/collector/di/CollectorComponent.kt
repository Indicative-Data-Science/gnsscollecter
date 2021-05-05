package uk.co.dawg.gnss.collector.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import uk.co.dawg.gnss.collector.CollectorApplication
import uk.co.dawg.gnss.collector.location.service.GnssCollectorService
import uk.co.dawg.gnss.collector.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        StorageModule::class,
        GnssCollectorService.Module::class,
        MainActivity.Module::class
    ]
)
interface CollectorComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): CollectorComponent
    }

    fun inject(application: CollectorApplication)
}