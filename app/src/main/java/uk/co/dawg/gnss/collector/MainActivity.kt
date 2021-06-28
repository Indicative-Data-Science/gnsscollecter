package uk.co.dawg.gnss.collector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.*
import uk.co.dawg.gnss.collector.presentation.map.MapFragment
import uk.co.dawg.gnss.collector.presentation.onboarding.OnboardingFragment
import uk.co.dawg.gnss.collector.presentation.splash.SplashFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector

    @dagger.Module
    abstract class Module {

        @ContributesAndroidInjector(
            modules = [
                SplashFragment.Module::class,
                OnboardingFragment.Module::class,
                MapFragment.Module::class
            ]
        )
        abstract fun contributeActivity(): MainActivity
    }
}