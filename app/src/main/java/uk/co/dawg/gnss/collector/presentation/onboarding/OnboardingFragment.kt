package uk.co.dawg.gnss.collector.presentation.onboarding

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
import uk.co.dawg.gnss.collector.databinding.FragmentOnboardingBinding
import uk.co.dawg.gnss.collector.domain.HasSeenOnboardingUseCase
import javax.inject.Inject

class OnboardingFragment : Fragment() {

    @Inject
    lateinit var hasSeenOnboardingUseCase: HasSeenOnboardingUseCase

    private lateinit var binding: FragmentOnboardingBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        binding.btnNext.setOnClickListener {
            hasSeenOnboardingUseCase.set(true)
            findNavController().navigate(R.id.action_frag_onboarding_to_frag_map)
        }

        return binding.root
    }
    
    @dagger.Module
    abstract class Module {
        @ContributesAndroidInjector
        abstract fun bind(): OnboardingFragment
    }
}