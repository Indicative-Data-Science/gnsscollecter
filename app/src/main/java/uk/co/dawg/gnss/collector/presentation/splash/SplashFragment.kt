package uk.co.dawg.gnss.collector.presentation.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjection
import uk.co.dawg.gnss.collector.R
import uk.co.dawg.gnss.collector.domain.HasSeenOnboardingUseCase
import javax.inject.Inject

class SplashFragment : Fragment() {

    @Inject
    lateinit var hasSeenOnboardingUseCase: HasSeenOnboardingUseCase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (hasSeenOnboardingUseCase.get()) {
            findNavController().navigate(R.id.action_frag_splash_to_frag_map)
        } else {
            findNavController().navigate(R.id.action_frag_splash_to_frag_onboarding)
        }


    }

    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun bind(): SplashFragment
    }
}